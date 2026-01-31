package com.miswah.patientservice.service;


import com.miswah.patientservice.dto.request.PatientRequestDTO;
import com.miswah.patientservice.dto.response.PatientResponseDTO;
import com.miswah.patientservice.model.Patient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PatientService {
   List<PatientResponseDTO> getAllPatient();

  void savePatient(PatientRequestDTO dto);
}
