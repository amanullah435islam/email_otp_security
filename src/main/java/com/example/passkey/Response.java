package com.example.passkey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response {

	    private String clientDataJSON;
	    private String attestationObject;
	    private String authenticatorData;
	    private String signature;
	    private String userHandle;
	    
	    
	}   
