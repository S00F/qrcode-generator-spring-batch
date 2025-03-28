package com.exaltit.dojo.model;


import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
public class Pass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String prenom;
    private String nom;
    private LocalDate dateNaissance;
    private boolean statusVIP;
    private LocalDateTime dateHeureDeLaDemande;
    private LocalDateTime dateHeureDeGeneration;
    private String qrCodePath;

    @Enumerated(EnumType.STRING)
    private PassStatus status = PassStatus.PENDING;
}