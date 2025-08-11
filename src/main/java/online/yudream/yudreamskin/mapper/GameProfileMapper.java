package online.yudream.yudreamskin.mapper;

import online.yudream.yudreamskin.entity.GameProfile;
import online.yudream.yudreamskin.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GameProfileMapper extends MongoRepository<GameProfile, String> {

    List<GameProfile> findGameProfileByUser(User user);

    List<GameProfile> findGameProfileByUserAndNameLike(User user, String name);
}
