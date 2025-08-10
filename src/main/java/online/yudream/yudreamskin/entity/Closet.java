package online.yudream.yudreamskin.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.MongoId;
import java.io.Serializable;

@Document("tb_closet")
public class Closet implements Serializable {
    @MongoId
    private String id;
    @DocumentReference(lazy = true)
    private User user;
    @DocumentReference(lazy = true)
    private Skin skin;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Skin getSkin() {
        return skin;
    }

    public void setSkin(Skin skin) {
        this.skin = skin;
    }
}