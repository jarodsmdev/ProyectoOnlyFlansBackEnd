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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

@Service
@Transactional
public class ProductService {

    private final IProductPersistence productPersistence;
    private final S3Service s3Service;

    public ProductService(IProductPersistence productPersistence, S3Service s3Service) {
        this.productPersistence = productPersistence;
        this.s3Service = s3Service;
    }

    public List<ProductDTO> getAllProducts(){
        return productPersistence.findAll()
                .stream()
                .map(ProductMapper::toDTO)
                .toList();
    }

    public ProductDTO createProduct(ProductCreateRequest newProduct, MultipartFile file) throws IOException {
        // Verificar si ya existe el código 
        if(productPersistence.existsById(newProduct.codigo())){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Producto con código '" + newProduct.codigo() + "' ya existe"
            );
        }

        // 1. Subir imagen a S3
        String url = s3Service.uploadFile(file, "products/" + newProduct.codigo());

        // 2. Crear la entidad producto
        // Mapear DTO a la entidad Product
        Product product = new Product();

        // Mapear todos los campos del DTO a la Entity
        product.setCodigo(newProduct.codigo());
        product.setCategoria(newProduct.categoria());
        product.setNombre(newProduct.nombre());
        product.setDescripcion(newProduct.descripcion());
        product.setPrecio(newProduct.precio());
        product.setUrl(url); // Usar la URL devuelta por S3

        // 3. Guardar la entidad
        Product savedEntity = productPersistence.save(product);
        
        // 4. Devolver el DTO de respuesta
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
    public ProductDTO updateProduct(String codigo, ProductUpdateRequest updateProduct, MultipartFile file) throws IOException {
        Product existingProduct = productPersistence.findById(codigo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));

        existingProduct.setCategoria(updateProduct.categoria());
        existingProduct.setNombre(updateProduct.nombre());
        existingProduct.setDescripcion(updateProduct.descripcion());
        existingProduct.setPrecio(updateProduct.precio());

        // SI hay archivo → subir a S3
        if (file != null && !file.isEmpty()) {
            String url = s3Service.uploadFile(file, "products/" + codigo);
            existingProduct.setUrl(url);
        }
        // SI NO hay archivo → mantener URL original

        return ProductMapper.toDTO(productPersistence.save(existingProduct));
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
