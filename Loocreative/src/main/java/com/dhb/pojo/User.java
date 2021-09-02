package com.dhb.pojo;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.UUID;

@Data
@Entity
@Table(name = "user")
public class User implements Serializable {
    @Id
    private String id = UUID.randomUUID().toString();
    private String username;
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")
    private String password;
    @Email
    private String email;
    @Pattern(regexp = "/\\(?([0-9]{3})\\)?([ .-]?)([0-9]{3})\2([0-9]{4})/")
    private String phone;
    private String address;
    private String emoji;
    @Transient
    private MultipartFile fileEmoji;
    private String role;
}
