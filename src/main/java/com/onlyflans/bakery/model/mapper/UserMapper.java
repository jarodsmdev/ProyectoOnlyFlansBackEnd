package com.onlyflans.bakery.model.mapper;

import com.onlyflans.bakery.model.User;
import com.onlyflans.bakery.model.dto.OrderSummaryDTO;
import com.onlyflans.bakery.model.dto.UserDTO;

import java.util.stream.Collectors;

public class UserMapper {

    public static UserDTO toDTO(User user){
        if (user == null) return null;

        UserDTO dto = new UserDTO();
        dto.setRut(user.getRut());
        dto.setNombres(user.getNombres());
        dto.setApellidos(user.getApellidos());
        dto.setFechaNacimiento(user.getFechaNacimiento());
        dto.setEmail(user.getEmail());

        if (user.getOrders() != null){
            dto.setOrders(user.getOrders().stream().map(order -> {
                OrderSummaryDTO summary = new OrderSummaryDTO();
                summary.setId(order.getId());
                summary.setEstado(order.getEstado());
                summary.setTotal(order.getTotal());
                return summary;
            }).collect(Collectors.toList()));
        }

        return dto;
    }
}
