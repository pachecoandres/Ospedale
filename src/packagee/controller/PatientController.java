package packagee.controller;

import java.time.LocalDate;
import packagee.Patient;
import packagee.dto.PatientDto;
import packagee.repository.UserRepository;
import packagee.validation.UserValidator;

public class PatientController {

    private UserRepository userRepository;
    private UserValidator validator;
    private PatientDto patientDto;

    public PatientController(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.validator = new UserValidator();
        this.patientDto = new PatientDto();
    }

    public ControllerResponse registerPatient(String firstname, String lastname, String idText, String gender,
            String birthdateText, String address, String phoneText, String email, String username,
            String password, String confirmPassword) {
        if (hasEmptyData(firstname, lastname, idText, gender, birthdateText, address, phoneText, email, username, password, confirmPassword)) {
            return new ControllerResponse(400, "Complete todos los campos", "{}");
        }
        if (!validator.isValidIdText(idText)) {
            return new ControllerResponse(400, "El ID debe tener 12 digitos y ser mayor que 0", "{}");
        }

        long id = Long.parseLong(idText);
        if (userRepository.findById(id) != null) {
            return new ControllerResponse(409, "Ya existe un usuario con ese ID", "{}");
        }
        if (userRepository.findByUsername(username) != null) {
            return new ControllerResponse(409, "Ya existe un usuario con ese username", "{}");
        }
        if (!validator.isValidGender(gender)) {
            return new ControllerResponse(400, "Seleccione un genero valido", "{}");
        }
        if (!validator.isValidDate(birthdateText)) {
            return new ControllerResponse(400, "La fecha debe tener formato AAAA-MM-DD", "{}");
        }
        if (!validator.isValidPhone(phoneText)) {
            return new ControllerResponse(400, "El telefono debe tener 10 digitos", "{}");
        }
        if (!validator.isValidEmail(email)) {
            return new ControllerResponse(400, "El email no es valido", "{}");
        }
        if (!password.equals(confirmPassword)) {
            return new ControllerResponse(400, "Las contrasenas no coinciden", "{}");
        }

        Patient patient = new Patient(id, username, firstname, lastname, password, email,
                LocalDate.parse(birthdateText), readGender(gender), Long.parseLong(phoneText), address);
        userRepository.add(patient);
        return new ControllerResponse(201, "Paciente registrado correctamente", patientDto.serialize(patient));
    }

    private boolean hasEmptyData(String... values) {
        for (String value : values) {
            if (validator.isEmpty(value)) {
                return true;
            }
        }
        return false;
    }

    private boolean readGender(String gender) {
        return gender.equals("Female");
    }
}
