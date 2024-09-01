package com.evilhydra.Notes.config;

import com.evilhydra.Notes.model.User;
import com.evilhydra.Notes.service.UserService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class FirebaseFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(FirebaseFilter.class);

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7); // Remove "Bearer " prefix

        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
            String uid = decodedToken.getUid();

            Optional<User>  existingUser = userService.getUserById(uid);
            if(existingUser.isEmpty()){
                // If the user doesn't exist , create a new user in the database
                User newUser = new User();
                newUser.setId(uid);
                newUser.setUserId(uid);
                // Handle null values for name
                String name = decodedToken.getName();
                newUser.setUserName((name != null && !name.isEmpty()) ? name : "User");

                // Extract provider from the decoded token
                String provider = getProviderFromToken(decodedToken);
                newUser.setProvider(provider);


                newUser.setEmail(decodedToken.getEmail());
                newUser.setRoles(List.of("USER"));
                newUser.setPassword("");
                userService.createUser(newUser);
            }else {
                // Update existing user details
                User user = existingUser.get();
                String name = decodedToken.getName();
                user.setUserName((name != null && !name.isEmpty()) ? name : "User");
                userService.updateUser(user);
            }



            var authentication = new UsernamePasswordAuthenticationToken(uid, null, null);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            logger.error("Firebase ID token is invalid.", e);
        }

        filterChain.doFilter(request, response);
    }
    private String getProviderFromToken(FirebaseToken decodedToken) {
        // Check if the decoded token has a claims map with the 'firebase' claim
        if (decodedToken.getClaims().containsKey("firebase")) {
            Map<String, Object> firebaseClaims = (Map<String, Object>) decodedToken.getClaims().get("firebase");
            if (firebaseClaims != null && firebaseClaims.containsKey("sign_in_provider")) {
                return (String) firebaseClaims.get("sign_in_provider");
            }
        }
        return "Firebase"; // Default to Firebase if no specific provider is found
    }

}
