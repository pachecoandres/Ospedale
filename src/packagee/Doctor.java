/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package packagee;

import java.util.ArrayList;
import java.util.Comparator;         //
import java.util.List;               //
import java.util.stream.Collectors;  //

/**
 *
 * @author edangulo
 */

//TODO LO QUE TENGA AL LADO UN DOBLE SLASH ES UN CAMBIO O CORRECCION DEL CODIGO
public class Doctor extends User {
    
    private Specialty specialty;
    private String licenceNumber;
    private String assignedOffice;
    private ArrayList<Appointment> appointments;
    private ArrayList<Hospitalization> hospitalizations;

    public Doctor(long id, String username, String firstname, String lastname, String password, Specialty specialty, String licenceNumber, String assignedOffice) {
        super(id, username, firstname, lastname, password);
        hospitalizations = new ArrayList<>();
        this.specialty = specialty;
        this.licenceNumber = licenceNumber;
        this.assignedOffice = assignedOffice;
        this.appointments = new ArrayList<>();  //
        this.hospitalizations = new ArrayList<>();  //
    }
    
    public String getLicenceNumber() {  //
        return licenceNumber; 
    }
    
    public String getAssignedOffice() {  //
        return assignedOffice; 
    }

    public List<Appointment> getAppointments() {
        appointments.sort(Comparator.comparing(Appointment::getDatetime).reversed());  //
        return appointments;
    }

    public Specialty getSpecialty() {
        return specialty;
    }
    
    public boolean addHospitalization(Hospitalization hospitalization){
        return hospitalizations.add(hospitalization);
    }

    public void setSpecialty(Specialty specialty) {
        this.specialty = specialty;
    }

    public void setLicenceNumber(String licenceNumber) {
        this.licenceNumber = licenceNumber;
    }

    public void setAssignedOffice(String assignedOffice) {
        this.assignedOffice = assignedOffice;
    }
    
    public List<Appointment> getPendingAppointments() {  //
        return appointments.stream()
                .filter(a -> a.getStatus() == AppointmentStatus.PENDING)
                .sorted(Comparator.comparing(Appointment::getDatetime).reversed())
                .collect(Collectors.toList());
    }
    
    public List<Hospitalization> getHospitalizations() {  //
        return hospitalizations; 
    }
    
    public void addAppointment(Appointment appointment) {  //
        this.appointments.add(appointment);
    }
    
    public boolean isAvailable(java.time.LocalDateTime datetime) {  //
        for (Appointment a : appointments) {
            if (a.getStatus() == AppointmentStatus.CANCELED) continue;
            long diff = Math.abs(java.time.Duration.between(a.getDatetime(), datetime).toMinutes());
            if (diff < 15) return false;
        }
        return true;
    }
}
