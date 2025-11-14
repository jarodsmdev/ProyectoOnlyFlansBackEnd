package com.onlyflans.bakery.service;

import com.onlyflans.bakery.model.UserRole;
import com.onlyflans.bakery.model.dto.request.UserCreateRequest;
import com.onlyflans.bakery.model.dto.request.UserUpdateRequest;
import com.onlyflans.bakery.model.User;
import com.onlyflans.bakery.model.dto.response.UserDTO;
import com.onlyflans.bakery.model.mapper.UserMapper;
import com.onlyflans.bakery.persistence.IUserPersistence;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class UserService {

    /* Es mejor usar final que la anotacion Autowired, debido a su inmutabilidad
    * y a la facilidad de los tests */
    private final IUserPersistence userPersistence;
    private final PasswordEncoder passwordEncoder;

    public UserService(IUserPersistence userPersistence, PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
        this.userPersistence = userPersistence;
    }

    public List<UserDTO> getAllUsers(){
        return userPersistence.findAll()
                .stream()
                .map(UserMapper::toDTO)
                .toList();
    }

    public UserDTO getUser(String rut) {
        User user = userPersistence.findById(rut)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuario con RUT '" + rut + "' no encontrado"
                ));

        return UserMapper.toDTO(user);

        /* Evitar el uso de .get() por verificacion redundante y posibles inconsistencias con la basde de datos */
    }

    public UserDTO createUser(UserCreateRequest createRequest) {
        if (userPersistence.existsById(createRequest.rut())){
            throw new ResponseStatusException(
                    /* HttpStatus.CONFLICT -> código HTTP 409 */
                    HttpStatus.CONFLICT, "Usuario ya existe"
            );
        }

        // Convertir request a Entity
        User user = new User();
        user.setRut(createRequest.rut());
        user.setNombres(createRequest.nombres());
        user.setApellidos(createRequest.apellidos());
        user.setFechaNacimiento(createRequest.fechaNacimiento());
        user.setEmail(createRequest.email());

        // Asignar el rol como "NORMAL" por defecto
        user.setUserRole(UserRole.NORMAL);

        // Se encripta la contraseña
        String contrasennaEncriptada = passwordEncoder.encode(createRequest.contrasenna());
        user.setContrasenna(contrasennaEncriptada);

        //GUARDAR
        userPersistence.save(user);

        //Ahora que se guarda, devolver el DTO
        return UserMapper.toDTO(user);
    }

    public UserDTO updateUser(String rut, UserUpdateRequest updateRequest) {
        User user = userPersistence.findById(rut)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuario no encontrado"
                ));

        // Actualizar SOLO los campos que vienen en el request
        if (updateRequest.nombres() != null) {
            user.setNombres(updateRequest.nombres());
        }
        if (updateRequest.apellidos() != null) {
            user.setApellidos(updateRequest.apellidos());
        }
        if (updateRequest.fechaNacimiento() != null) {
            user.setFechaNacimiento(updateRequest.fechaNacimiento());
        }
        if (updateRequest.email() != null) {
            user.setEmail(updateRequest.email());
        }

        // Encriptar la nueva contraseña
        if (updateRequest.contrasenna() != null) {
            String contrasennaEncriptada = passwordEncoder.encode(updateRequest.contrasenna());
            user.setContrasenna(contrasennaEncriptada);
        }

        //GUARDAR
        userPersistence.save(user);

        //Devolver el DTO actualizado
        return UserMapper.toDTO(user);
    }

    public void deleteUser(String rut) {
        User user = userPersistence.findById(rut)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuario no encontrado"
                ));

        userPersistence.delete(user);
    }

    // verificar login
    public UserDTO checkCredentials(String rut, String contrasenaPlana){
        User user = userPersistence.findById(rut)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        if (!passwordEncoder.matches(contrasenaPlana, user.getContrasenna())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas");
        }

        return UserMapper.toDTO(user);
    }

    /**
     * Método exclusivo para cambiar el rol de un usuario.
     * Requiere que el metodo sea autenticado con ADMIN.
     */
    public UserDTO updateRole(String rut, UserRole newRole) {
        User user = userPersistence.findById(rut)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuario no encontrado"
                ));

        user.setUserRole(newRole); // Modificar el rol

        //GUARDAR
        userPersistence.save(user);

        return UserMapper.toDTO(user);
    }

}
