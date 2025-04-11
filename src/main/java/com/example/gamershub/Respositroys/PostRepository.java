package com.example.gamershub.Respositroys;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.gamershub.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByUserId(Long userId); 
    List<Post> findByUserIdIn(List<Long> userIds);


}
