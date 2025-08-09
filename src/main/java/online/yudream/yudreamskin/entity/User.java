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
@Data
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


}