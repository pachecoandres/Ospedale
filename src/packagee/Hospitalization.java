/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package packagee;

import java.time.LocalDate;

/**
 *
 * @author edangulo
 */
//TODO LO QUE TENGA AL LADO UN DOBLE SLASH ES UN CAMBIO O CORRECCION DEL CODIGO
public class Hospitalization {
    
    private final String id;
    private final Patient patient;  //
    private Doctor doctor;
    private final LocalDate date;  //

    public String getId() {
        return id;
    }
    
    public Patient getPatient() {  //
        return patient; 
    }
    
    public Doctor getDoctor() {  //
        return doctor; 
    }
    
    public LocalDate getDate() {  //
        return date; 
    }
    
    public String getReason() {  //
        return reason; 
    }
    
    public RoomType getRoomType() {  //
        return roomType; 
    }
    
    public String getObservations() {  //
        return observations; 
    }
    
    public HospitalizationStatus getStatus() {  //
        return status; 
    }
    
    private final String reason;  //
    private final RoomType roomType;  //
    private String observations;
    private HospitalizationStatus status;

    public void setStatus(HospitalizationStatus status) {
        this.status = status;
    }
    
    public void setObservations(String observations) {  //
        this.observations = observations; 
    }

    public Hospitalization(String id, Patient patient, Doctor doctor, LocalDate date, String reason, RoomType roomType, String observations) {
        this.id = id;
        this.patient = patient;
        patient.setHospitalization(this);
        this.doctor = doctor;
        doctor.addHospitalization(this);
        this.date = date;
        this.reason = reason;
        this.roomType = roomType;
        this.observations = observations;
        this.status = HospitalizationStatus.REQUESTED;
    }
    public Hospitalization(String id, Patient patient, Doctor doctor, LocalDate date, String reason, RoomType roomType, String observations, HospitalizationStatus hopsS) {
        this.id = id;
        this.patient = patient;
        patient.setHospitalization(this);
        this.doctor = doctor;
        doctor.addHospitalization(this);
        this.date = date;
        this.reason = reason;
        this.roomType = roomType;
        this.observations = observations;
        this.status = hopsS;  //
    }
    
}
