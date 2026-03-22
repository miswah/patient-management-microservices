package com.miswah.patientservice.service.impl;


import billing.BillingResponse;
import com.miswah.patientservice.Exception.EmailAlreadyExistsException;
import com.miswah.patientservice.Exception.PatientNotFoundException;
import com.miswah.patientservice.dto.request.PatientRequestDTO;
import com.miswah.patientservice.dto.response.PatientResponseDTO;
import com.miswah.patientservice.grpc.BillingServiceGrpcClient;
import com.miswah.patientservice.model.Patient;
import com.miswah.patientservice.repository.PatientRepository;
import com.miswah.patientservice.service.PatientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final BillingServiceGrpcClient billingServiceGrpcClient;

    private static final Logger log = LoggerFactory.getLogger(PatientServiceImpl.class);

    @Autowired
    PatientServiceImpl(PatientRepository patientRepository, BillingServiceGrpcClient billingServiceGrpcClient){
        this.billingServiceGrpcClient = billingServiceGrpcClient;
        this.patientRepository = patientRepository;
    }

    @Override
    public List<PatientResponseDTO> getAllPatient() {
        List<Patient> model = this.patientRepository.findAll();
        return this.convertToDtoList(model);
    }

    @Override
    public PatientResponseDTO savePatient(PatientRequestDTO dto) {
        if(this.patientRepository.existsByEmail(dto.email())){
            throw new EmailAlreadyExistsException("A patient with this email already exists "+dto.email());
        }
        Patient patient = this.patientRepository.save(this.convertToModel(dto));
        BillingResponse billingServiceResponse = this.billingServiceGrpcClient.createBillingAccount(patient.getId().toString(), patient.getName(), patient.getEmail());

        log.info("Response received from billin service {}", billingServiceResponse);
        return convertToDto(patient);
    }

    @Override
    public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO dto) {
        Patient model = this.patientRepository.findById(id).orElseThrow(() -> new PatientNotFoundException("Patient not found for id "+ id));

        if(this.patientRepository.existsByEmailAndIdNot(dto.email(), id)){
            throw new EmailAlreadyExistsException("A patient with this email already exists "+dto.email());
        }

        model.setName(dto.name());
        model.setEmail(dto.email());
        model.setAddress(dto.address());
        model.setDateOfBirth(LocalDate.parse(dto.dateOfBirth()));

        this.patientRepository.save(model);

        return convertToDto(model);
    }

    @Override
    public void deletePatient(UUID id) {
        Patient model = this.patientRepository.findById(id).orElseThrow(() -> new PatientNotFoundException("Patient not found for id "+ id));

        this.patientRepository.delete(model);
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
