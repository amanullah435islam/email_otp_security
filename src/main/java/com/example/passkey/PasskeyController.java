package com.example.passkey;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.yubico.webauthn.data.ByteArray;
import com.yubico.webauthn.data.PublicKeyCredential;

import jakarta.annotation.PostConstruct;

import com.yubico.webauthn.data.AuthenticatorAttestationResponse;
import com.yubico.webauthn.data.ClientRegistrationExtensionOutputs;
import com.yubico.webauthn.data.AuthenticatorAssertionResponse;
import com.yubico.webauthn.data.ClientAssertionExtensionOutputs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonInclude;

@RestController
@RequestMapping("/auth/passkey")
public class PasskeyController {

    @Autowired
    private PasskeyService service;
    
   
    @Autowired
    private ObjectMapper objectMapper;

    
    @PostConstruct
    public void init() {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        
        objectMapper.configure(
        	    com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
        	    false
        	);
    }
    // REGISTER CHALLENGE
    @PostMapping("/register-challenge")
    public Object registerChallenge(@RequestBody Map<String, String> body) {

        var options = service.generateRegisterChallenge(body.get("email"));

        Map<String, Object> response = new HashMap<>();

        response.put("rp", options.getRp());
        response.put("user", options.getUser());
        response.put("challenge", options.getChallenge());
        response.put("pubKeyCredParams", options.getPubKeyCredParams());
        response.put("timeout", options.getTimeout());

        // ✅ convert Optional → proper value
        response.put("excludeCredentials",
                options.getExcludeCredentials().orElse(Set.of()));

        response.put("authenticatorSelection",
                options.getAuthenticatorSelection().orElse(null));

        response.put("attestation", options.getAttestation());

        // ❌ extensions একদম দিও না

        System.out.println("REGISTER HIT");
        return response;
    }
 
    
//    //only LOGIN CHALLENGE		
//     //ai code tik ace but nicer ta user kora holo:
    
//    @PostMapping("/login-challenge")
//    public Object loginChallenge(
//            @RequestBody Map<String, String> body
//    ) {
//
//        return service.generateLoginChallenge(
//                body.get("email")
//        );
//    }
    
    
//  //LOGIN CHALLENGE and login check	
    @PostMapping("/login-challenge")
    public Object loginChallenge(@RequestBody Map<String, String> body) {

        var request = service.generateLoginChallenge(body.get("email"));
        var options = request.getPublicKeyCredentialRequestOptions();

        Map<String, Object> response = new HashMap<>();

        Map<String, Object> pk = new HashMap<>();

        pk.put("challenge", options.getChallenge());
        pk.put("rpId", options.getRpId());
        pk.put("timeout", options.getTimeout());

        // ✅ allowCredentials clean fix
        pk.put("allowCredentials",
                options.getAllowCredentials()
                	.orElse(List.of())   // Optional handle
	                .stream()
	                .map(c -> {
	                    Map<String, Object> m = new HashMap<>();
	                    m.put("type", c.getType());
	                    m.put("id", c.getId());
	
	                    // 🔥 IMPORTANT FIX
	                    m.put("transports", List.of("internal"));
	
	                    return m;
	                }).toList()
        );

        // 🔥 IMPORTANT FIX
        pk.put("userVerification", "required");

        // ❌ DON'T add extensions

        response.put("publicKeyCredentialRequestOptions", pk);
        response.put("username", request.getUsername());

        return response;
    }
    
    
    
    
    
    private ByteArray base64ToByteArray(String base64) {
        base64 = base64.replace('-', '+').replace('_', '/');
        return new ByteArray(
                Base64.getDecoder().decode(base64)
        );
    }
    
    private PublicKeyCredential<
				    AuthenticatorAttestationResponse,
				    ClientRegistrationExtensionOutputs
				> parseRegisterCredential(Object credentialObj) throws Exception {

	String json = objectMapper.writeValueAsString(credentialObj);
	
	return PublicKeyCredential.parseRegistrationResponseJson(json);
	}
    
    
    
    private PublicKeyCredential<
				    AuthenticatorAssertionResponse,
				    ClientAssertionExtensionOutputs
					> parseLoginCredential(Object assertionObj) throws Exception {
	
	String json = objectMapper.writeValueAsString(assertionObj);
	
	return PublicKeyCredential.parseAssertionResponseJson(json);
	} 
    
    
//    @PostMapping("/register")
//    public String register(@RequestBody PasskeyRequest req) throws Exception {
//
//    	if (req.credential == null) {
//            return "Credential missing";
//        }
//    	
//        var credential =
//                //parseRegisterCredential(req.credential);
//
//        		 PublicKeyCredential.parseRegistrationResponseJson(
//                         objectMapper.writeValueAsString(req.credential)
//                 );
//        
//        service.finishRegistration(
//                credential,
//                req.email
//        );
//
//        return "Registered Successfully";
//    }
    
    
    
    @PostMapping("/register")
    public String register(@RequestBody Map<String, Object> req) throws Exception {

        String email = (String) req.get("email");

        String json = objectMapper.writeValueAsString(req.get("credential"));

        var credential =
                PublicKeyCredential.parseRegistrationResponseJson(json);

        service.finishRegistration(credential, email);

        return "Registered Successfully";
    }
    
    
    @PostMapping("/login")
    public Object login(@RequestBody Map<String, Object> body) throws Exception {
        return service.login(body);
    }
    
    
}
