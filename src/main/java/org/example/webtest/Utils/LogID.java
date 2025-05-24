package org.example.webtest.Utils;

import java.util.concurrent.atomic.AtomicLong;

public class LogID {
    private static final AtomicLong sequence = new AtomicLong(0);
    private static long lastTimestamp = -1;

    /**
     * 生成唯一递增ID
     * 格式：时间戳(13位) + 序列号(4位)
     * 例如：16831234567890001
     * @return 17位的唯一递增ID
     */
    public static synchronized String generateUniqueId() {
        long currentTimestamp = System.currentTimeMillis();
        
        // 如果当前时间戳小于等于上次时间戳，则使用上次时间戳
        if (currentTimestamp <= lastTimestamp) {
            currentTimestamp = lastTimestamp;
        } else {
            lastTimestamp = currentTimestamp;
            // 重置序列号
            sequence.set(0);
        }
        
        // 获取并递增序列号
        long currentSequence = sequence.incrementAndGet();
        
        // 如果序列号超过9999，等待到下一秒
        if (currentSequence > 9999) {
            try {
                Thread.sleep(1);
                return generateUniqueId();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Failed to generate unique ID", e);
            }
        }
        
        // 组合时间戳和序列号
        return String.format("%d%04d", currentTimestamp, currentSequence);
    }
}
