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
    public String clientDataJSON;
    public String attestationObject; // register
    public String authenticatorData; // login
    public String signature;
    public String userHandle;
}