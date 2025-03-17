package com.souhailbektachi.hopital.web;

import com.souhailbektachi.hopital.entities.Patient;
import com.souhailbektachi.hopital.repositories.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {
    private final PatientRepository patientRepository;

    @GetMapping
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    @GetMapping("/{id}")
    public Patient getPatient(@PathVariable Long id) {
        return patientRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Patient savePatient(@RequestBody Patient patient) {
        return patientRepository.save(patient);
    }
}
