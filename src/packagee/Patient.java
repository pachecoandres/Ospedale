/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package packagee;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;  //
import java.util.List;        //

/**
 *
 * @author edangulo
 */

//TODO LO QUE TENGA AL LADO UN DOBLE SLASH ES UN CAMBIO O CORRECCION DEL CODIGO
public class Patient extends User {
    
    private String email;
    private LocalDate birthdate;
    private boolean gender;
    private long phone;
    private String address;
    private ArrayList<Appointment> appointments;
    private Hospitalization hospitalization;

    public String getEmail() {  //
        return email; 
    }
    
    public LocalDate getBirthdate() {  //
        return birthdate; 
    }

    public boolean isGender() {  //
        return gender; 
    }

    public long getPhone() {  //
        return phone; 
    }

    public String getAddress() {  //
        return address; 
    }

    public Hospitalization getHospitalization() {  //
        return hospitalization; 
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setHospitalization(Hospitalization hospitalization) {
        this.hospitalization = hospitalization;
    }

    public List<Appointment> getAppointments() {
        appointments.sort(Comparator.comparing(Appointment::getDatetime).reversed());  //
        return appointments;
    }
    
    public void addAppointment(Appointment appointment) {  //
        this.appointments.add(appointment);  //
    }

    public Patient(long id, String username, String firstname, String lastname, String password, String email, LocalDate birthdate, boolean gender, long phone, String address) {
        super(id, username, firstname, lastname, password);
        this.email = email;
        this.birthdate = birthdate;
        this.gender = gender;
        this.phone = phone;
        this.address = address;
        this.appointments = new ArrayList<>();
    }
    
}
