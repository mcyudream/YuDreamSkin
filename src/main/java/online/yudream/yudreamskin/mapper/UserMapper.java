package online.yudream.yudreamskin.mapper;

import online.yudream.yudreamskin.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserMapper extends MongoRepository<User, String> {
    Optional<Object> findByIdOrUsernameOrEmail(String id, String username, String email);

    User findUserByUsernameOrEmailOrQq(String username, String email, String qq);

    User findUserByUsername(String username);
}
