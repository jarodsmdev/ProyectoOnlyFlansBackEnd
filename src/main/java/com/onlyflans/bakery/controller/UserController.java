package com.onlyflans.bakery.controller;

import com.onlyflans.bakery.model.dto.request.UserCreateRequest;
import com.onlyflans.bakery.model.dto.request.UserRoleUpdateRequest;
import com.onlyflans.bakery.model.dto.request.UserUpdateRequest;
import com.onlyflans.bakery.model.User;
import com.onlyflans.bakery.model.dto.request.LoginRequest;
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
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('ADMIN')")
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
                                    value = "[{\"rut\": \"19291648-8\", \"nombres\": \"Juan Miguel\", \"apellidos\": \"Perez Aguirre\", \"fechaNacimiento\": \"2001-11-10\", \"email\": \"juan.perez@gmail.com\", \"contrasenna\": \"Contraseña123\", \"role\": \"NORMAL\"}, {\"rut\": \"21291648-8\", \"nombres\": \"Maria Angela\", \"apellidos\": \"Lopez Soto\", \"fechaNacimiento\": \"1995-05-20\", \"email\": \"maria.lopez@gmail.com\", \"contrasenna\": \"KAqet53_/#\", \"role\": \"NORMAL\"}]"
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

    // GET (usuario por rut)
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
                        value = "{\"rut\": 20881702-K, \"contrasenna\": \"Contraseña123\", \"nombres\": \"Armando Pleito\", \"apellidos\": \"Delano Fuerte\", \"email\": \"ADelano@gmail.com\", \"role\": \"ADMIN\"}"))),
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
    @Operation(summary = "Crear un nuevo usuario.", description = "Crea un nuevo usuario en la base de datos con la información proporcionada.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Usuario creado correctamente.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = User.class),
                            examples = @ExampleObject(
                                    name = "Ejemplo de usuario creado",
                                    value = "{\"rut\": \"19291648-8\", \"nombres\": \"Juan Miguel\", \"apellidos\": \"Perez Aguirre\", \"fechaNacimiento\": \"2001-11-10\", \"email\": \" paguirre@gmail.com\", \"contrasenna\": \"Contraseña123\", \"role\": \"NORMAL\"}"))),
            @ApiResponse(responseCode = "400", description = "Creacion de usuario inválida. Verifique los datos proporcionados.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al intentar crear el usuario.", content = @Content)
    })
    public ResponseEntity<User> createUser(@RequestBody @Valid UserCreateRequest createRequest) {
        User createdUser = userService.createUser(createRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PostMapping("/login")
    @Operation(summary = "Login de usuario.", description = "Verifica las credenciales del usuario para iniciar sesión.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuario autenticado correctamente.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = User.class),
                            examples = @ExampleObject(
                                    name = "Ejemplo de usuario autenticado",
                                    value = "{\"rut\": \"14295638-8\", \"nombres\": \"Esteban Miguel\", \"apellidos\": \"Farias Castro\", \"fechaNacimiento\": \"1989-04-15\", \"email\": fcastro@gmail.com, \"contrasenna\": \"Contraseña123\", \"role\": \"NORMAL\"}"))),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas. No se pudo autenticar al usuario.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor durante el proceso de autenticación de usuario.", content = @Content)
    })
    public ResponseEntity<User> login(@RequestBody LoginRequest loginRequest){
        User user = userService.checkCredentials(loginRequest.email(), loginRequest.password());

        return ResponseEntity.ok(user);
    }

    // PUT
    @PutMapping("/{rut}")
    @Operation(summary = "Actualizar un usuario existente.", description = "Actualiza la información de un usuario existente en la base de datos segun su rut.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuario actualizado correctamente.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = User.class),
                            examples = @ExampleObject(
                                    name = "Ejemplo de usuario actualizado",
                                    value = "{\"rut\": \"20351648-K\", \"nombres\": \"Miguel\", \"apellidos\": \"Suazo Diaz\", \"fechaNacimiento\": \"2003-03-18\", \"email\": \"mi.suazo@gmail.com\", \"contrasenna\": \"NuevaContraseña123\", role\": \"NORMAL\"}"))),
                @ApiResponse(responseCode = "400", description = "Actualización de usuario inválida. Verifique los datos proporcionados.", content = @Content),
                @ApiResponse(responseCode = "404", description = "El Usuario no existe en la base de datos o el rut es incorrecto.", content = @Content),
                @ApiResponse(responseCode = "500", description = "Error interno del servidor al intentar actualizar el usuario.", content = @Content)
        })
    public ResponseEntity<User> updateUser(
        @Parameter(description = "Rut del usuario a actualizar", required = true, example = "20351648-K") @PathVariable String rut,
        @RequestBody @Valid UserUpdateRequest updateRequest) {
        User updatedUser = userService.updateUser(rut, updateRequest);
        return ResponseEntity.ok(updatedUser);
    }

    // PATCH
    @PatchMapping("/{rut}")
    @Operation(summary = "Actualizar parcialmente un usuario existente.", description = "Actualiza parcialmente la información de un usuario existente en la base de datos segun su rut.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuario actualizado correctamente.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = User.class),
                            examples = @ExampleObject(
                                    name = "Ejemplo de usuario actualizado parcialmente",
                                    value = "{\"rut\": \"20351648-K\", \"nombres\": \"Miguel Angel\", \"apellidos\": \"Suazo Diaz\", \"fechaNacimiento\": \"2003-03-18\", \"email\": \"an.msuazo@gmail.com\", \"contrasenna\": \"JKdnjewew3$&\", role\": \"NORMAL\"}"))),
                @ApiResponse(responseCode = "400", description = "Actualización parcial de usuario inválida. Verifique el rut u otros campos proporcionados.", content = @Content),
                @ApiResponse(responseCode = "404", description = "El Usuario no existe en la base de datos o el rut es incorrecto.", content = @Content),
                @ApiResponse(responseCode = "500", description = "Error interno del servidor al intentar actualizar parcialmente el usuario.", content = @Content)
        })
    public ResponseEntity<User> partialUpdateUser(
        @Parameter(description = "Rut del usuario a actualizar (parcialmente)", required = true, example = "20351648-K") @PathVariable String rut,
        @RequestBody UserUpdateRequest updateRequest) {
        User updatedUser = userService.updateUser(rut, updateRequest);
        return ResponseEntity.ok(updatedUser);
    }

    // DELETE
    @DeleteMapping("/{rut}")
    @Operation(summary = "Eliminar un usuario existente.", description = "Elimina un usuario existente en la base de datos segun su rut.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Usuario eliminado correctamente.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "El Usuario no existe en la base de datos o el rut es incorrecto.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al intentar eliminar el usuario.", content = @Content)
    })
    public ResponseEntity<Void> deleteUser(
        @Parameter(description = "Rut del usuario a eliminar.", required = true, example = "20351648-K") @PathVariable String rut) {
        userService.deleteUser(rut);
        return ResponseEntity.noContent().build();
    }


    @PatchMapping("/{rut}/role")
    @PreAuthorize("hasAuthority('ADMIN')") // La Seguridad a Nivel de Método
    @Operation(summary = "Actualizar el rol de un usuario existente.", description = "Actualiza el rol de un usuario existente en la base de datos segun su rut (solo para ADMINs).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rol de usuario actualizado correctamente.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = User.class),
                            examples = @ExampleObject(
                                    name = "Ejemplo de usuario con rol actualizado",
                                    value = "{\"rut\": \"20351648-K\", \"nombres\": \"Miguel Angel\", \"apellidos\": \"Suazo Diaz\", \"fechaNacimiento\": \"2003-03-18\", \"email\": \"an.msuazo@gmail.com\", \"contrasenna\": \"JKdnjewew3$&\", \"role\": \"ADMIN\"}"))),
                @ApiResponse(responseCode = "400", description = "Actualización de rol inválida. Verifique el rut u otros campos proporcionados.", content = @Content),
                @ApiResponse(responseCode = "404", description = "El Usuario no existe en la base de datos o el rut es incorrecto.", content = @Content),
                @ApiResponse(responseCode = "403", description = "Acceso denegado. Solo los administradores pueden cambiar roles de usuarios.", content = @Content),
                @ApiResponse(responseCode = "500", description = "Error interno del servidor al intentar actualizar el rol del usuario.", content = @Content)
        })
    public ResponseEntity<User> updateRole(
            @Parameter(description = "Rut del usuario para modificar su rol.", required = true, example = "20351648-K") @PathVariable String rut,
            @RequestBody UserRoleUpdateRequest request) {

        // Llama al Service para ejecutar la lógica de cambio de rol
        User updatedUser = userService.updateRole(rut, request.newRole());

        // Retorna el resultado (por ejemplo, 200 OK con el usuario actualizado)
        return ResponseEntity.ok(updatedUser);
    }

}
