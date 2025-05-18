package org.example.webtest.Utils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class FileCache {
    private static final ConcurrentHashMap<String, CacheEntry> cache = new ConcurrentHashMap<>();
    private static final long EXPIRATION_TIME = TimeUnit.DAYS.toMillis(1); // 1 day in milliseconds

    private static class CacheEntry {
        private final String value;
        private final long expirationTime;

        public CacheEntry(String value) {
            this.value = value;
            this.expirationTime = System.currentTimeMillis() + EXPIRATION_TIME;
        }

        public boolean isExpired() {
            return System.currentTimeMillis() > expirationTime;
        }

        public String getValue() {
            return value;
        }
    }

    public static void put(String fileName, String value) {
        cache.put(fileName, new CacheEntry(value));
    }

    public static String get(String fileName) {
        CacheEntry entry = cache.get(fileName);
        if (entry == null || entry.isExpired()) {
            cache.remove(fileName);
            return null;
        }
        return entry.getValue();
    }

    public static void remove(String fileName) {
        cache.remove(fileName);
    }

    public static boolean contains(String fileName) {
        CacheEntry entry = cache.get(fileName);
        if (entry == null || entry.isExpired()) {
            cache.remove(fileName);
            return false;
        }
        return true;
    }

    // Clean up expired entries
    public static void cleanup() {
        cache.entrySet().removeIf(entry -> entry.getValue().isExpired());
    }
} 