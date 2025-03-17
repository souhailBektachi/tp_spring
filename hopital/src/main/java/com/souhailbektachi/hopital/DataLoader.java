package com.souhailbektachi.hopital;

import com.souhailbektachi.hopital.entities.Patient;
import com.souhailbektachi.hopital.repositories.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final PatientRepository patientRepository;

    @Override
    public void run(String... args) {
        Patient[] patients = {
            Patient.builder().nom("Hassan").dateNaissance(new Date()).malade(true).score(50).build(),
            Patient.builder().nom("Mohammed").dateNaissance(new Date()).malade(false).score(80).build(),
            Patient.builder().nom("Yassine").dateNaissance(new Date()).malade(true).score(30).build(),
            Patient.builder().nom("Hanae").dateNaissance(new Date()).malade(false).score(70).build(),
            Patient.builder().nom("Karima").dateNaissance(new Date()).malade(true).score(90).build()
        };
        for(Patient patient : patients) {
            patientRepository.save(patient);
        }
    }
}
