package com.onlyflans.bakery.controller;

import com.onlyflans.bakery.model.dto.UserCreateRequest;
import com.onlyflans.bakery.model.dto.UserUpdateRequest;
import com.onlyflans.bakery.model.User;
import com.onlyflans.bakery.model.dto.LoginRequest;
import com.onlyflans.bakery.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Usuarios", description = "Operaciones relacionadas con los usuarios")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // GET
    @GetMapping
    @Operation(summary = "Obtener todos los usuarios registrados.", description = "Obtiene una lista con todos los usuarios registrados en la base de datos.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuarios listados correctamente.",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = User.class)),
                            examples = @ExampleObject(
                                    name = "Ejemplo lista de usuarios",
                                    value = "[{\"rut\": \"19291648-8\", \"nombres\": \"Juan Miguel\", \"apellidos\": \"Perez Aguirre\", \"fechaNacimiento\": \"2001-11-10\", \"email\": \"juan.perez@gmail.com\", \"contrasenna\": \"Contraseña123\"}, {\"rut\": \"21291648-8\", \"nombres\": \"Maria Angela\", \"apellidos\": \"Lopez Soto\", \"fechaNacimiento\": \"1995-05-20\", \"email\": \"maria.lopez@gmail.com\", \"contrasenna\": \"KAqet53_/#\"}]"
                            )
                    )
            ),
            @ApiResponse(responseCode = "204", description = "No se encontraron usuarios en la base de datos.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al listar los usuarios.", content = @Content)
    })
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return users.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(users);
    }

    // GET
    @GetMapping("/{rut}")
    @Operation(summary = "Obtener un usuario especifico.", description = "Obtiene el usuario que se especifica con su rut")
    @ApiResponses(value = {
            @ApiResponse(
                responseCode = "200", 
                description = "Usuario encontrado correctamente.", 
                content = @Content(
                    mediaType = "application/json", 
                    schema = @Schema(implementation = User.class), 
                    examples = @ExampleObject(
                        name = "Ejemplo de usuario filtrado", 
                        value = "{\"rut\": 20881702-K, \"contrasenna\": \"Contraseña123\", \"nombres\": \"Armando Pleito\", \"apellidos\": \"Delano Fuerte\", \"email\": \"ADelano@gmail.com\"}"))),
            @ApiResponse(responseCode = "404", description = "El Usuario no existe en la base de datos o el rut es incorrecto.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al buscar el usuario.", content = @Content)
    })
    public ResponseEntity<User> getUser(
        @Parameter(description = "Rut del usuario a buscar", required = true, example = "20881702-K") @PathVariable String rut) {
        User user = userService.getUser(rut);
        return ResponseEntity.ok(user);
    }

    // POST
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody @Valid UserCreateRequest createRequest) {
        User createdUser = userService.createUser(createRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody LoginRequest loginRequest){
        User user = userService.checkCredentials(loginRequest.rut(), loginRequest.contrasenna());

        return ResponseEntity.ok(user);
    }

    // PUT
    @PutMapping("/{rut}")
    public ResponseEntity<User> updateUser(@PathVariable String rut,
                                           @RequestBody @Valid UserUpdateRequest updateRequest) {
        User updatedUser = userService.updateUser(rut, updateRequest);
        return ResponseEntity.ok(updatedUser);
    }

    // PATCH
    @PatchMapping("/{rut}")
    public ResponseEntity<User> partialUpdateUser(@PathVariable String rut,
                                                  @RequestBody UserUpdateRequest updateRequest) {
        User updatedUser = userService.updateUser(rut, updateRequest);
        return ResponseEntity.ok(updatedUser);
    }

    // DELETE
    @DeleteMapping("/{rut}")
    public ResponseEntity<Void> deleteUser(@PathVariable String rut) {
        userService.deleteUser(rut);
        return ResponseEntity.noContent().build();
    }
}
