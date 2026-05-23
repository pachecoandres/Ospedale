package packagee.repository;

import java.util.List;
import packagee.User;

public interface UserRepository {

    List<User> findAll();

    User findByUsername(String username);

    User findById(long id);

    void add(User user);
}
