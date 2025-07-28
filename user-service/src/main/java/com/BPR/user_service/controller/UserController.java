package com.BPR.user_service.controller;

import com.BPR.user_service.entity.User;
import com.BPR.user_service.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(HttpServletRequest request, Authentication authentication) {
        String username = authentication.getName();
        System.out.println("UserId from token is: "+request.getHeader("X-User-Id"));
        System.out.println("UserRole from token is: "+request.getHeader("X-User-Role"));
        System.out.println("UserName from token is: "+request.getHeader("X-Username"));
        return ResponseEntity.of(userRepository.findByUsername(username));
    }
}
