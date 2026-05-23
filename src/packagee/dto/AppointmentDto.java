package packagee.dto;

import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import packagee.Appointment;

public class AppointmentDto {

    public String serialize(Appointment appointment) {
        JSONObject json = new JSONObject();
        json.put("id", appointment.getId());
        json.put("patientId", appointment.getPatient().getId());
        json.put("patientName", appointment.getPatient().getFirstname() + " " + appointment.getPatient().getLastname());
        json.put("doctorId", appointment.getDoctor().getId());
        json.put("doctorName", appointment.getDoctor().getFirstname() + " " + appointment.getDoctor().getLastname());
        json.put("specialty", appointment.getSpecialty().name());
        json.put("datetime", appointment.getDatetime().toString());
        json.put("type", appointment.isType() ? "In-person" : "Remote");
        json.put("reason", appointment.getReason());
        json.put("status", appointment.getStatus().name());
        return json.toString();
    }

    public String serializeList(List<Appointment> appointments) {
        JSONArray array = new JSONArray();
        for (Appointment appointment : appointments) {
            array.put(new JSONObject(serialize(appointment)));
        }
        return array.toString();
    }
}
