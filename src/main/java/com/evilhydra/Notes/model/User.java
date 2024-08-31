package com.evilhydra.Notes.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Users")
public class User {

    @Id
    private String id;

    @NonNull
    @Indexed(unique = true)
    private String userId;

    @NonNull
    private String userName;

    @NonNull
    private String email;

    @NonNull
    private String password;

    private List<String> roles;

    @DBRef
    private List<Note> noteList = new ArrayList<>();

}
