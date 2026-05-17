package com.example.passkey;

import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component
public class ChallengeStore {

    private final Map<String, byte[]> store = new HashMap<>();

    public void save(String email, byte[] challenge) {
        store.put(email, challenge);
    }

    public byte[] get(String email) {
        return store.get(email);
    }
}
