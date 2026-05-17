package com.example.passkey;

import java.util.Base64;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.yubico.webauthn.data.ByteArray;
import com.yubico.webauthn.data.PublicKeyCredential;

import com.yubico.webauthn.data.AuthenticatorAttestationResponse;
import com.yubico.webauthn.data.ClientRegistrationExtensionOutputs;
import com.yubico.webauthn.data.AuthenticatorAssertionResponse;
import com.yubico.webauthn.data.ClientAssertionExtensionOutputs;

import com.fasterxml.jackson.databind.ObjectMapper;


@RestController
@RequestMapping("/auth/passkey")
public class PasskeyController {

    @Autowired
    private PasskeyService service;
    
   

    private final ObjectMapper objectMapper = new ObjectMapper();

    // REGISTER CHALLENGE
    @PostMapping("/register-challenge")
    public Object registerChallenge(
            @RequestBody Map<String, String> body
    ) {

        return service.generateRegisterChallenge(
                body.get("email")
        );
    }

    // LOGIN CHALLENGE
    @PostMapping("/login-challenge")
    public Object loginChallenge(
            @RequestBody Map<String, String> body
    ) {

        return service.generateLoginChallenge(
                body.get("email")
        );
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
    
    
    @PostMapping("/register")
    public String register(@RequestBody PasskeyRequest req) throws Exception {

    	if (req.credential == null) {
            return "Credential missing";
        }
    	
        var credential =
                parseRegisterCredential(req.credential);

        service.finishRegistration(
                credential,
                req.email
        );

        return "Registered Successfully";
    }
    
    
    @PostMapping("/login")
    public Object login(@RequestBody Map<String, Object> body) throws Exception {
        return service.login(body);
    }
    
    
}
