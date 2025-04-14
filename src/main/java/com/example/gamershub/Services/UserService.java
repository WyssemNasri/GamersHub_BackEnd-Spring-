package com.example.gamershub.Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.gamershub.Respositroys.UserRepository;
import com.example.gamershub.dto.UserDto;
import com.example.gamershub.dto.UserResponse;
import com.example.gamershub.entity.User;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
@Service
public class UserService {

    @Value("${file.profile-pic-dir}")
    private String profilePicDir;

    @Value("${file.cover-pic-dir}")
    private String coverPicDir;
    
    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserDto adduser(UserDto userdto) {
        if (userRepository.existsByEmail(userdto.getEmail())) {
            throw new RuntimeException("Email already in use");
        } else {
           
            String hashedPassword = passwordEncoder.encode(userdto.getPassword());
            userdto.setPassword(hashedPassword);
            

            User user = userdto.toUser();
            user.setCreatedAt(LocalDateTime.now());


            User savedUser = userRepository.save(user);

            return new UserDto(null, null, 
                               savedUser.getFirstName(),savedUser.getLastName(), 
                               null, null);
        }
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public String saveProfilePicture(MultipartFile profilePic, Long userId) throws IOException {
        String profilePicName = saveFile(profilePic, profilePicDir); // Récupère uniquement le nom du fichier
        updateProfilePicUrl(userId, profilePicName);
        return profilePicName; // Retourne uniquement le nom du fichier
    }
    
    public String saveCoverPicture(MultipartFile coverPic, Long userId) throws IOException {
        String coverPicName = saveFile(coverPic, coverPicDir); // Récupère uniquement le nom du fichier
        updateCoverPicUrl(userId, coverPicName);
        return coverPicName; // Retourne uniquement le nom du fichier
    }

    
    private String saveFile(MultipartFile file, String directoryPath) throws IOException {
        File dir = new File(directoryPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    
        // Générer un nom de fichier unique
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        
        // Sauvegarde du fichier dans le dossier
        file.transferTo(new File(directoryPath, fileName));
    
        // Retourne uniquement le nom du fichier
        return fileName;
    }
    
    private void updateProfilePicUrl(Long userId, String profilePicName) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setProfilePicture(profilePicName); // Stocker uniquement le nom du fichier
        userRepository.save(user);
    }
    
    private void updateCoverPicUrl(Long userId, String coverPicName) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setCoverPicture(coverPicName); // Stocker uniquement le nom du fichier
        userRepository.save(user);
    }
    

    public UserResponse getUserDetails(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        
        return new UserResponse(
            user.getFirstName(),
            user.getLastName(),
            user.getProfilePicture(),
            user.getCoverPicture()
        );
    }
}
