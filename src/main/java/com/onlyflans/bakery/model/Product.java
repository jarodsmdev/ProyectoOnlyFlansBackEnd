package com.onlyflans.bakery.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "Products")
@Data @AllArgsConstructor @NoArgsConstructor
public class Product {

    @Id
    @Schema(description = "Codigo del producto.", example = "TC001")
    private String codigo;

    @Schema(description = "Categoria del producto.", example = "Tortas Cuadradas")
    private String categoria;
    
    @Schema(description = "Nombre del producto.", example = "Torta Cuadrada de Chocolate")
    private String nombre;
    
    @Schema(description = "Descripcion del producto.", example = "Deliciosa torta de chocolate con capas de ganache y un toque de avellanas. Personalizable con mensajes especiales")
    private String descripcion;
    
    @Schema(description = "Precio del producto.", example = "45000")
    private Integer precio;
    
    @Schema(description = "URL de la imagen del producto.", example = "https://brigams.pe/wp-content/uploads/chocolate-2.jpg")
    private String url;

    /* Detalles de compra donde aparece el producto */
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<OrderDetail> orderDetails = new ArrayList<>();
}
