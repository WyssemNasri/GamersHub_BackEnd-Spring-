package com.example.gamershub.Respositroys;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.gamershub.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {


}
