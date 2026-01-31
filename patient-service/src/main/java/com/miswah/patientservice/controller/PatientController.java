package com.miswah.patientservice.controller;


import com.miswah.patientservice.dto.request.PatientRequestDTO;
import com.miswah.patientservice.dto.response.PatientResponseDTO;
import com.miswah.patientservice.model.Patient;
import com.miswah.patientservice.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;

    @Autowired
    PatientController(PatientService patientService){
        this.patientService = patientService;
    }

    @GetMapping
    public ResponseEntity<List<PatientResponseDTO>> getAllPatient() {
        return ResponseEntity.ok().body(this.patientService.getAllPatient());
    }

    @PostMapping
    public ResponseEntity<String> savePatient(@Valid @RequestBody PatientRequestDTO dto) {
        this.patientService.savePatient(dto);
        return ResponseEntity.ok("saved");
    }
}
