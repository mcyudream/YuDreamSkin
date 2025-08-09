package online.yudream.yudreamskin.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URLEncoder;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class R <T>{
    public int code;
    public String msg;
    public T data;
    public boolean success;

    public static <T>R<T> ok(T data) {
        return new R<T>(200,"success",data,true);
    }

    public static <T>R<T> fail(String msg) {
        return new R<T>(500, URLEncoder.encode(msg),null,false);
    }

    public static <T>R<T> fail(int code,String msg) {
        return new R<T>(code,URLEncoder.encode(msg),null,false);
    }
}
