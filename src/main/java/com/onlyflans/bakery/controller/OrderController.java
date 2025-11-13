package com.onlyflans.bakery.controller;

import com.onlyflans.bakery.model.Order;
import com.onlyflans.bakery.model.dto.request.OrderCreateRequest;
import com.onlyflans.bakery.model.dto.request.OrderUpdateRequest;
import com.onlyflans.bakery.model.dto.response.OrderDTO;
import com.onlyflans.bakery.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@Tag(name = "Order Controller", description = "Endpoints para gestionar las órdenes de la panadería")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // GET
    @GetMapping
    @Operation(summary = "Obtener todas las órdenes", description = "Recupera una lista de todas las órdenes en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de órdenes recuperada exitosamente.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Order.class)), examples = @ExampleObject(name = "Ejemplo de lista de órdenes", value = "[{\"id\": \"550e8400-e29b-41d4-a716-446655440000\", \"fecha\": \"2024-05-15\", \"estado\": \"PENDIENTE\", \"total\": 25000, \"user\": {\"rut\": \"20881702-K\", \"nombres\": \"Armando Pleito\", \"apellidos\": \"Delano Fuerte\", \"email\": \"a.pleito@gmail.com\"}}, {\"id\": \"660e8400-e29b-41d4-a716-446655440111\", \"fecha\": \"2024-05-16\", \"estado\": \"COMPLETADA\", \"total\": 15000, \"user\": {\"rut\": \"19876543-2\", \"nombres\": \"Beatriz Gomez\", \"apellidos\": \"Lopez Martinez\", \"email\": \"b.gomez@gmail.com\"}}]"))),
            @ApiResponse(responseCode = "204", description = "No hay órdenes disponibles en el sistema.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al intentar recuperar las órdenes.", content = @Content)
    })
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<OrderDTO> orders = orderService.getAllOrders();
        return orders.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una orden por ID", description = "Recupera una orden específica utilizando su ID único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orden recuperada exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class), examples = @ExampleObject(name = "Ejemplo de orden", value = "{\"id\": \"550e8400-e29b-41d4-a716-446655440000\", \"fecha\": \"2024-05-15\", \"estado\": \"PENDIENTE\", \"total\": 25000, \"user\": {\"rut\": \"20881702-K\", \"nombres\": \"Armando Pleito\", \"apellidos\": \"Delano Fuerte\", \"email\": \"a.pleito@gmail.com\"}}"))),
            @ApiResponse(responseCode = "404", description = "La orden con el ID especificado no existe.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al intentar recuperar la orden.", content = @Content)
    })
    public ResponseEntity<OrderDTO> getOrder(@PathVariable String id) {
        OrderDTO order = orderService.getOrder(id);
        return order == null
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(order);
    }

    // POST
    @PostMapping
    @Operation(summary = "Crear una nueva orden", description = "Crea una nueva orden en el sistema con los detalles proporcionados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Orden creada exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class), examples = @ExampleObject(name = "Ejemplo de orden creada", value = "{\"id\": \"550e8400-e29b-41d4-a716-446655440000\", \"fecha\": \"2024-05-15\", \"estado\": \"PENDIENTE\", \"total\": 25000, \"user\": {\"rut\": \"20881702-K\", \"nombres\": \"Armando Pleito\", \"apellidos\": \"Delano Fuerte\", \"email\": \"a.pleito@gmail.com\"}}"))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida. Verifique los datos proporcionados para la creación de la orden.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al intentar crear la orden.", content = @Content)
    })
    public ResponseEntity<OrderDTO> createOrder(@RequestBody @Valid OrderCreateRequest request) {
        OrderDTO orderCreated = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderCreated);
    }

    // 🟧 PUT: actualizar una orden
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una orden existente", description = "Permite modificar el estado o total de una orden específica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orden actualizada exitosamente.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDTO.class))),
            @ApiResponse(responseCode = "404", description = "La orden con el ID especificado no existe.", content = @Content)
    })
    public ResponseEntity<OrderDTO> updateOrder(
            @PathVariable String id,
            @RequestBody @Valid OrderUpdateRequest request
    ) {
        OrderDTO updatedOrder = orderService.updateOrder(id, request);
        return ResponseEntity.ok(updatedOrder);
    }

    // DELETE
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una orden", description = "Elimina una orden específica del sistema utilizando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Orden eliminada exitosamente.", content = @Content),
            @ApiResponse(responseCode = "404", description = "La orden con el ID especificado no existe.", content = @Content)
    })
    public ResponseEntity<Void> deleteOrder(
            @Parameter(description = "Id de la orden para eliminarla.", required = true, example = "550e8400-e29b-41d4-a716-446655440000") @PathVariable String id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
