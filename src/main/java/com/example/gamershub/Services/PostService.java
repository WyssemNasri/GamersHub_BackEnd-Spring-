package com.example.gamershub.Services;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.gamershub.Respositroys.PostRepository;
import com.example.gamershub.Respositroys.UserRepository;
import com.example.gamershub.dto.PostRequest;
import com.example.gamershub.entity.Post;
import com.example.gamershub.entity.User;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public Post createPost(PostRequest postRequest, MultipartFile file) throws IOException {
        // Vérifier si l'utilisateur existe
        User user = userRepository.findById(postRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        // Vérifier et créer le dossier d'upload s'il n'existe pas
        File uploadDirectory = new File(uploadDir);
        if (!uploadDirectory.exists()) {
            uploadDirectory.mkdirs();
        }

        // Sauvegarde du fichier dans le dossier
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, fileName);

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Création et sauvegarde du post
        Post post = new Post();
        post.setUser(user);
        post.setDescription(postRequest.getDescription());
        post.setUrlMedia(filePath.toString());

        return postRepository.save(post);
    }
}
