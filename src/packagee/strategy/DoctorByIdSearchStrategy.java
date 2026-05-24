package packagee.strategy;

import java.time.LocalDateTime;
import packagee.Doctor;
import packagee.User;
import packagee.repository.UserRepository;
import packagee.validation.UserValidator;

public class DoctorByIdSearchStrategy implements DoctorSearchStrategy {

    private UserValidator validator;

    public DoctorByIdSearchStrategy() {
        this.validator = new UserValidator();
    }

    @Override
    public Doctor findDoctor(UserRepository userRepository, String value, LocalDateTime datetime) {
        if (!validator.isValidIdText(value)) {
            return null;
        }

        User user = userRepository.findById(Long.parseLong(value));
        if (user instanceof Doctor) {
            Doctor doctor = (Doctor) user;
            if (doctor.isAvailable(datetime)) {
                return doctor;
            }
        }
        return null;
    }
}
