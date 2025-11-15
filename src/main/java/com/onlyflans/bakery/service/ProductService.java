package com.onlyflans.bakery.service;

import com.onlyflans.bakery.model.Product;
import com.onlyflans.bakery.model.dto.response.ProductDTO;
import com.onlyflans.bakery.model.mapper.ProductMapper;
import com.onlyflans.bakery.persistence.IProductPersistence;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class ProductService {

    private final IProductPersistence productPersistence;

    public ProductService(IProductPersistence productPersistence) {
        this.productPersistence = productPersistence;
    }

    public List<ProductDTO> getAllProducts(){
        return productPersistence.findAll()
                .stream()
                .map(ProductMapper::toDTO)
                .toList();
    }

    public ProductDTO getProduct(String codigo){
        Product product = productPersistence.findById(codigo)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Producto con el codigo '" + codigo + "' no encontrado."
                ));

        return (ProductMapper.toDTO(product));
    }

    public ProductDTO createProduct(Product product) {
        if(productPersistence.existsById(product.getCodigo())){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "producto ya existe"
            );
        }

        productPersistence.save(product);
        return ProductMapper.toDTO(product);
    }

    public ProductDTO updateProduct(String codigo, Product product){
        Product productToUpdate = productPersistence.findById(codigo).
                orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Producto con el codigo '" + codigo + "' no encontrado."
                ));

        if (product.getCategoria() != null){
            productToUpdate.setCategoria(product.getCategoria());
        }
        if (product.getNombre() != null){
            productToUpdate.setNombre(product.getNombre());
        }
        if (product.getDescripcion() != null){
            productToUpdate.setDescripcion(product.getDescripcion());
        }
        if (product.getPrecio() != null){
            productToUpdate.setPrecio(product.getPrecio());
        }
        if (product.getUrl() != null){
            productToUpdate.setUrl(product.getUrl());
        }

        productPersistence.save(productToUpdate);
        return ProductMapper.toDTO(productToUpdate);
    }

    public void deleteProduct(String codigo){
        Product product = productPersistence.findById(codigo).
                orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Producto con el codigo '" + codigo + "' no encontrado."
                ));

        productPersistence.delete(product);
    }
}
