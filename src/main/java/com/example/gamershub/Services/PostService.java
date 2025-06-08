package com.example.gamershub.Services;
import com.example.gamershub.Respositroys.PostRepository;
import com.example.gamershub.Respositroys.UserRepository;
import com.example.gamershub.dto.PostRequest;
import com.example.gamershub.entity.Post;
import com.example.gamershub.entity.User;
import org.springframework.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private  PostRepository postRepository;
    @Autowired
    private  UserRepository userRepository;
    

    @Value("${file.upload-dir}")
    private String uploadDir;

    public List<Post> fetchUserPosts(Long userId) {
        return postRepository.findByUserId(userId);
    }
    private final RestTemplate restTemplate = new RestTemplate();
    private boolean isContentSafe(String description, MultipartFile file) throws IOException {
        boolean isTextSafe = true;
        boolean isImageSafe = true;
    
        // Analyse du texte uniquement s'il est non nul et non vide
        if (description != null && !description.trim().isEmpty()) {
            HttpHeaders textHeaders = new HttpHeaders();
            textHeaders.setContentType(MediaType.APPLICATION_JSON);
            String textJson = "{\"message\": \"" + description.replace("\"", "\\\"") + "\"}";
            HttpEntity<String> textRequest = new HttpEntity<>(textJson, textHeaders);
    
            ResponseEntity<Map> textResponse = restTemplate.postForEntity("http://localhost:5001/predict/text", textRequest, Map.class);
            String textPrediction = (String) textResponse.getBody().get("prediction");
    
            isTextSafe = !textPrediction.equalsIgnoreCase("bad");
        }
    
        // Analyse de l'image uniquement si le fichier est présent
        if (file != null && !file.isEmpty()) {
            HttpHeaders imageHeaders = new HttpHeaders();
            imageHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
    
            MultiValueMap<String, Object> imageData = new LinkedMultiValueMap<>();
            ByteArrayResource imageResource = new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            };
            imageData.add("image", imageResource);
            HttpEntity<MultiValueMap<String, Object>> imageRequest = new HttpEntity<>(imageData, imageHeaders);
    
            ResponseEntity<Map> imageResponse = restTemplate.postForEntity("http://localhost:5001/predict/image", imageRequest, Map.class);
            String imagePrediction = (String) imageResponse.getBody().get("predicted_class");
    
            isImageSafe = !List.of("porn", "hentai", "sexy").contains(imagePrediction);
        }
    
        return isTextSafe && isImageSafe;
    }
    
    public Post createPost(PostRequest postRequest, MultipartFile file) throws IOException {
        User user = userRepository.findById(postRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        String urlMedia = null;

        if (file != null && !file.isEmpty()) {
            if (!isContentSafe(postRequest.getDescription(), file)) {
                throw new RuntimeException("Contenu inapproprié détecté");
            }

            File uploadDirectory = new File(uploadDir);
            if (!uploadDirectory.exists()) uploadDirectory.mkdirs();

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


public List<Post> fetchVideoPosts() {
    List<Post> allPosts = postRepository.findAll();
    return allPosts.stream()
            .filter(post -> post.getUrlMedia() != null && 
                    (post.getUrlMedia().endsWith(".mp4") || post.getUrlMedia().endsWith(".avi") || post.getUrlMedia().endsWith(".mov")))
            .collect(Collectors.toList());
}


}
