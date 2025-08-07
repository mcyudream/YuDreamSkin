package online.yudream.yudreamskin.config;

import com.yubico.webauthn.CredentialRepository;
import com.yubico.webauthn.RelyingParty;
import com.yubico.webauthn.data.RelyingPartyIdentity;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class WebAuthnConfig {
    @Value("${passkey.rpId}")
    private String rpId;
    @Value("${passkey.rpName}")
    private String rpName;
    @Value("${passkey.origins}")
    private String origins;
    @Resource
    private CredentialRepository credentialRepository;

    @Bean
    public RelyingParty relyingParty() {
        var rpIdentity = RelyingPartyIdentity.builder()
                .id(rpId)
                .name(rpName)
                .build();

        return RelyingParty.builder()
                .identity(rpIdentity)
                .credentialRepository(credentialRepository)
                .origins(Collections.singleton(origins))
                .build();
    }
}