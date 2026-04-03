package com.example.aichatbot.security;

import com.example.aichatbot.entity.AppUser;
import com.example.aichatbot.entity.AuthProvider;
import com.example.aichatbot.repository.AppUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final AppUserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String sub = oAuth2User.getAttribute("sub");

        AppUser user = userRepository.findByEmail(email).orElseGet(() -> {
            String username = email.split("@")[0];
            // Ensure uniqueness
            if (userRepository.existsByUsername(username)) {
                username = username + "_" + sub.substring(0, 6);
            }
            AppUser newUser = AppUser.builder()
                    .username(username)
                    .email(email)
                    .provider(AuthProvider.GOOGLE)
                    .providerId(sub)
                    .build();
            return userRepository.save(newUser);
        });

        String token = jwtUtil.generateToken(user.getUsername());
        String redirectUrl = "http://localhost:5173/oauth2/callback?token=" + token;
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}
