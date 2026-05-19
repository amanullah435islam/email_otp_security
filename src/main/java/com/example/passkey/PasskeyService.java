package com.example.passkey;

import com.example.demo.model.User;
import com.example.demo.repo.UserRepository;
import com.example.security.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yubico.webauthn.*;
import com.yubico.webauthn.data.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class PasskeyService {

    @Autowired
    private RelyingParty relyingParty;

    @Autowired
    private PasskeyRepository repo;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtil jwtUtil;

    // STORE
    private Map<String, PublicKeyCredentialCreationOptions> registerRequestStore = new ConcurrentHashMap<>();
    private Map<String, AssertionRequest> loginRequestStore = new ConcurrentHashMap<>();

    private static final Logger log = LoggerFactory.getLogger(PasskeyService.class);

    // =========================
    // REGISTER CHALLENGE
    // =========================
    public PublicKeyCredentialCreationOptions generateRegisterChallenge(String email) {

        PublicKeyCredentialCreationOptions original =
                relyingParty.startRegistration(
                        StartRegistrationOptions.builder()
                                .user(
                                        UserIdentity.builder()
                                                .name(email)
                                                .displayName(email)
                                                .id(new ByteArray(email.getBytes()))
                                                .build()
                                )
                                .build()
                );

        registerRequestStore.put(email, original);

        return PublicKeyCredentialCreationOptions.builder()
                .rp(original.getRp())
                .user(original.getUser())
                .challenge(original.getChallenge())
                .pubKeyCredParams(original.getPubKeyCredParams())
                .timeout(original.getTimeout())
                .excludeCredentials(Optional.empty())
                .attestation(original.getAttestation())
                .authenticatorSelection(
                        AuthenticatorSelectionCriteria.builder()
                                .authenticatorAttachment(AuthenticatorAttachment.PLATFORM)
                                .userVerification(UserVerificationRequirement.REQUIRED)
                                .residentKey(ResidentKeyRequirement.REQUIRED)
                                .build()
                )
                .build();
    }

    // =========================
    // LOGIN CHALLENGE
    // =========================
    public AssertionRequest generateLoginChallenge(String email) {

        List<PublicKeyCredentialDescriptor> credentials = repo.findByUserEmail(email)
                .stream()
                .map(c -> PublicKeyCredentialDescriptor.builder()
                        .id(new ByteArray(
                                Base64.getDecoder().decode(c.getCredentialIdBase64())
                        ))
                        .type(PublicKeyCredentialType.PUBLIC_KEY)
                        .transports(Set.of(AuthenticatorTransport.INTERNAL))
                        .build())
                .toList();

        AssertionRequest request =
                relyingParty.startAssertion(
                        StartAssertionOptions.builder()
                                .username(email)
                                .userVerification(UserVerificationRequirement.REQUIRED)
                                .build()
                );

        loginRequestStore.put(email, request);

        return request;
    }

    // =========================
    // FINISH REGISTRATION
    // =========================
    public void finishRegistration(
            PublicKeyCredential<
                    AuthenticatorAttestationResponse,
                    ClientRegistrationExtensionOutputs
            > credential,
            String email
    ) throws Exception {

        if (!repo.findByUserEmail(email).isEmpty()) {
            throw new RuntimeException("Passkey already exists");
        }

        PublicKeyCredentialCreationOptions request = registerRequestStore.get(email);

        if (request == null) {
            throw new IllegalStateException("No registration request found");
        }

        RegistrationResult result =
                relyingParty.finishRegistration(
                        FinishRegistrationOptions.builder()
                                .request(request)
                                .response(credential)
                                .build()
                );

//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found"));
        System.out.println("EMAIL = " + email);

        Optional<User> userOpt =
                userRepository.findByEmail(email);

        System.out.println("USER = " + userOpt);
        
        PasskeyCredential entity = new PasskeyCredential();

        User user = userOpt
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );
        entity.setUser(user);
        // Base64 encode
        entity.setCredentialIdBase64(
                Base64.getEncoder().encodeToString(
                        result.getKeyId().getId().getBytes()
                )
        );

        
        entity.setPublicKey(
                Base64.getEncoder().encodeToString(
                        result.getPublicKeyCose().getBytes()
                )
        );

        entity.setSignatureCount(result.getSignatureCount());

        repo.save(entity);

        registerRequestStore.remove(email);

        log.info("Passkey registered for {}", email);
    }

    // =========================
    // FINISH LOGIN
    // =========================
    public boolean finishLogin(
            PublicKeyCredential<
                    AuthenticatorAssertionResponse,
                    ClientAssertionExtensionOutputs
            > credential,
            String email
    ) throws Exception {

        AssertionRequest request = loginRequestStore.get(email);

        if (request == null) {
            throw new RuntimeException("No login request found");
        }

        AssertionResult result =
                relyingParty.finishAssertion(
                        FinishAssertionOptions.builder()
                                .request(request)
                                .response(credential)
                                .build()
                );

        loginRequestStore.remove(email);

        // ✅ signature count update
        if (result.isSuccess()) {
            repo.findByUserEmail(email).forEach(c -> {
                c.setSignatureCount(result.getSignatureCount());
                repo.save(c);
            });
        }

        log.info("Passkey logged for {}", email);

        return result.isSuccess() && result.isUserVerified();
    }

    // =========================
    // LOGIN (FINAL API)
    // =========================
    public Object login(Map<String, Object> body) throws Exception {

        String email = (String) body.get("email");

        Map<String, Object> assertion =
                (Map<String, Object>) body.get("assertion");

        String json = objectMapper.writeValueAsString(assertion);

        var credential =
                PublicKeyCredential.parseAssertionResponseJson(json);

        boolean success = finishLogin(credential, email);

        if (!success) {
            return Map.of(
                    "success", false,
                    "message", "Login Failed"
            );
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isVerified()) {
            return Map.of(
                    "success", false,
                    "message", "Email not verified"
            );
        }

        String token = jwtUtil.generateToken(
                email,
                user.getRole()
        );

        return Map.of(
                "success", true,
                "message", "Login Success",
                "token", token,
                "role", user.getRole()
        );
    }
}



















