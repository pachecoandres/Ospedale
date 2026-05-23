package packagee.dto;

import org.json.JSONObject;
import packagee.Patient;

public class PatientDto {

    public String serialize(Patient patient) {
        JSONObject json = new JSONObject();
        json.put("id", patient.getId());
        json.put("username", patient.getUsername());
        json.put("firstname", patient.getFirstname());
        json.put("lastname", patient.getLastname());
        json.put("email", patient.getEmail());
        json.put("birthdate", patient.getBirthdate().toString());
        json.put("gender", patient.isGender() ? "Female" : "Male");
        json.put("phone", patient.getPhone());
        json.put("address", patient.getAddress());
        return json.toString();
    }
}
