package online.yudream.yudreamskin.mapper;

import online.yudream.yudreamskin.entity.Closet;
import online.yudream.yudreamskin.entity.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ClosetMapper extends MongoRepository<Closet, String> {
    Closet findClosetByUser(User user);

    Page<Closet> findClosetsByUser(@NotNull User user, PageRequest of);

}
