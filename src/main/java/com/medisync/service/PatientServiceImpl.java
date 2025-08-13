package com.medisync.service;

import com.medisync.dto.PatientDTO;
import com.medisync.mapper.PatientMapper;
import com.medisync.model.Patient;
import com.medisync.repository.PatientRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    @Transactional(readOnly = true)
    @Override
    public List<PatientDTO> getAllPatients() {
        // Simple, predictable order
        return patientRepository.findAll().stream()
                .sorted(Comparator.comparing(Patient::getLastName, String.CASE_INSENSITIVE_ORDER)
                        .thenComparing(p -> p.getFirstName() == null ? "" : p.getFirstName(), String.CASE_INSENSITIVE_ORDER))
                .map(PatientMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public PatientDTO getPatientById(UUID id) {
        Patient p = patientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found: " + id));
        return PatientMapper.toDto(p);
    }

    @Transactional
    @Override
    public PatientDTO createPatient(PatientDTO dto) {
        // Pre-check for cleaner error than DB constraint
        if (patientRepository.existsByEmailIgnoreCase(dto.getEmail())) {
            throw new DataIntegrityViolationException("Email already in use: " + dto.getEmail());
        }
        Patient toSave = PatientMapper.toNewEntity(dto);
        Patient saved = patientRepository.save(toSave);
        return PatientMapper.toDto(saved);
    }

    @Transactional
    @Override
    public PatientDTO updatePatient(UUID id, PatientDTO dto) {
        Patient existing = patientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found: " + id));

        // Enforce email uniqueness across other records
        if (dto.getEmail() != null &&
                patientRepository.existsByEmailIgnoreCaseAndIdNot(dto.getEmail(), id)) {
            throw new DataIntegrityViolationException("Email already in use: " + dto.getEmail());
        }

        PatientMapper.updateEntityFromDto(dto, existing);
        Patient saved = patientRepository.save(existing);
        return PatientMapper.toDto(saved);
    }

    @Transactional
    @Override
    public void deletePatient(UUID id) {
        if (!patientRepository.existsById(id)) {
            throw new EntityNotFoundException("Patient not found: " + id);
        }
        patientRepository.deleteById(id);
    }
}