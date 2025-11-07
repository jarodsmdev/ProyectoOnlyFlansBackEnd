package com.onlyflans.bakery.model.mapper;

import com.onlyflans.bakery.model.Product;
import com.onlyflans.bakery.model.dto.ProductDTO;

public class ProductMapper {

    public static ProductDTO toDTO(Product product){

        if (product == null) return null;

        ProductDTO dto = new ProductDTO();
        dto.setCodigo(product.getCodigo());
        dto.setCategoria(product.getCategoria());
        dto.setNombre(product.getNombre());
        dto.setDescripcion(product.getDescripcion());
        dto.setPrecio(product.getPrecio());
        dto.setUrl(product.getUrl());

        return dto;
    }
}
