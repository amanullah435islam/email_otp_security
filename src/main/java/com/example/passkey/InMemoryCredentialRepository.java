package com.example.passkey;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.yubico.webauthn.CredentialRepository;
import com.yubico.webauthn.RegisteredCredential;
import com.yubico.webauthn.data.ByteArray;
import com.yubico.webauthn.data.PublicKeyCredentialDescriptor;

import lombok.RequiredArgsConstructor;

import java.nio.charset.StandardCharsets;


@Repository
@RequiredArgsConstructor
public class InMemoryCredentialRepository implements CredentialRepository {

	   private final PasskeyRepository repo;


    // 🔹 USER → credential IDs
    @Override
    public Set<PublicKeyCredentialDescriptor> getCredentialIdsForUsername(String username) {

        return repo.findByUserEmail(username)
                .stream()
                .map(c -> PublicKeyCredentialDescriptor.builder()
                        //.id(new ByteArray(c.getCredentialId()))
                        .id(new ByteArray(
                                Base64.getDecoder().decode(c.getCredentialIdBase64())
                        ))
                        .build())
                .collect(Collectors.toSet());
    }

    // 🔹 username → userHandle
    @Override
    public Optional<ByteArray> getUserHandleForUsername(String username) {

        return Optional.of(
                new ByteArray(username.getBytes(StandardCharsets.UTF_8))
        );
    }

    // 🔹 userHandle → username
    @Override
    public Optional<String> getUsernameForUserHandle(ByteArray userHandle) {

        return Optional.of(
                new String(userHandle.getBytes(), StandardCharsets.UTF_8)
        );
    }

    // 🔹 find single credential
    @Override
    public Optional<RegisteredCredential> lookup(
            ByteArray credentialId,
            ByteArray userHandle
    ) {

        String email = new String(userHandle.getBytes(), StandardCharsets.UTF_8);

        return repo.findByUserEmail(email)
                .stream()
//                .filter(c ->
//                        Arrays.equals(c.getCredentialId(), credentialId.getBytes())
//                )
                .filter(c -> Arrays.equals(
                        Base64.getDecoder().decode(c.getCredentialIdBase64()),
                        credentialId.getBytes()
                ))
                .findFirst()
                .map(c -> RegisteredCredential.builder()
                        //.credentialId(new ByteArray(c.getCredentialId()))
                        .credentialId(new ByteArray(
                                        Base64.getDecoder().decode(c.getCredentialIdBase64())
                                ))
                        .userHandle(new ByteArray(email.getBytes(StandardCharsets.UTF_8)))
                        //.publicKeyCose(new ByteArray(c.getPublicKey()))
                        .publicKeyCose(new ByteArray(
                                Base64.getDecoder().decode(c.getPublicKey())
                        ))
                        .signatureCount(c.getSignatureCount())
                        .build());
    }

    // 🔹 find all by credentialId
    @Override
    public Set<RegisteredCredential> lookupAll(ByteArray credentialId) {

        return repo.findAll()
                .stream()
//                .filter(c ->
//                        Arrays.equals(c.getCredentialId(), credentialId.getBytes())
//                )
                .filter(c -> Arrays.equals(
                        Base64.getDecoder().decode(c.getCredentialIdBase64()),
                        credentialId.getBytes()
                ))
                .map(c -> RegisteredCredential.builder()
                        //.credentialId(new ByteArray(c.getCredentialId()))
                        .credentialId(new ByteArray(
                                Base64.getDecoder().decode(c.getCredentialIdBase64())
                        ))
                        .userHandle(new ByteArray(c.getUser().getEmail().getBytes(StandardCharsets.UTF_8)))
                        //.publicKeyCose(new ByteArray(c.getPublicKey()))
                        .publicKeyCose(new ByteArray(
                                Base64.getDecoder().decode(c.getPublicKey())
                        ))
                        .signatureCount(c.getSignatureCount())
                        .build())
                .collect(Collectors.toSet());
    }
}














// //old code:

//@Repository
//public class InMemoryCredentialRepository
//        implements CredentialRepository {
//
//    @Autowired
//    private PasskeyRepository repo;
//
//    // USER → credential IDs
//    @Override
//    public Set<PublicKeyCredentialDescriptor>
//    getCredentialIdsForUsername(String username) {
//
//        return repo.findByEmail(username)
//                .stream()
//                .map(c -> PublicKeyCredentialDescriptor.builder()
//                        .id(new ByteArray(c.getCredentialId()))
//                        .build())
//                .collect(Collectors.toSet());
//    }
//
//    // username → userHandle
//    @Override
//    public Optional<ByteArray>
//    getUserHandleForUsername(String username) {
//
//        return Optional.of(
//                new ByteArray(username.getBytes())
//        );
//    }
//
//    // userHandle → username
//    @Override
//    public Optional<String>
//    getUsernameForUserHandle(ByteArray userHandle) {
//
//        return Optional.of(
//                new String(userHandle.getBytes())
//        );
//    }
//
//    // find single credential
//    @Override
//    public Optional<RegisteredCredential>
//    lookup(
//            ByteArray credentialId,
//            ByteArray userHandle
//    ) {
//
//        return repo.findByEmail(
//                new String(userHandle.getBytes())
//        ).stream()
//                .filter(c ->
//                        Arrays.equals(
//                                c.getCredentialId(),
//                                credentialId.getBytes()
//                        )
//                )
//                .findFirst()
//                .map(c -> RegisteredCredential.builder()
//                        .credentialId(
//                                new ByteArray(c.getCredentialId())
//                        )
//                        .userHandle(userHandle)
//                        .publicKeyCose(
//                                new ByteArray(c.getPublicKey())
//                        )
//                        .signatureCount(
//                                c.getSignatureCount()
//                        )
//                        .build());
//    }
//
//    // find all by credentialId
//    @Override
//    public Set<RegisteredCredential>
//    lookupAll(ByteArray credentialId) {
//
//        return repo.findAll()
//                .stream()
//                .filter(c ->
//                        Arrays.equals(
//                                c.getCredentialId(),
//                                credentialId.getBytes()
//                        )
//                )
//                .map(c -> RegisteredCredential.builder()
//                        .credentialId(
//                                new ByteArray(c.getCredentialId())
//                        )
//                        .userHandle(
//                                new ByteArray(
//                                        c.getEmail().getBytes()
//                                )
//                        )
//                        .publicKeyCose(
//                                new ByteArray(c.getPublicKey())
//                        )
//                        .signatureCount(
//                                c.getSignatureCount()
//                        )
//                        .build())
//                .collect(Collectors.toSet());
//    }
//}