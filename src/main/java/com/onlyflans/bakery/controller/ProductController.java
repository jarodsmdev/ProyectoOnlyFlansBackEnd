package com.onlyflans.bakery.controller;

import com.onlyflans.bakery.model.Product;
import com.onlyflans.bakery.model.dto.response.ProductDTO;
import com.onlyflans.bakery.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Product Controller", description = "Endpoints para gestionar productos en la panadería OnlyFlans")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo producto", description = "Crea un nuevo producto en la panadería OnlyFlans")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Producto creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos para crear el producto"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al intentar crear el producto")
    })
    public ResponseEntity<ProductDTO> createProduct(@RequestBody Product product){
        ProductDTO saved = productService.createProduct(product);
        URI location = URI.create("/products/" + saved.getCodigo());
        return ResponseEntity.created(location).body(saved);
    }

    @GetMapping
    @Operation(summary = "Obtener todos los productos", description = "Recupera la lista de todos los productos disponibles en la panadería OnlyFlans")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de productos recuperada exitosamente", content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Product.class)),
                            examples = @ExampleObject(
                                    name = "Ejemplo lista de productos",
                                    value = "[{\"Codigo\": \"TC001\", \"Categoria\": \"Tortas Cuadradas\", \"nombre\": \"Torta Cuadrada de Chocolate\", \"Descripcion\": \"Deliciosa torta de chocolate con capas de ganache y un toque de avellanas. Personalizable con mensajes especiales\", \"Precio\": \"45000\", \"Url imagen\": \"https://brigams.pe/wp-content/uploads/chocolate-2.jpg\"}, {\"Codigo\": \"TC002\", \"Categoria\": \"Tortas Cuadradas\", \"nombre\": \"Torta Cuadrada de Frutas\", \"Descripcion\": \"Una mezcla de frutas frescas y crema chantilly sobre un suave bizcocho de vainilla, ideal para celebraciones.\", \"Precio\": \"50000\", \"Url imagen\": \"https://brigams.pe/wp-content/uploads/tutifruti-2-1000x667.jpg\"}]"
                            )
                    )),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al intentar recuperar los productos")
    })
    public ResponseEntity<List<ProductDTO>> getAllProducts(){
        return ResponseEntity.ok(productService.getAllProducts());
    }
}
