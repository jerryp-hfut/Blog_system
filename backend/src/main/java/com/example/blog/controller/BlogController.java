package com.example.blog.controller;

import com.example.blog.entity.Blog;
import com.example.blog.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestHeader;
import com.example.blog.repository.UserRepository;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@RestController
@RequestMapping("/api/blogs")
public class BlogController {
    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<Blog> getAllBlogs() {
        return blogRepository.findAll();
    }

    @GetMapping("/{id}")
    public Blog getBlogById(@PathVariable Integer id) {
        return blogRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Blog createBlog(@RequestBody Blog blog, @RequestHeader(value = "Authorization", required = false) String auth) {
        // 简单token校验，实际应解析JWT
        if (blog.getUser() == null || blog.getUser().getId() == null) {
            throw new RuntimeException("未登录或用户信息缺失");
        }
        // 校验用户是否存在
        var user = userRepository.findById(blog.getUser().getId()).orElseThrow(() -> new RuntimeException("用户不存在"));
        blog.setUser(user);
        return blogRepository.save(blog);
    }

    @PutMapping("/{id}")
    public Blog updateBlog(@PathVariable Integer id, @RequestBody Blog blog) {
        blog.setId(id);
        return blogRepository.save(blog);
    }

    @DeleteMapping("/{id}")
    public void deleteBlog(@PathVariable Integer id) {
        blogRepository.deleteById(id);
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<?> likeBlog(@PathVariable Integer id, @RequestHeader(value = "Authorization", required = false) String auth) {
        // 获取当前用户id（假设token中带userId，实际应解析JWT，这里用前端传userId）
        // 为简单起见，前端需在请求体中传userId
        // 生产环境请用更安全的方式
        // 这里只做演示
        return ResponseEntity.badRequest().body(Map.of("message", "请用POST /blogs/{id}/like，body中带userId"));
    }

    @PostMapping("/{id}/like-user")
    public ResponseEntity<?> likeBlogByUser(@PathVariable Integer id, @RequestBody Map<String, Object> req) {
        Object userIdObj = req.get("userId");
        Integer userId = null;
        if (userIdObj instanceof Integer) {
            userId = (Integer) userIdObj;
        } else if (userIdObj instanceof Number) {
            userId = ((Number) userIdObj).intValue();
        } else if (userIdObj instanceof String) {
            userId = Integer.valueOf((String) userIdObj);
        }
        if (userId == null) return ResponseEntity.badRequest().body(Map.of("message", "缺少userId"));
        String key = id + ":" + userId;
        if (LikeMemoryStore.hasLiked(key)) {
            return ResponseEntity.badRequest().body(Map.of("message", "您已点赞过该博客"));
        }
        Blog blog = blogRepository.findById(id).orElseThrow();
        blog.setLikes(blog.getLikes() == null ? 1 : blog.getLikes() + 1);
        blogRepository.save(blog);
        LikeMemoryStore.addLike(key);
        return ResponseEntity.ok(Map.of("likes", blog.getLikes()));
    }

    @PostMapping("/cover")
    public ResponseEntity<?> uploadCover(@RequestParam("file") MultipartFile file) {
        try {
            String folder = System.getProperty("user.dir") + "/public/cover/";
            Files.createDirectories(Paths.get(folder));
            String original = file.getOriginalFilename();
            String ext = (original != null && original.lastIndexOf('.') != -1) ? original.substring(original.lastIndexOf('.')) : ".jpg";
            String filename = "cover_" + System.currentTimeMillis() + ext;
            Path path = Paths.get(folder + filename);
            file.transferTo(path);
            return ResponseEntity.ok().body(Map.of("url", "/cover/" + filename));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "上传失败"));
        }
    }
}
