package packagee.dto;

import org.json.JSONObject;
import packagee.Doctor;

public class DoctorDto {

    public String serialize(Doctor doctor) {
        JSONObject json = new JSONObject();
        json.put("id", doctor.getId());
        json.put("username", doctor.getUsername());
        json.put("firstname", doctor.getFirstname());
        json.put("lastname", doctor.getLastname());
        json.put("specialty", doctor.getSpecialty().name());
        json.put("licenceNumber", doctor.getLicenceNumber());
        json.put("assignedOffice", doctor.getAssignedOffice());
        return json.toString();
    }
}
