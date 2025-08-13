package com.medisync.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(
        name = "patients",
        uniqueConstraints = @UniqueConstraint(columnNames = "email")
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Patient {

    @Id
    @GeneratedValue
    @UuidGenerator // Optional, explicit for Hibernate 6; safe to keep since ID gen already works
    @EqualsAndHashCode.Include
    private UUID id;

    @NotBlank
    @Size(max = 100)
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @NotBlank
    @Size(max = 100)
    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Email
    @NotBlank
    @Size(max = 255)
    @Column(name = "email", nullable = false, length = 255)
    private String email;

    @Past
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false, length = 32)
    private Gender gender;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$")
    @Column(name = "phone_number", length = 16)
    private String phoneNumber;

    // Optional auditing fields (uncomment if you want timestamps)
    // @CreationTimestamp
    // @Column(name = "created_at", nullable = false, updatable = false)
    // private java.time.OffsetDateTime createdAt;
    //
    // @UpdateTimestamp
    // @Column(name = "updated_at", nullable = false)
    // private java.time.OffsetDateTime updatedAt;
}