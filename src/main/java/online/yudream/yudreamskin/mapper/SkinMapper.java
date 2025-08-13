package online.yudream.yudreamskin.mapper;

import online.yudream.yudreamskin.entity.Skin;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SkinMapper extends MongoRepository<Skin, String> {
    Skin findSkinByMigratedId(Integer migratedId);

    List<Skin> findSkinsByHash(String hash);
}
