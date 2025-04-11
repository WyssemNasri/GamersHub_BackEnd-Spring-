package com.example.gamershub.Respositroys;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.gamershub.entity.Like;
import com.example.gamershub.entity.Post;
import com.example.gamershub.entity.User;


public interface LikeRepository extends JpaRepository<Like, Long> {

    Boolean existsByUserIdAndPostId(Long userId, Long postId);
    List<Like> findByPostId(Long postId);
    int countByPostId(Long postId);
    Like findByUserAndPost (User user , Post post); ; 




}
