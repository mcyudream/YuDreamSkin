package online.yudream.yudreamskin.entity

import com.fasterxml.jackson.databind.ser.Serializers
import org.springframework.context.annotation.Configuration
import java.io.Serializable
import java.time.LocalDateTime


data class IpEntity (val ip: String,
                val time: LocalDateTime = LocalDateTime.now(),
    ): Serializable