/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package packagee.controller;

import packagee.Appointment;
import packagee.AppointmentStatus;
import packagee.Prescription;
import packagee.dto.PrescriptionDto;
import packagee.validation.UserValidator;

/**
 *
 * @author natya
 */
public class PrescriptionController {
    
    private UserValidator validator;
    private PrescriptionDto prescriptionDto;
    public PrescriptionController() {
        this.validator = new UserValidator();
        this.prescriptionDto = new PrescriptionDto();
    }
    
    public ControllerResponse prescribeMedication(Appointment appointment, String medicationName,
            String doseText, String administrationRoute, String treatmentDurationText,
            String frequencyText, String additionalInstructions) {
        if (appointment == null) {
            return new ControllerResponse(400, "La cita no existe", "{}");
        }
        
        if (appointment.getStatus() != AppointmentStatus.PENDING) {
            return new ControllerResponse(400, "Solo se puede preescribir en citas PENDING", "{}");
        }
        
        if (validator.isEmpty(medicationName) || validator.isEmpty(doseText) || 
                validator.isEmpty(administrationRoute) || validator.isEmpty(treatmentDurationText) ||
                validator.isEmpty(frequencyText)) {
            return new ControllerResponse(400, "Complete todos los campos obligatorios", "{}");
        }
        
        double dose;
        try {
            dose = Double.parseDouble(doseText);
            if (dose <= 0) {
                return new ControllerResponse(400, "La dosis debe ser mayor que 0", "{}");
            }
        } catch (NumberFormatException e) {
            return new ControllerResponse(400, "La dosis debe ser un numero valido", "{}");
        }
        
        int treatmentDuration;
        try {
            treatmentDuration = Integer.parseInt(treatmentDurationText);
            if (treatmentDuration <= 0) {
                return new ControllerResponse(400, "La duracion de tratamiento debe ser mayor que 0", "{}");
            }
        } catch (NumberFormatException e) {
            return new ControllerResponse(400, "La duración del tratamiento debe ser un numero valido", "{}");
        }
        
        int frequency;
        try {
            frequency = Integer.parseInt(frequencyText);
            if (frequency <= 0) {
                return new ControllerResponse(400, "La frecuencia debe ser mayor que 0", "{}");
            }
        } catch (NumberFormatException e) {
            return new ControllerResponse(400, "La frecuencia debe ser un numero valido", "{}");
        }
        
        if (additionalInstructions == null){
            additionalInstructions = "";
        }
        
        Prescription prescription = new Prescription(appointment, medicationName, 
                dose, administrationRoute, treatmentDuration, 
                additionalInstructions, frequency);
        appointment.addPrescription(prescription);
        
    return new ControllerResponse(201, "Medicamento preescrito correctamente", prescriptionDto.serialize(prescription));
    
    }
    
}
