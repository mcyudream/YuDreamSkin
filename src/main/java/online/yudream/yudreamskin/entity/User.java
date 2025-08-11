package online.yudream.yudreamskin.entity;

import com.fasterxml.jackson.databind.ser.std.SerializableSerializer;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Document("tb_user")
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @MongoId
    private String id;
    private String username;
    private String password;
    private String email;
    private String qq;
    private String avatar;
    private String nickname;
    private String bindProfile;
    @DocumentReference(lazy = true)   // 只存 id，查询时自动 join
    private List<Role> roles;
    private Map<String, GameProfile>  profiles; // uuid
    private List<IpEntity> loginIps =  new ArrayList<>();
    @CreatedDate
    private LocalDateTime createTime;
    @LastModifiedDate
    private LocalDateTime updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getBindProfile() {
        return bindProfile;
    }

    public void setBindProfile(String bindProfile) {
        this.bindProfile = bindProfile;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public Map<String, GameProfile> getProfiles() {
        return profiles;
    }

    public void setProfiles(Map<String, GameProfile> profiles) {
        this.profiles = profiles;
    }

    public List<IpEntity> getLoginIps() {
        return loginIps;
    }

    public void setLoginIps(List<IpEntity> loginIps) {
        this.loginIps = loginIps;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}