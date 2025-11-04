package com.onlyflans.bakery.model.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import io.swagger.v3.oas.annotations.media.Schema;


@Entity
@Table(name = "Users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @Schema(description = "Identificador del usuario mediante su rut.", example = "19291648-8")
    private String rut;

    @Schema(description = "Nombres del usuario.", example = "Vicente Andres")
    private String nombres;

    @Schema(description = "Apellidos del usuario.", example = "Briones Palacios")
    private String apellidos;
    
    @Schema(description = "Atributo para la fecha de nacimiento del usuario.", example = "10/11/2001")
    private LocalDate fechaNacimiento;
    
    @Column(unique = true)
    @Schema(description = "Correo electronico del usuario.", example = "astrid@gmail.com")
    private String email;

    @Schema(description = "Contraseña del usuario.", example = "churrete22costero")
    private String contrasenna;
}
