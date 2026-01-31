package com.miswah.patientservice.service;


import com.miswah.patientservice.dto.request.PatientRequestDTO;
import com.miswah.patientservice.dto.response.PatientResponseDTO;
import com.miswah.patientservice.model.Patient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface PatientService {
   List<PatientResponseDTO> getAllPatient();

   PatientResponseDTO savePatient(PatientRequestDTO dto);

   PatientResponseDTO updatePatient(UUID id, PatientRequestDTO dto);

   void deletePatient(UUID id);
}
