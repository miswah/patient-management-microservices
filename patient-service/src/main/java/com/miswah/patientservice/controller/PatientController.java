package com.miswah.patientservice.controller;


import com.miswah.patientservice.dto.request.PatientRequestDTO;
import com.miswah.patientservice.dto.response.PatientResponseDTO;
import com.miswah.patientservice.dto.validators.CreatePatientValidationGroup;
import com.miswah.patientservice.model.Patient;
import com.miswah.patientservice.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/patients")
@Tag(name="Patient", description="API for managing patients")
public class PatientController {

    private final PatientService patientService;

    @Autowired
    PatientController(PatientService patientService){
        this.patientService = patientService;
    }

    @GetMapping
    @Operation(summary ="Get Patients")
    public ResponseEntity<List<PatientResponseDTO>> getAllPatient() {
        return ResponseEntity.ok().body(this.patientService.getAllPatient());
    }

    @PostMapping
    public ResponseEntity<PatientResponseDTO> savePatient(@Validated({Default.class, CreatePatientValidationGroup.class}) @RequestBody PatientRequestDTO dto) {
        return ResponseEntity.ok().body(this.patientService.savePatient(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientResponseDTO> updatePatient(@PathVariable UUID id, @Validated({Default.class}) @RequestBody PatientRequestDTO dto){
        return ResponseEntity.ok().body(this.patientService.updatePatient(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable UUID id){
        this.patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
}
