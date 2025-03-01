package com.jinelei.iotgenius.helper;

import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * @Author: jinelei
 * @Description:
 * @Date: 2023/07/25
 * @Version: 1.0.0
 */
@SuppressWarnings("unused")
public class InstantHelper {
    public static Instant parseInstant(String time) {
        if (time == null || !StringUtils.hasLength(time.trim())) {
            return null;
        } else if (time.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\+08:00")) {
            return Instant.parse(time.trim());
        } else if (time.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d{3}\\+08:00")) {
            return Instant.parse(time.trim());
        } else if (time.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")) {
            return LocalDateTime.parse(time, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toInstant(ZoneOffset.ofHours(8));
        } else if (time.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3}")) {
            return LocalDateTime.parse(time, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")).toInstant(ZoneOffset.ofHours(8));
        } else {
            return null;
        }
    }

    public static Instant parseInstantWithDefault(String time) {
        if (!StringUtils.hasLength(time) || time.trim().isEmpty()) {
            return Instant.now();
        }
        if (time.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\+08:00")) {
            return Instant.parse(time);
        }
        if (time.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d{3}\\+08:00")) {
            return Instant.parse(time);
        }
        return Instant.now();
    }
}
