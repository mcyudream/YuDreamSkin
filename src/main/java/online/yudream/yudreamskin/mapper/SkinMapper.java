package online.yudream.yudreamskin.mapper;

import online.yudream.yudreamskin.entity.Skin;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SkinMapper extends MongoRepository<Skin, String> {
    Skin findSkinByMigratedId(Integer migratedId);
}
