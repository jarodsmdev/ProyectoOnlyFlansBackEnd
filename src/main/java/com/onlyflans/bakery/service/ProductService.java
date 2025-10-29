package com.onlyflans.bakery.service;

import com.onlyflans.bakery.model.Product;
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

    public List<Product> getAllProducts(){
        return productPersistence.findAll();
    }

    public Product createProduct(Product product) {
        if(productPersistence.existsById(product.getCodigo())){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "producto ya existe"
            );
        }
        return productPersistence.save(product);
    }
}
