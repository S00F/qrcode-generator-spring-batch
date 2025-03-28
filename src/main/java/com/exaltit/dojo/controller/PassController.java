package com.exaltit.dojo.controller;



import com.exaltit.dojo.model.Pass;
import com.exaltit.dojo.repository.PassRepository;
import com.exaltit.dojo.service.BatchJobService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;


@RestController
@RequestMapping("/api/passes")
@RequiredArgsConstructor
public class PassController {
    private final BatchJobService batchJobService;
    private final PassRepository passRepository;

    @PostMapping("/generate-passes")
    @Operation(summary = "Start batch processing of passes from CSV")
    public ResponseEntity<String> generatePasses() {
        try {
            batchJobService.startBatchJob();
            return ResponseEntity.ok("Pass generation batch job started successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error starting batch job: " + e.getMessage());
        }
    }

    @GetMapping("/search")
    @Operation(summary = "Search for a pass")
    public ResponseEntity<Pass> searchPass(
            @RequestParam String nom,
            @RequestParam String prenom,
            @RequestParam LocalDate dateNaissance) {
        return passRepository.findByNomAndPrenomAndDateNaissance(nom, prenom, dateNaissance)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}