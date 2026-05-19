package com.example.passkey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Credential {

    private String id;
    private String rawId;
    private String type;   // 🔥 ADD THIS
    private Response response;
}