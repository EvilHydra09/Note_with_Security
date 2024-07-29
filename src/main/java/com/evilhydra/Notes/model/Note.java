package com.evilhydra.Notes.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Notes")
public class Note {
    @Id
    private String id;
    private String title;
    private String content;
    private LocalDateTime localDateTime;
    private String userName;

}
