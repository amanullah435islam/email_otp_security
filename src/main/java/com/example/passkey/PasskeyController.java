package com.example.passkey;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.yubico.webauthn.data.ByteArray;
import com.yubico.webauthn.data.PublicKeyCredential;
import jakarta.annotation.PostConstruct;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonInclude;

@RestController
@RequestMapping("/auth/passkey")
public class PasskeyController {

    @Autowired
    private PasskeyService service;
      
    @Autowired
    private ObjectMapper objectMapper;

    private static final Logger log = LoggerFactory.getLogger(PasskeyController.class);

    
    
//  // OBJECTMAPPER USE
    @PostConstruct
    public void init() {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        
        objectMapper.configure(
        	    com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
        	    false
        	);
    }
    
    
    
//     //REGISTER CHALLENGE AND REGISTER CHECK
    @PostMapping("/register-challenge")
    public Object registerChallenge(@RequestBody Map<String, String> body) {

        var options = service.generateRegisterChallenge(body.get("email"));

        Map<String, Object> response = new HashMap<>();

        response.put("rp", options.getRp());
        response.put("user", options.getUser());
        //response.put("challenge", options.getChallenge());
        
        response.put("challenge",
        	    Base64.getUrlEncoder().withoutPadding()
        	        .encodeToString(options.getChallenge().getBytes())
        	);
        response.put("pubKeyCredParams", options.getPubKeyCredParams());
        response.put("timeout", options.getTimeout());

        // ✅ convert Optional → proper value
        response.put("excludeCredentials",
                options.getExcludeCredentials().orElse(Set.of()));

        response.put("authenticatorSelection",
                options.getAuthenticatorSelection().orElse(null));

        response.put("attestation", options.getAttestation());

        // ❌ extensions একদম দিও না

        
        log.info("REGISTER HIT");
        return response;
    }
 
    
    
//  //LOGIN CHALLENGE AND LOGIN CHECK
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
    
    
    
//  // BASE64 CONVERT
    private ByteArray base64ToByteArray(String base64) {
        base64 = base64.replace('-', '+').replace('_', '/');
        return new ByteArray(
                Base64.getDecoder().decode(base64)
        );
    }
    
    
    
//  // REGISTER METHODE
    @PostMapping("/register")
    public String register(@RequestBody Map<String, Object> req) throws Exception {

        String email = (String) req.get("email");

        String json = objectMapper.writeValueAsString(req.get("credential"));

        var credential =
                PublicKeyCredential.parseRegistrationResponseJson(json);

        service.finishRegistration(credential, email);

        return "Registered Successfully";
    }
    
    
    
//    // LOGIN METHODE
    @PostMapping("/login")
    public Object login(@RequestBody Map<String, Object> body) throws Exception {
        return service.login(body);
    }
    
    
}
