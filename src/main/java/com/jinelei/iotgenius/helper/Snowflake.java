package com.jinelei.iotgenius.helper;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author: jinelei
 * @Description: 雪花算法，最大53位
 * @Date: 2023/3/27
 * @Version: 1.0.0
 */
@SuppressWarnings("unused")
public interface Snowflake {
    Snowflake DEFAULT = new Snowflake() {
    };
    Long START_TIMESTAMP = LocalDateTime.of(2024, 1, 1, 0, 0, 0).toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
    AtomicLong timestamp = new AtomicLong(0L);
    AtomicLong sequence = new AtomicLong(0L);

    default int getWorkId() {
        return 1;
    }

    default int getWorkIdBits() {
        return 6;
    }

    default int getSequenceBits() {
        return 6;
    }

    default int getTimestampBits() {
        return 41;
    }

    default long next() {
        long now = Instant.now().toEpochMilli();
        // 如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
        if (timestamp.get() > now) {
            throw new RuntimeException("Clock moved backwards.  Refusing to generate id for %d milliseconds");
        }
        if (timestamp.get() == now) {
            long seq = sequence.incrementAndGet();
            if ((seq & (-1L << getSequenceBits())) <= 0) {
                while (Instant.now().toEpochMilli() == timestamp.get()) {
                    timestamp.set(Instant.now().toEpochMilli());
                }
                sequence.set(0);
            }
        } else {
            timestamp.set(now);
            sequence.set(0);
        }
        return (((getWorkId() & ~(-1L << getWorkIdBits())) << (getTimestampBits() + getSequenceBits()))
                | (((timestamp.get() - START_TIMESTAMP) & ~(-1L << getTimestampBits())) << (getSequenceBits()))
                | (sequence.get() & ~(-1L << getSequenceBits())));
    }
}