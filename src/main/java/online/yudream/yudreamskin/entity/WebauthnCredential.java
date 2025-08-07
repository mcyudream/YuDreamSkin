package online.yudream.yudreamskin.entity;

import com.yubico.webauthn.RegisteredCredential;
import com.yubico.webauthn.RegistrationResult;
import com.yubico.webauthn.data.PublicKeyCredentialCreationOptions;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Clock;
import java.util.Date;
import java.util.TreeSet;

@Data
@Document("tb_webauthn_credential")
public class WebauthnCredential {
    @MongoId
    private String id;
    private String userID;
    @CreatedDate
    private Date createTime;
    private CredentialRegistration credentialRegistration;

    public static WebauthnCredential from(String userID, PublicKeyCredentialCreationOptions request, RegistrationResult result) {
        WebauthnCredential webauthnCredential = new WebauthnCredential();
        webauthnCredential.setUserID(userID);
        webauthnCredential.setCreateTime(new Date());
        webauthnCredential.setCredentialRegistration(
                CredentialRegistration.builder()
                .userIdentity(request.getUser())
                .transports(result.getKeyId().getTransports().orElse(new TreeSet<>()))
                .registration(Clock.systemUTC().instant())
                .credential(RegisteredCredential.builder()
                        .credentialId(result.getKeyId().getId())
                        .userHandle(request.getUser().getId())
                        .publicKeyCose(result.getPublicKeyCose())
                        .signatureCount(result.getSignatureCount())
                        .build())
                .build());
        return webauthnCredential;
    }
}