// //pore check korbo::::::::::::::


//@Service
//public class PasskeyService {
//
//    @Autowired
//    private RelyingParty relyingParty;
//    
//    @Autowired
//    private PasskeyRepository repo;   
//    
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//    
//    @Autowired
//    private JwtUtil jwtUtil;
//    
//    
////  // STORE
//    private Map<String, PublicKeyCredentialCreationOptions> registerRequestStore = new ConcurrentHashMap<>();
//    private Map<String, AssertionRequest> loginRequestStore = new ConcurrentHashMap<>();
//    
//
//    
////  // PRODUCT LEVEL DEBUG CHECK    
//    private static final Logger log = LoggerFactory.getLogger(PasskeyService.class);
//    
//    
//    
////  // REGISTER CHALLENGE
//    public PublicKeyCredentialCreationOptions generateRegisterChallenge(String email) {
//
//        PublicKeyCredentialCreationOptions original =
//                relyingParty.startRegistration(
//                        StartRegistrationOptions.builder()
//                                .user(
//                                        UserIdentity.builder()
//                                                .name(email)
//                                                .displayName(email)
//                                                .id(new ByteArray(email.getBytes()))
//                                                .build()
//                                )
//                                .build()
//                );
//
//        registerRequestStore.put(email, original);
//
//        return PublicKeyCredentialCreationOptions.builder()
//                .rp(original.getRp())
//                .user(original.getUser())
//                .challenge(original.getChallenge())
//                .pubKeyCredParams(original.getPubKeyCredParams())
//                .timeout(original.getTimeout())
//
//                // ✅ MUST BE EMPTY ARRAY              
//                .excludeCredentials(Optional.empty())   // 🔥 BEST FIX
//
//                .attestation(original.getAttestation())
//
//                // ❌ NO extensions at all
//                .authenticatorSelection(
//                        AuthenticatorSelectionCriteria.builder()
//                                .authenticatorAttachment(AuthenticatorAttachment.PLATFORM)
//                                .userVerification(UserVerificationRequirement.REQUIRED)
//                                .residentKey(ResidentKeyRequirement.REQUIRED)
//                                .build()
//                )
//                .build();
//    }
//
//    
//    
////    // LOGIN CHALLENGE
//    public AssertionRequest generateLoginChallenge(String email) {
//
//    	List<PublicKeyCredentialDescriptor> credentials = repo.findByEmail(email)
//    		    .stream()
//    		    .map(c -> PublicKeyCredentialDescriptor.builder()
//    		        //.id(new ByteArray(c.getCredentialId()))
//    		        .id(new ByteArray(
//                            Base64.getDecoder().decode(c.getCredentialIdBase64())
//                    ))
//    		        .type(PublicKeyCredentialType.PUBLIC_KEY)
//
//    		        // 🔥 MOST IMPORTANT LINE
//    		        .transports(Set.of(AuthenticatorTransport.INTERNAL))
//
//    		        .build())
//    		    .toList();
//    	
//    	AssertionRequest request =
//    		    relyingParty.startAssertion(
//    		        StartAssertionOptions.builder()
//    		            .username(email)
//    		            .userVerification(UserVerificationRequirement.REQUIRED)
//    		            .allowCredentials(credentials) // 🔥 ADD THIS
//    		            .build()
//    		    );
//        
//    	
//        loginRequestStore.put(email, request);
//
//        return request;
//    }
//    
//    
//      
////     // VERIFY REGISTRATION::::::::::
//    public void finishRegistration(
//            PublicKeyCredential<
//                    AuthenticatorAttestationResponse,
//                    ClientRegistrationExtensionOutputs
//            > credential,
//            String email
//    ) throws Exception {
//    	
//    	if (!repo.findByEmail(email).isEmpty()) {
//    	    throw new RuntimeException("Passkey already exists");
//    	}
//
//        PublicKeyCredentialCreationOptions request =
//                registerRequestStore.get(email);
//
//        if (request == null) {
//            //throw new RuntimeException("No registration request found");
//            throw new IllegalStateException("No registration request found");
//        }
//
//        RegistrationResult result =
//                relyingParty.finishRegistration(
//
//                        FinishRegistrationOptions.builder()
//                                .request(request)
//                                .response(credential)
//                                .build()
//                );
//
//        // ✅ SAVE TO DB
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        
//        PasskeyCredential entity = new PasskeyCredential();
//
//        entity.setUser(user);  // 🔥 IMPORTANT
//        //entity.setCredentialId(result.getKeyId().getId().getBytes());
//       // entity.setPublicKey(result.getPublicKeyCose().getBytes());        
//     
//        // ✅ credentialId → Base64
//        entity.setCredentialIdBase64(
//                Base64.getEncoder().encodeToString(
//                        result.getKeyId().getId().getBytes()
//                )
//        );
//        
//     // ✅ publicKey → Base64
//        entity.setPublicKey(
//                Base64.getEncoder().encodeToString(
//                        result.getPublicKeyCose().getBytes()
//                )
//        );
//        entity.setSignatureCount(result.getSignatureCount());
//
//        repo.save(entity);
//
//        // 🔥 IMPORTANT LINE (এইটা তুমি জিজ্ঞেস করছো)
//        registerRequestStore.remove(email);
//        //System.out.println("Passkey registered for: " + email);     // just debug
//        log.info("Passkey registered for {}", email);			//production level
//    }
//    
//    
//          
////     // VERIFY LOGIN::::::::::
//    public boolean finishLogin(
//            PublicKeyCredential<
//                    AuthenticatorAssertionResponse,
//                    ClientAssertionExtensionOutputs
//            > credential,
//            String email
//    ) throws Exception {
//
//    	
//        AssertionRequest request =
//                loginRequestStore.get(email);
//        if (request == null) {
//            throw new RuntimeException("No login request found");
//        }
//        
//        AssertionResult result =
//                relyingParty.finishAssertion(
//
//                        FinishAssertionOptions.builder()
//                                .request(request) // ✅ correct
//                                .response(credential)
//                                .build()
//                );
//        
//        
//        // 🔥 IMPORTANT LINE (এইটা তুমি জিজ্ঞেস করছো)
//        loginRequestStore.remove(email);
//        
//        
//        //System.out.println("Passkey loged for: " + email);
//        log.info("Passkey loged for {}", email);
//        
//       return result.isSuccess() && result.isUserVerified();      
//    }
// 
//    
//
//    public Object login(Map<String, Object> body) throws Exception {
//
//        String email = (String) body.get("email");
//
//        String json = objectMapper.writeValueAsString(
//                body.get("assertion")
//        );
//
//        var credential =
//                PublicKeyCredential.parseAssertionResponseJson(json);
//
//        boolean success = finishLogin(credential, email);
//
//        if (!success) {
//            return Map.of(
//                    "success", false,
//                    "message", "Login Failed"
//            );
//        }
//
//        User user = userRepository.findByEmail(email)
//        		 .orElseThrow(() -> new RuntimeException("User not found"));
//
//        if (user == null) {
//            return Map.of(
//                    "success", false,
//                    "message", "User not found"
//            );
//        }
//
//        if (!user.isVerified()) {
//            return Map.of(
//                    "success", false,
//                    "message", "Email not verified"
//            );
//        }
//
//        String token = jwtUtil.generateToken(
//                email,
//                user.getRole()
//        );
//
//        return Map.of(
//                "success", true,
//                "message", "Login Success",
//                "token", token,
//                "role", user.getRole()
//        );
//    }
//}
