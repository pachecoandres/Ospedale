package packagee.strategy;

import java.time.LocalDateTime;
import packagee.Doctor;
import packagee.repository.UserRepository;

public interface DoctorSearchStrategy {

    Doctor findDoctor(UserRepository userRepository, String value, LocalDateTime datetime);
}
