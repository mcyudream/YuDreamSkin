package online.yudream.yudreamskin.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Document("tb_skin")
public class Skin {

    private String id;
    @DocumentReference(lazy = true)
    private User user;

    private String fileName;
    private String name;
    private Integer status;
    private Integer like;
    private Map<String,String> metadata;
    private String skinType;
    @CreatedDate
    private LocalDateTime createdAt;

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
}
