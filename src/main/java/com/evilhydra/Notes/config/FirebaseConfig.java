package com.evilhydra.Notes.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

@Configuration
public class FirebaseConfig {

    private static final Logger logger = LoggerFactory.getLogger(FirebaseConfig.class);

    @PostConstruct
    public void initialize() throws IOException {
        // Get the base64-encoded Firebase credentials from the environment variable
        String firebaseCredentialsBase64 = System.getenv("FIREBASE_CREDENTIALS");

        if (firebaseCredentialsBase64 == null || firebaseCredentialsBase64.isEmpty()) {
            logger.error("Firebase credentials are not set in environment variables.");
            throw new IllegalStateException("Firebase credentials are not set in environment variables.");
        }

        // Decode the base64-encoded credentials
        byte[] decodedCredentials = Base64.getDecoder().decode(firebaseCredentialsBase64);

        // Initialize Firebase with the decoded credentials
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(new ByteArrayInputStream(decodedCredentials)))
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
            logger.info("Firebase app initialized with name: {}", FirebaseApp.getInstance().getName());
        } else {
            logger.info("Firebase app already initialized with name: {}", FirebaseApp.getInstance().getName());
        }
    }
}