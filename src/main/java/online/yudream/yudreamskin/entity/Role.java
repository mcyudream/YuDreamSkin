package online.yudream.yudreamskin.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.io.Serializable;
import java.util.List;

@Document("tb_role")
public class Role implements Serializable {
    @MongoId
    private String id;
    private String description;
    private String displayName;
    private String level;
    @DocumentReference(lazy = true)
    private List<Permission> permission;

    public Role() {

    }
    public Role(String id, String description, String displayName, String level) {
        this.id = id;
        this.description = description;
        this.displayName = displayName;
        this.level = level;
    }
    public Role(String id, String description, String displayName) {
        this.id = id;
        this.description = description;
        this.displayName = displayName;
    }


    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<Permission> getPermission() {
        return permission;
    }

    public void setPermission(List<Permission> permission) {
        this.permission = permission;
    }
}
