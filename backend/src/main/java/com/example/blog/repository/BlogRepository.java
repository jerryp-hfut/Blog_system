package com.example.blog.repository;

import com.example.blog.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Integer> {
    // 统计每个分类下的博客数量
    @Query("SELECT b.category.name, COUNT(b.id) FROM Blog b GROUP BY b.category.name")
    List<Object[]> countBlogsByCategory();
}
