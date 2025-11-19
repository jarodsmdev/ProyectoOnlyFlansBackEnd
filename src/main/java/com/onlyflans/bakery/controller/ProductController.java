package com.onlyflans.bakery.controller;

import com.onlyflans.bakery.exception.dto.ErrorResponse;
import com.onlyflans.bakery.model.Product;
import com.onlyflans.bakery.model.dto.request.ProductCreateRequest;
import com.onlyflans.bakery.model.dto.request.ProductUpdateRequest;
import com.onlyflans.bakery.model.dto.response.ProductDTO;
import com.onlyflans.bakery.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;



@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Product Controller", description = "Endpoints para gestionar productos en la panadería OnlyFlans")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

//    @PostMapping
//    @Operation(summary = "Crear un nuevo producto", description = "Crea un nuevo producto en la panadería OnlyFlans")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "201", description = "Producto creado exitosamente"),
//            @ApiResponse(responseCode = "400", description = "Datos inválidos para crear el producto"),
//            @ApiResponse(responseCode = "500", description = "Error interno del servidor al intentar crear el producto")
//    })
//    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductCreateRequest newProduct){
//        ProductDTO saved = productService.createProduct(newProduct);
//        URI location = URI.create("/products/" + saved.getCodigo());
//        return ResponseEntity.created(location).body(saved);
//    }
    @Operation(
            summary = "Crear un nuevo producto",
            description = "Crea un producto nuevo en el sistema junto con una imagen. "
                    + "El archivo debe ser una imagen válida (png, jpeg, webp) y no exceder los 5 MB."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Producto creado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Solicitud inválida (archivo no permitido, datos incorrectos)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @Parameters({
            @Parameter(
                    name = "file",
                    description = "Imagen del producto (png, jpg, jpeg, webp). Máximo 5MB.",
                    required = true,
                    content = @Content(mediaType = "multipart/form-data")
            ),
            @Parameter(
                    name = "codigo",
                    description = "Código único del producto",
                    required = true,
                    example = "P-500"
            ),
            @Parameter(
                    name = "categoria",
                    description = "Categoría del producto",
                    required = true,
                    example = "pasteles"
            ),
            @Parameter(
                    name = "nombre",
                    description = "Nombre del producto",
                    required = true,
                    example = "Torta de Chocolate"
            ),
            @Parameter(
                    name = "descripcion",
                    description = "Descripción del producto",
                    required = true,
                    example = "Torta cubierta de chocolate belga"
            ),
            @Parameter(
                    name = "precio",
                    description = "Precio del producto",
                    required = true,
                    example = "12990"
            )
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductDTO> createProduct(
            @RequestParam("file") MultipartFile file,
            @RequestParam("codigo") String codigo,
            @RequestParam("categoria") String categoria,
            @RequestParam("nombre") String nombre,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("precio") Integer precio) throws IOException {

        ProductCreateRequest newProduct = new ProductCreateRequest(
                codigo,
                categoria,
                nombre,
                descripcion,
                precio
        );

        ProductDTO saved = productService.createProduct(newProduct, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
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

    @GetMapping("/{codigo}")
    @Operation(summary = "Obtener un producto por su código", description = "Recupera los detalles de un producto específico utilizando su código")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto recuperado exitosamente", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Product.class),
                    examples = @ExampleObject(
                            name = "Ejemplo producto",
                            value = "{\"Codigo\": \"TC001\", \"Categoria\": \"Tortas Cuadradas\", \"nombre\": \"Torta Cuadrada de Chocolate\", \"Descripcion\": \"Deliciosa torta de chocolate con capas de ganache y un toque de avellanas. Personalizable con mensajes especiales\", \"Precio\": \"45000\", \"Url imagen\": \"https://brigams.pe/wp-content/uploads/chocolate-2.jpg\"}"
                    )
            )),
            @ApiResponse(responseCode = "400", description = "Código de producto inválido"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado para recuperar el producto"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado con el código proporcionado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al intentar recuperar el producto")
    })
    public ResponseEntity<ProductDTO> getProductById(
        @Parameter(description = "Codigo del producto para buscar.", required = true, example = "TC001") @PathVariable String codigo){ {
    
        ProductDTO productDTO = productService.getProductById(codigo);
        return ResponseEntity.ok(productDTO);
    }}
    


    @PutMapping("/{codigo}")
    @Operation(summary = "Actualizar un producto existente", description = "Actualiza los detalles de un producto existente en la panadería OnlyFlans")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos para actualizar el producto"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado para actualizar el producto"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado para actualizar"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al intentar actualizar el producto")
    })
    public ResponseEntity<ProductDTO> updateProduct(
        @Parameter(description = "Codigo del producto a actualizar.", required = true, example = "TC001") @PathVariable String codigo, 
        @Valid @RequestBody ProductUpdateRequest product) {
        
        ProductDTO updatedProduct = productService.updateProduct(codigo, product);
        return ResponseEntity.ok(updatedProduct);
    }


    @DeleteMapping("/{codigo}")
    @Operation(summary = "Eliminar un producto", description = "Elimina un producto existente de la panadería OnlyFlans")
    @ApiResponses(value = { 
            @ApiResponse(responseCode = "204", description = "Producto eliminado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Código de producto inválido para eliminar"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado para eliminar el producto"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado para eliminar"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al intentar eliminar el producto")
    })
    public ResponseEntity<Void> deleteProduct(
        @Parameter(description = "Codigo del producto a eliminar.", required = true, example = "TC001") @PathVariable String codigo){

        productService.deleteProduct(codigo);
        
        // Devuelve 204 No Content explícitamente
        return ResponseEntity.noContent().build();
    }


}
