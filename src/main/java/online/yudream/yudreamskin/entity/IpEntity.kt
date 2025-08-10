package online.yudream.yudreamskin.entity

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.ser.Serializers
import org.springframework.context.annotation.Configuration
import java.io.Serializable
import java.time.LocalDateTime


data class IpEntity (val ip: String, @field:JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                val time: LocalDateTime = LocalDateTime.now(),
    ): Serializable