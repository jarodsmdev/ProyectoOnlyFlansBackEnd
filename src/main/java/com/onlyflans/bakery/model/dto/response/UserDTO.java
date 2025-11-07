package com.onlyflans.bakery.model.dto.response;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class UserDTO {
    private String rut;
    private String nombres;
    private String apellidos;
    private LocalDate fechaNacimiento;
    private String email;
    private List<OrderSummaryDTO> orders;
}
