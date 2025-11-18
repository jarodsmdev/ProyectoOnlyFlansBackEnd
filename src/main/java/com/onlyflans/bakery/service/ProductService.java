package com.onlyflans.bakery.service;

import com.onlyflans.bakery.model.Product;
import com.onlyflans.bakery.model.dto.request.ProductCreateRequest;
import com.onlyflans.bakery.model.dto.request.ProductUpdateRequest;
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

    public ProductDTO createProduct(ProductCreateRequest newProduct) {
        // Verificar si ya existe el código
        if(productPersistence.existsById(newProduct.codigo())){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Producto con código '" + newProduct.codigo() + "' ya existe"
            );
        }

        // Mapear DTO a la entidad Product
        Product product = new Product();

        // Mapear todos los campos del DTO a la Entity
        product.setCodigo(newProduct.codigo());
        product.setCategoria(newProduct.categoria());
        product.setNombre(newProduct.nombre());
        product.setDescripcion(newProduct.descripcion());
        product.setPrecio(newProduct.precio());
        product.setUrl(newProduct.url());

        // Guardar la entidad
        Product savedEntity = productPersistence.save(product);

        // Devolver el DTO de respuesta
        return ProductMapper.toDTO(savedEntity);
    }

    public ProductDTO getProductById(String codigo) {
        Product product = productPersistence.findById(codigo).orElseThrow(() ->
                new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Producto no encontrado"
                ));

        return ProductMapper.toDTO(product); // Usar el mapper estático
    }

    // Actualizar: Recibir Request DTO y Devolver Response DTO
    public ProductDTO updateProduct(String codigo, ProductUpdateRequest updateProduct) {
        Product existingProduct = productPersistence.findById(codigo).orElseThrow(() ->
                new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Producto no encontrado"
                ));

        // Aplicar cambios del DTO al Entity existente
        existingProduct.setCategoria(updateProduct.categoria());
        existingProduct.setNombre(updateProduct.nombre());
        existingProduct.setDescripcion(updateProduct.descripcion());
        existingProduct.setPrecio(updateProduct.precio());
        existingProduct.setUrl(updateProduct.url()); // Usar los getters del Request DTO

        Product updatedEntity = productPersistence.save(existingProduct);

        // Devolver el DTO actualizado
        return ProductMapper.toDTO(updatedEntity);
    }


    public void deleteProduct(String codigo) {
        if(!productPersistence.existsById(codigo)){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Producto no encontrado"
            );
        }
        productPersistence.deleteById(codigo);
    }

}
