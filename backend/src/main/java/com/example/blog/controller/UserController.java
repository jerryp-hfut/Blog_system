package com.example.blog.controller;

import com.example.blog.entity.User;
import com.example.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Integer id, @RequestBody User user) {
        user.setId(id);
        return userRepository.save(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Integer id) {
        userRepository.deleteById(id);
    }

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        // 简单校验，实际应加密码加密
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new RuntimeException("用户名已存在");
        }
        // 新注册用户默认普通用户
        user.setRole("user");
        return userRepository.save(user);
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> req) {
        String username = req.get("username");
        String password = req.get("password");
        User user = userRepository.findByUsername(username);
        if (user == null || !user.getPassword().equals(password)) {
            throw new RuntimeException("用户名或密码错误");
        }
        // 指定“潘序”为管理员
        if ("潘序".equals(user.getUsername())) {
            user.setRole("admin");
            userRepository.save(user);
        }
        // 生成简单token（实际应用JWT）
        String token = UUID.randomUUID().toString();
        Map<String, Object> res = new HashMap<>();
        res.put("token", token);
        res.put("user", user);
        return res;
    }

    @PostMapping("/avatar")
    public ResponseEntity<?> uploadAvatar(@RequestParam("file") MultipartFile file, @RequestParam("userId") Integer userId) {
        try {
            String folder = System.getProperty("user.dir") + "/public/avatar/";
            Files.createDirectories(Paths.get(folder));
            String original = file.getOriginalFilename();
            String ext = (original != null && original.lastIndexOf('.') != -1) ? original.substring(original.lastIndexOf('.')) : ".jpg";
            String filename = "user_" + userId + "_" + System.currentTimeMillis() + ext;
            Path path = Paths.get(folder + filename);
            file.transferTo(path);
            // 保存头像URL到用户
            User user = userRepository.findById(userId).orElseThrow();
            user.setAvatar("/avatar/" + filename);
            userRepository.save(user);
            return ResponseEntity.ok().body(Map.of("url", "/avatar/" + filename));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "上传失败"));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> req) {
        Integer userId = Integer.valueOf(req.get("userId"));
        String oldPassword = req.get("oldPassword");
        String newPassword = req.get("newPassword");
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "用户不存在"));
        }
        if (!user.getPassword().equals(oldPassword)) {
            return ResponseEntity.badRequest().body(Map.of("message", "原密码错误"));
        }
        user.setPassword(newPassword);
        userRepository.save(user);
        return ResponseEntity.ok(Map.of("message", "密码重置成功"));
    }
}
