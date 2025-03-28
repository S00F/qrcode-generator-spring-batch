package com.exaltit.dojo.repository;


import com.exaltit.dojo.model.Pass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface PassRepository extends JpaRepository<Pass, Long> {

    Optional<Pass> findByNomAndPrenomAndDateNaissance(String nom, String prenom, LocalDate dateNaissance);

}