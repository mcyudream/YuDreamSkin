package online.yudream.yudreamskin.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDateTime;
import java.util.Map;

@AllArgsConstructor
@Builder
@Document("tb_skin")
public class Skin {

    private String id;
    @DocumentReference(lazy = true)
    private User user;

    private String fileName;
    private String name;
    private Integer status;
    private String hash;
    private Integer like;
    private Map<String,String> metadata;
    private String skinType;
    @CreatedDate
    private LocalDateTime createdAt;
    private Integer migratedId; //默认无

    public Skin(){

    }
    public Skin(String name, String file,Map<String,String> metadata,Integer status, Integer like,String type){
        this.name = name;
        this.fileName = file;
        this.metadata = metadata;
        this.status = status;
        this.like = like;
        this.skinType = type;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Integer getMigratedId() {
        return migratedId;
    }

    public void setMigratedId(Integer migratedId) {
        this.migratedId = migratedId;
    }

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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getLike() {
        return like;
    }

    public void setLike(Integer like) {
        this.like = like;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    public String getSkinType() {
        return skinType;
    }

    public void setSkinType(String skinType) {
        this.skinType = skinType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
