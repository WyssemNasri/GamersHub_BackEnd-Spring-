package com.example.gamershub.Services;

import com.example.gamershub.Respositroys.PostRepository;
import com.example.gamershub.Respositroys.UserRepository;
import com.example.gamershub.dto.FriendDTO;
import com.example.gamershub.dto.PostRequest;
import com.example.gamershub.entity.Post;
import com.example.gamershub.entity.User;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {


    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final FriendshipService friendshipService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public PostService(PostRepository postRepository, UserRepository userRepository, FriendshipService friendshipService) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.friendshipService = friendshipService;
    }

    public List<Post> fetchUserPosts(Long userId) {
        return postRepository.findByUserId(userId);
    }

    public Post createPost(PostRequest postRequest, MultipartFile file) throws IOException {
        User user = userRepository.findById(postRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        File uploadDirectory = new File(uploadDir);
        if (!uploadDirectory.exists()) {
            uploadDirectory.mkdirs();
        }

        String urlMedia = null;
        if (file != null && !file.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            urlMedia = fileName;

        }

        Post post = new Post();
        post.setUser(user);
        post.setDescription(postRequest.getDescription());
        post.setUrlMedia(urlMedia);

        return postRepository.save(post);
    }
}
