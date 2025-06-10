package com.example.blog.controller;

import com.example.blog.entity.Tag;
import com.example.blog.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {
    @Autowired
    private TagRepository tagRepository;

    @GetMapping
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    @PostMapping
    public Tag createTag(@RequestBody Tag tag) {
        return tagRepository.save(tag);
    }

    @PutMapping("/{id}")
    public Tag updateTag(@PathVariable Integer id, @RequestBody Tag tag) {
        tag.setId(id);
        return tagRepository.save(tag);
    }

    @DeleteMapping("/{id}")
    public void deleteTag(@PathVariable Integer id) {
        tagRepository.deleteById(id);
    }
}
