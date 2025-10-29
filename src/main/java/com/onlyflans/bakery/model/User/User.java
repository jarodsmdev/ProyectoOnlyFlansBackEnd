package com.onlyflans.bakery.model.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    private String rut;
    private String nombres;
    private String apellidos;
    private LocalDate fechaNacimiento;
    @Column(unique = true)
    private String email;
    private String contrasenna;
}
