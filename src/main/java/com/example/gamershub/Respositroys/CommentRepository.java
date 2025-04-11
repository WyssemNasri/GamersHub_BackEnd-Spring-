package com.example.gamershub.Respositroys;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.gamershub.entity.Comment;
import com.example.gamershub.entity.Post;
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(Long postId);
    List<Comment> findByPost(Post post);
    int countByPostId(Long postId);
}



