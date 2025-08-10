package online.yudream.yudreamskin.mapper;

import online.yudream.yudreamskin.entity.GameProfile;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GameProfileMapper extends MongoRepository<GameProfile, String> {

}
