package com.dhb.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dk5jgf3xj",
                "api_key", "781767664334152",
                "api_secret", "JmhAHxHXKWsrnrfWnsRiMg_JsSw"
        ));
    }
}
