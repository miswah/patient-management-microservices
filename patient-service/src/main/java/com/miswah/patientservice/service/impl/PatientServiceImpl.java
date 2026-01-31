package com.miswah.patientservice.service.impl;


import com.miswah.patientservice.dto.request.PatientRequestDTO;
import com.miswah.patientservice.dto.response.PatientResponseDTO;
import com.miswah.patientservice.model.Patient;
import com.miswah.patientservice.repository.PatientRepository;
import com.miswah.patientservice.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    @Autowired
    PatientServiceImpl(PatientRepository patientRepository){
        this.patientRepository = patientRepository;
    }

    @Override
    public List<PatientResponseDTO> getAllPatient() {
        List<Patient> model = this.patientRepository.findAll();
        return this.convertToDtoList(model);
    }

    @Override
    public void savePatient(PatientRequestDTO dto) {
        this.patientRepository.save(this.convertToModel(dto));
    }

    private static PatientResponseDTO convertToDto(Patient model){
        return new PatientResponseDTO(model.getId().toString(), model.getName(), model.getEmail(), model.getAddress(), model.getDateOfBirth().toString());
    }

    private List<PatientResponseDTO> convertToDtoList(List<Patient> model){
        return model.stream().map(PatientServiceImpl::convertToDto).toList();
    }

    private Patient convertToModel(PatientRequestDTO dto){
        Patient model = new Patient();
        model.setName(dto.name());
        model.setEmail(dto.email());
        model.setAddress(dto.address());
        model.setDateOfBirth(LocalDate.parse(dto.dateOfBirth()));
        model.setRegisteredDate(LocalDate.parse(dto.registeredDate()));
        return model;
    }
}
