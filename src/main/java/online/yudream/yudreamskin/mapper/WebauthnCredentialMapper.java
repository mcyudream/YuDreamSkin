package online.yudream.yudreamskin.mapper;



import online.yudream.yudreamskin.entity.WebauthnCredential;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface WebauthnCredentialMapper extends MongoRepository<WebauthnCredential, String> {

    List<WebauthnCredential> findAllByUserID(String userID);
}
