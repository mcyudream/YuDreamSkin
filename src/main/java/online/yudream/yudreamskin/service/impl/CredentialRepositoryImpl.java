package online.yudream.yudreamskin.service.impl;

import com.yubico.webauthn.CredentialRepository;
import com.yubico.webauthn.RegisteredCredential;
import com.yubico.webauthn.data.ByteArray;
import com.yubico.webauthn.data.PublicKeyCredentialDescriptor;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import online.yudream.yudreamskin.entity.CredentialRegistration;
import online.yudream.yudreamskin.entity.User;
import online.yudream.yudreamskin.entity.WebauthnCredential;
import online.yudream.yudreamskin.mapper.UserMapper;
import online.yudream.yudreamskin.mapper.WebauthnCredentialMapper;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class CredentialRepositoryImpl implements CredentialRepository {
    @Resource
    private WebauthnCredentialMapper webauthnCredentialRepository;
    @Resource
    private UserMapper userMapper;

    @Override
    public Set<PublicKeyCredentialDescriptor> getCredentialIdsForUsername(String username) {
        return webauthnCredentialRepository.findAllByUserID(getUserIDByEmail(username)).stream()
                .map(WebauthnCredential::getCredentialRegistration)
                .map(it -> PublicKeyCredentialDescriptor.builder()
                        .id(it.getCredential().getCredentialId())
                        .transports(it.getTransports())
                        .build())
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Optional<String> getUsernameForUserHandle(ByteArray userHandle) {
        return getRegistrationsByUserHandle(userHandle).stream()
                .findAny()
                .map(CredentialRegistration::getUsername);
    }

    @Override
    public Optional<ByteArray> getUserHandleForUsername(String username) {
        return getRegistrationsByUsername(username).stream()
                .findAny()
                .map(reg -> reg.getUserIdentity().getId());
    }

    @Override
    public Optional<RegisteredCredential> lookup(ByteArray credentialId, ByteArray userHandle) {
        Optional<CredentialRegistration> registrationMaybe = webauthnCredentialRepository.findAll().stream()
                .map(WebauthnCredential::getCredentialRegistration)
                .filter(it -> it.getCredential().getCredentialId().equals(credentialId))
                .findAny();

        return registrationMaybe.map(it ->
                RegisteredCredential.builder()
                        .credentialId(it.getCredential().getCredentialId())
                        .userHandle(it.getCredential().getUserHandle())
                        .publicKeyCose(it.getCredential().getPublicKeyCose())
                        .signatureCount(it.getCredential().getSignatureCount())
                        .build());
    }

    @Override
    public Set<RegisteredCredential> lookupAll(ByteArray credentialId) {
        return webauthnCredentialRepository.findAll().stream()
                .map(WebauthnCredential::getCredentialRegistration)
                .filter(it -> it.getCredential().getCredentialId().equals(credentialId))
                .map(it ->
                        RegisteredCredential.builder()
                                .credentialId(it.getCredential().getCredentialId())
                                .userHandle(it.getCredential().getUserHandle())
                                .publicKeyCose(it.getCredential().getPublicKeyCose())
                                .signatureCount(it.getCredential().getSignatureCount())
                                .build())
                .collect(Collectors.toUnmodifiableSet());
    }

    private String getUserIDByEmail(String email) {
        User user = userMapper.findUserByEmail(email);
        return user.getId();
    }

    private Collection<CredentialRegistration> getRegistrationsByUsername(String username) {
        return webauthnCredentialRepository.findAllByUserID(getUserIDByEmail(username)).stream()
                .map(WebauthnCredential::getCredentialRegistration)
                .toList();
    }

    private Collection<CredentialRegistration> getRegistrationsByUserHandle(ByteArray userHandle) {
        return webauthnCredentialRepository.findAll().stream()
                .map(WebauthnCredential::getCredentialRegistration)
                .filter(it -> it.getUserIdentity().getId().equals(userHandle))
                .toList();
    }
}