package ru.netology.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

@Configuration
public class AppConfig {

    @Bean
    public PostRepository repository() {
        return new PostRepository();
    }

    @Bean
    public PostService postService() {
        return new PostService(repository());
    }
}