package packagee.repository;

import java.util.List;
import packagee.User;
import packagee.observer.DataObserver;

public interface UserRepository {

    List<User> findAll();

    User findByUsername(String username);

    User findById(long id);

    void add(User user);

    void addObserver(DataObserver observer);

    void notifyObservers();
}
