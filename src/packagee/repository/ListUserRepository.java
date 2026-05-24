package packagee.repository;

import java.util.List;
import packagee.User;
import packagee.observer.DataObserver;

public class ListUserRepository implements UserRepository {

    private List<User> users;
    private java.util.List<DataObserver> observers;

    public ListUserRepository(List<User> users) {
        this.users = users;
        this.observers = new java.util.ArrayList<>();
    }

    @Override
    public List<User> findAll() {
        return users;
    }

    @Override
    public User findByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public User findById(long id) {
        for (User user : users) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }

    @Override
    public void add(User user) {
        users.add(user);
        notifyObservers();
    }

    @Override
    public void addObserver(DataObserver observer) {
        observers.add(observer);
    }

    @Override
    public void notifyObservers() {
        for (DataObserver observer : observers) {
            observer.update();
        }
    }
}
