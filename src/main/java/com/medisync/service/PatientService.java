package com.medisync.service;

import com.medisync.dto.PatientDTO;

import java.util.List;
import java.util.UUID;

public interface PatientService {
    List<PatientDTO> getAllPatients();
    PatientDTO getPatientById(UUID id);
    PatientDTO createPatient(PatientDTO dto);
    PatientDTO updatePatient(UUID id, PatientDTO dto);
    void deletePatient(UUID id);
}