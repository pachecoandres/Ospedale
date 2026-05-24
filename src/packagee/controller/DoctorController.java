package packagee.controller;

import packagee.Doctor;
import packagee.Specialty;
import packagee.dto.DoctorDto;
import packagee.repository.UserRepository;
import packagee.validation.UserValidator;

public class DoctorController {

    private UserRepository userRepository;
    private UserValidator validator;
    private DoctorDto doctorDto;

    public DoctorController(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.validator = new UserValidator();
        this.doctorDto = new DoctorDto();
    }

    public ControllerResponse registerDoctor(String firstname, String lastname, String idText, String specialtyText,
            String licenceNumber, String assignedOffice, String username, String password, String confirmPassword) {
        if (hasEmptyData(firstname, lastname, idText, specialtyText, licenceNumber, assignedOffice, username, password, confirmPassword)) {
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
        if (!validator.isValidSpecialty(specialtyText)) {
            return new ControllerResponse(400, "Seleccione una especialidad valida", "{}");
        }
        if (!validator.isValidMedicalLicence(licenceNumber)) {
            return new ControllerResponse(400, "La licencia debe tener formato L-XXXXXXXXXX MTL", "{}");
        }
        if (!validator.isValidOffice(assignedOffice)) {
            return new ControllerResponse(400, "La oficina debe tener formato O-XXX", "{}");
        }
        if (!password.equals(confirmPassword)) {
            return new ControllerResponse(400, "Las contrasenas no coinciden", "{}");
        }

        Specialty specialty = Specialty.fromDisplayString(specialtyText);
        Doctor doctor = new Doctor(id, username, firstname, lastname, password, specialty, licenceNumber, assignedOffice);
        userRepository.add(doctor);
        return new ControllerResponse(201, "Doctor registrado correctamente", doctorDto.serialize(doctor));
    }

    private boolean hasEmptyData(String... values) {
        for (String value : values) {
            if (validator.isEmpty(value)) {
                return true;
            }
        }
        return false;
    }
    
    public ControllerResponse updateDoctor(String idText, String firstname, String lastname, 
            String specialtyText, String licenceNumber, String assignedOffice, String username, 
            String password, String confirmPassword){
        
        if (hasEmptyData(firstname, lastname, idText, licenceNumber, assignedOffice, username, password, confirmPassword)){
            return new ControllerResponse(400, "Complete todos los campos", "{}");
        }
        
        if (!validator.isValidIdText(idText)){
            return new ControllerResponse(400, "El ID debe tener 12 digitos y ser mayor que 0", "{}");    
        }
        
        long id = Long.parseLong(idText);
        packagee.User user = userRepository.findById(id);
        
        if (!(user instanceof Doctor)) {
            return new ControllerResponse(409, "El doctor no existe", "{}");
        }
        Doctor doctor = (Doctor) user;
        
        packagee.User userWithUsername = userRepository.findByUsername(username);
        if (userWithUsername != null && userWithUsername.getId() != id) {
            return new ControllerResponse(409, "Ya existe un usuario con ese username", "{}");
        }
        
        if (!validator.isValidSpecialty(specialtyText)) {
            return new ControllerResponse(400, "Seleccione una especialidad valida", "{}");
        }
        
        if (!validator.isValidMedicalLicence(licenceNumber)) {
            return new ControllerResponse(400, "La licencia debe tener fromato L-XXXXXXXXXX MTL", "{}");
        }
        
        if (!validator.isValidOffice(assignedOffice)) {
            return new ControllerResponse(400, "La oficina debe tener formato 0-XXX", "{}");
        }
        
        if (!password.equals(confirmPassword)) {
            return new ControllerResponse(400, "Las contraseñas no coinciden", "{}");
        }
        
        Specialty specialty = Specialty.fromDisplayString(specialtyText);
        doctor.setUsername(username);
        doctor.setFirstname(firstname);
        doctor.setLastname(lastname);
        doctor.setPassword(password);
        doctor.setSpecialty(specialty);
        doctor.setLicenceNumber(licenceNumber);
        doctor.setAssignedOffice(assignedOffice);
        
        userRepository.notifyObservers();
        
        return new ControllerResponse(200, "Doctor actualizado correctamente", doctorDto.serialize(doctor));
    }
}
