package com.example.blog.controller;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class LikeMemoryStore {
    private static final Set<String> likeSet = ConcurrentHashMap.newKeySet();

    public static boolean hasLiked(String key) {
        return likeSet.contains(key);
    }

    public static void addLike(String key) {
        likeSet.add(key);
    }
}
