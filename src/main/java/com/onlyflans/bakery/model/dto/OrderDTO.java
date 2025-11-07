package com.onlyflans.bakery.model.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class OrderDTO {
    private String id;
    private LocalDate fecha;
    private String estado;
    private Integer total;
    private String rutUsuario; // solo el rut
    private List<OrderDetailDTO> orderDetails;
}
