package packagee.repository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import packagee.Administrator;
import packagee.Doctor;
import packagee.Patient;
import packagee.Specialty;
import packagee.User;

public class JsonUserRepository implements UserRepository {

    private String filePath;
    private List<User> users;

    public JsonUserRepository(String filePath) {
        this.filePath = filePath;
        this.users = new ArrayList<>();
        loadUsers();
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

    private void loadUsers() {
        try {
            String content = Files.readString(Path.of(filePath));
            JSONArray array = new JSONObject(content).getJSONArray("users");
            for (int i = 0; i < array.length(); i++) {
                users.add(createUser(array.getJSONObject(i)));
            }
        } catch (Exception e) {
            users = new ArrayList<>();
        }
    }

    private User createUser(JSONObject json) {
        String type = json.getString("type");
        long id = json.getLong("id");
        String username = json.getString("username");
        String firstname = json.getString("firstname");
        String lastname = json.getString("lastname");
        String password = json.getString("password");

        if (type.equals("admin")) {
            return new Administrator(id, username, firstname, lastname, password);
        }
        if (type.equals("doctor")) {
            Specialty specialty = readSpecialty(json.getString("specialty"));
            String licenceNumber = json.getString("licenceNumber");
            String assignedOffice = json.getString("assignedOffice");
            return new Doctor(id, username, firstname, lastname, password, specialty, licenceNumber, assignedOffice);
        }

        String email = json.getString("email");
        LocalDate birthdate = LocalDate.parse(json.getString("birthdate"));
        boolean gender = json.getBoolean("gender");
        long phone = json.getLong("phone");
        String address = json.getString("address");
        return new Patient(id, username, firstname, lastname, password, email, birthdate, gender, phone, address);
    }

    private Specialty readSpecialty(String value) {
        if (value.equals("ORTHOPEDICS")) {
            return Specialty.TRAUMATOLOGY_ORTHOPEDICS;
        }
        return Specialty.valueOf(value);
    }
}
