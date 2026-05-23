package packagee.controller;

import packagee.User;
import packagee.dto.UserDto;
import packagee.repository.UserRepository;
import packagee.validation.UserValidator;

public class AuthController {

    private UserRepository userRepository;
    private UserValidator validator;
    private UserDto userDto;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.validator = new UserValidator();
        this.userDto = new UserDto();
    }

    public ControllerResponse login(String username, String password) {
        if (validator.isEmpty(username) || validator.isEmpty(password)) {
            return new ControllerResponse(400, "Ingrese usuario y contrasena", "{}");
        }

        User user = userRepository.findByUsername(username);
        if (user == null) {
            return new ControllerResponse(404, "El usuario no existe", "{}");
        }

        if (!user.getPassword().equals(password)) {
            return new ControllerResponse(401, "Contrasena incorrecta", "{}");
        }

        return new ControllerResponse(200, "Login correcto", userDto.serialize(user));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
