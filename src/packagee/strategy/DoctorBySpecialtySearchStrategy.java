package packagee.strategy;

import java.time.LocalDateTime;
import packagee.Doctor;
import packagee.Specialty;
import packagee.User;
import packagee.repository.UserRepository;
import packagee.validation.UserValidator;

public class DoctorBySpecialtySearchStrategy implements DoctorSearchStrategy {

    private UserValidator validator;

    public DoctorBySpecialtySearchStrategy() {
        this.validator = new UserValidator();
    }

    @Override
    public Doctor findDoctor(UserRepository userRepository, String value, LocalDateTime datetime) {
        if (!validator.isValidSpecialty(value)) {
            return null;
        }

        Specialty specialty = Specialty.fromDisplayString(value);
        for (User user : userRepository.findAll()) {
            if (user instanceof Doctor) {
                Doctor doctor = (Doctor) user;
                if (doctor.getSpecialty() == specialty && doctor.isAvailable(datetime)) {
                    return doctor;
                }
            }
        }
        return null;
    }
}
