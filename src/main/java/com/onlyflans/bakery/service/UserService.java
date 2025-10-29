package com.onlyflans.bakery.service;

import com.onlyflans.bakery.model.User.DTO.UserCreateRequest;
import com.onlyflans.bakery.model.User.DTO.UserUpdateRequest;
import com.onlyflans.bakery.model.User.User;
import com.onlyflans.bakery.persistence.IUserPersistence;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class UserService {

    /* Es mejor usar final que la anotacion Autowired, debido a su inmutabilidad
    * y a la facilidad de los tests */
    private final IUserPersistence userPersistence;

    public UserService(IUserPersistence userPersistence){
        this.userPersistence = userPersistence;
    }

    public List<User> getAllUsers(){
        return userPersistence.findAll();
    }

    public User getUser(String rut) {
        return userPersistence.findById(rut)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuario con RUT '" + rut + "' no encontrado"
                ));
        /* Evitar el uso de .get() por verificacion redundante y posibles inconsistencias con la basde de datos */
    }

    public User createUser(UserCreateRequest createRequest) {
        if (userPersistence.existsById(createRequest.rut())){
            throw new ResponseStatusException(
                    /* HttpStatus.CONFLICT -> código HTTP 409 */
                    HttpStatus.CONFLICT, "Usuario ya existe"
            );
        }

        // Convertir DTO a Entity
        User user = new User();
        user.setRut(createRequest.rut());
        user.setNombres(createRequest.nombres());
        user.setApellidos(createRequest.apellidos());
        user.setFechaNacimiento(createRequest.fechaNacimiento());
        user.setEmail(createRequest.email());
        user.setContrasenna(createRequest.contrasenna());

        return userPersistence.save(user);
    }

    public User updateUser(String rut, UserUpdateRequest updateRequest) {
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
        if (updateRequest.contrasenna() != null) {
            user.setContrasenna(updateRequest.contrasenna());
        }

        return userPersistence.save(user);
    }

}
