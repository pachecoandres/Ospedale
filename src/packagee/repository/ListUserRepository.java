package packagee.repository;

import java.util.List;
import packagee.User;

public class ListUserRepository implements UserRepository {

    private List<User> users;

    public ListUserRepository(List<User> users) {
        this.users = users;
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
    }
}
