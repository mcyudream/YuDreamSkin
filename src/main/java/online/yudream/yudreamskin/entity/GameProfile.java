package online.yudream.yudreamskin.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;
import java.util.Date;


@Document("tb_game_profile")
@ToString
public class GameProfile {
    @MongoId
    private String uuid;
    @DocumentReference(lazy = true)
    private User user;
    private String name;
    @DocumentReference(lazy = true)
    private Skin skin;
    @DocumentReference(lazy = true)
    private Skin cape;
    private LocalDateTime lastJoin;
    @CreatedDate
    private LocalDateTime createTime;

    public Skin getCape() {
        return cape;
    }

    public void setCape(Skin cape) {
        this.cape = cape;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Skin getSkin() {
        return skin;
    }

    public void setSkin(Skin skin) {
        this.skin = skin;
    }

    public LocalDateTime getLastJoin() {
        return lastJoin;
    }

    public void setLastJoin(LocalDateTime lastJoin) {
        this.lastJoin = lastJoin;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}
