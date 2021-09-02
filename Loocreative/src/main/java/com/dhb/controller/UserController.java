package com.dhb.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.dhb.exception.ResourceNotFoundException;
import com.dhb.exception.UsernameWasUsedException;
import com.dhb.pojo.User;
import com.dhb.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
@AllArgsConstructor
@CrossOrigin
public class UserController {
    private final UserService userService;
    private final Cloudinary cloudinary;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/users")
    public ResponseEntity<?> findAll(Pageable pageable) {
        Page<User> result = this.userService.findAll(pageable);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") String id) {
        return this.userService.findById(id).map(user -> ResponseEntity.ok(user))
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    @PostMapping("/users")
    public ResponseEntity<?> create(@ModelAttribute @Valid User userRequest,
                                    BindingResult bindingResult) throws IOException {
        if (!bindingResult.hasErrors()) {
            Map uploadResult;
            String path = "";
            if (userRequest.getFileEmoji() != null && !userRequest.getFileEmoji().isEmpty()) {
                uploadResult = cloudinary.uploader().upload(userRequest.getFileEmoji().getBytes(),
                        ObjectUtils.asMap(
                                "public_id", "loocreative/"
                                        + userRequest.getRole() + "/" + userRequest.getId()
                        ));
                path = uploadResult.get("url").toString();
            }

            userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));
            userRequest.setEmoji(path);

            User user = this.userService.createOrUpdate(userRequest);
            URI location = URI.create(String.format("/users/%s", user.getId()));
            return ResponseEntity.created(location).body(user);
        }
        return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> update(@ModelAttribute @Valid User userRequest,
                                    @PathVariable("id") String id,
                                    BindingResult bindingResult) {
        System.out.println(userRequest);
        if (!bindingResult.hasErrors()) {
            return this.userService.findById(id).map(user -> {
                if (this.userService.findByUsername(userRequest.getUsername()).isPresent()) {
                    if (!this.userService.findByUsername(userRequest.getUsername()).get().getUsername()
                            .equals(user.getUsername()))
                        throw new UsernameWasUsedException(userRequest.getUsername());
                }

                Map uploadResult = new HashMap();
                String path;
                if (userRequest.getFileEmoji() != null && !userRequest.getEmoji().isEmpty()) {
                    try {
                        uploadResult =cloudinary.uploader().upload(userRequest.getFileEmoji().getBytes(),
                                ObjectUtils.asMap(
                                        "public_id", "loocreative/" + user.getId()
                                ));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    path = uploadResult.get("url").toString();
                    user.setEmoji(path);
                }

                if (!userRequest.getPassword().equals(user.getPassword())) {
                    user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
                }

                user.setUsername(userRequest.getUsername());
                user.setPhone(userRequest.getPhone());
                user.setEmail(userRequest.getEmail());
                user.setAddress(userRequest.getAddress());
                user.setRole(userRequest.getRole());

                return ResponseEntity.ok(this.userService.createOrUpdate(user));
            }).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        }
        return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
    }

    @DeleteMapping("/users")
    public ResponseEntity<?> deleteAll() {
        this.userService.deleteAll();
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") String id) {
        this.userService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
