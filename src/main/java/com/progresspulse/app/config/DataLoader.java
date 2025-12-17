/*
package com.progresspulse.app.config;

import com.progresspulse.app.model.User;
import com.progresspulse.app.repository.UserRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.boot.context.event.ApplicationReadyEvent;

@Component
public class DataLoader {

    private final UserRepository userRepository;

    public DataLoader(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void seed() {
        if (userRepository.count() == 0) {
            User user = new User();
            user.setName("Test User");
            user.setEmail("test@progresspulse.com");
            userRepository.save(user);
        }
    }
}

 */


