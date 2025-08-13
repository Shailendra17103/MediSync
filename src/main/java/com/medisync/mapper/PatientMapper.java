package com.medisync.mapper;

import com.medisync.dto.PatientDTO;
import com.medisync.model.Patient;

public final class PatientMapper {
    private PatientMapper() {}

    public static PatientDTO toDto(Patient p) {
        if (p == null) return null;
        PatientDTO dto = new PatientDTO();
        dto.setId(p.getId());
        dto.setFirstName(p.getFirstName());
        dto.setLastName(p.getLastName());
        dto.setEmail(p.getEmail());
        dto.setDateOfBirth(p.getDateOfBirth());
        dto.setGender(p.getGender());
        dto.setPhoneNumber(p.getPhoneNumber());
        return dto;
    }

    public static void updateEntityFromDto(PatientDTO dto, Patient target) {
        target.setFirstName(dto.getFirstName());
        target.setLastName(dto.getLastName());
        target.setEmail(dto.getEmail());
        target.setDateOfBirth(dto.getDateOfBirth());
        target.setGender(dto.getGender());
        target.setPhoneNumber(dto.getPhoneNumber());
    }

    public static Patient toNewEntity(PatientDTO dto) {
        Patient p = new Patient();
        updateEntityFromDto(dto, p);
        return p;
    }
}