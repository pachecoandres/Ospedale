package packagee.facade;

import packagee.Appointment;
import packagee.controller.AppointmentController;
import packagee.controller.ControllerResponse;
import packagee.controller.HospitalizationController;
import packagee.controller.PrescriptionController;

public class OspedaleFacade {

    private AppointmentController appointmentController;
    private HospitalizationController hospitalizationController;
    private PrescriptionController prescriptionController;

    public OspedaleFacade(AppointmentController appointmentController,
            HospitalizationController hospitalizationController,
            PrescriptionController prescriptionController) {
        this.appointmentController = appointmentController;
        this.hospitalizationController = hospitalizationController;
        this.prescriptionController = prescriptionController;
    }

    public ControllerResponse acceptAppointment(String appointmentId) {
        return appointmentController.acceptAppointment(appointmentId);
    }

    public ControllerResponse completeAppointment(String appointmentId, String diagnosis,
            String observations, String recommendedTreatment, String followUp) {
        return appointmentController.completeAppointment(appointmentId, diagnosis, observations, recommendedTreatment, followUp);
    }

    public ControllerResponse rescheduleAppointment(String appointmentId, String newTime, String reason) {
        return appointmentController.rescheduleAppointment(appointmentId, newTime, reason);
    }

    public ControllerResponse cancelHospitalization(String hospitalizationId) {
        return hospitalizationController.cancelHospitalization(hospitalizationId);
    }

    public ControllerResponse approveHospitalization(String hospitalizationId) {
        return hospitalizationController.approveHospitalization(hospitalizationId);
    }

    public ControllerResponse requestHospitalization(String patientId, String doctorId,
            String dateText, String reason, String roomType, String observations) {
        return hospitalizationController.requestHospitalization(patientId, doctorId, dateText, reason, roomType, observations);
    }

    public ControllerResponse hospitalizeFromAppointment(Appointment appointment, String reason,
            String roomType, String observations) {
        return hospitalizationController.hospitalizeFromAppointment(appointment, reason, roomType, observations);
    }

    public ControllerResponse listPatientHospitalizations(String patientId) {
        return hospitalizationController.listPatientHospitalizations(patientId);
    }

    public ControllerResponse prescribeMedication(Appointment appointment, String medicationName,
            String doseText, String administrationRoute, String treatmentDurationText,
            String frequencyText, String additionalInstructions) {
        return prescriptionController.prescribeMedication(appointment, medicationName, doseText,
                administrationRoute, treatmentDurationText, frequencyText, additionalInstructions);
    }
}
