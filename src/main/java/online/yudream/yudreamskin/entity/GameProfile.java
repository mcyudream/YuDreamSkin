package online.yudream.yudreamskin.entity;

import lombok.Data;

import java.util.Date;

@Data
public class GameProfile {
    private String uuid;
    private String name;
    private String skin;
    private Date lastJoin;
    private Date createTime;
}
