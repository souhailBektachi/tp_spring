package com.souhailbektachi.hopital.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@Table(name = "PATIENTS")
public class Patient {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotEmpty
    @Size(min = 4, max = 40)
    @Column(length = 50)
    private String nom;
    
    @NotNull
    @Temporal(TemporalType.DATE)
    @Past(message = "La date de naissance doit être dans le passé")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateNaissance;
    
    private boolean malade;
    
    @Min(value = 0)
    @Max(value = 100)
    private int score;
}
