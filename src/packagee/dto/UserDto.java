package packagee.dto;

import org.json.JSONObject;
import packagee.Administrator;
import packagee.Doctor;
import packagee.Patient;
import packagee.User;

public class UserDto {

    public String serialize(User user) {
        JSONObject json = new JSONObject();
        json.put("id", user.getId());
        json.put("username", user.getUsername());
        json.put("firstname", user.getFirstname());
        json.put("lastname", user.getLastname());
        json.put("type", getType(user));
        return json.toString();
    }

    private String getType(User user) {
        if (user instanceof Administrator) {
            return "admin";
        }
        if (user instanceof Doctor) {
            return "doctor";
        }
        if (user instanceof Patient) {
            return "patient";
        }
        return "user";
    }
}
