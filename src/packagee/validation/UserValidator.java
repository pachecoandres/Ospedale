package packagee.validation;

import java.time.LocalDate;
import packagee.Specialty;

public class UserValidator {

    public boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    public boolean isValidId(long id) {
        return id > 0 && String.valueOf(id).length() == 12;
    }

    public boolean isValidIdText(String idText) {
        return idText != null && idText.matches("[1-9]\\d{11}");
    }

    public boolean isValidEmail(String email) {
        return email != null && email.matches("^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,}$");
    }

    public boolean isValidPhone(String phone) {
        return phone != null && phone.matches("\\d{10}");
    }

    public boolean isValidDate(String date) {
        try {
            if (date == null || !date.matches("\\d{4}-\\d{2}-\\d{2}")) {
                return false;
            }
            LocalDate.parse(date);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isValidGender(String gender) {
        return gender != null && (gender.equals("Female") || gender.equals("Male"));
    }

    public boolean isValidMedicalLicence(String licence) {
        return licence != null && licence.matches("L-\\d{10} MTL");
    }

    public boolean isValidOffice(String office) {
        return office != null && office.matches("O-\\d{3}");
    }

    public boolean isValidSpecialty(String specialty) {
        try {
            if (isEmpty(specialty) || specialty.equals("Select one")) {
                return false;
            }
            Specialty.fromDisplayString(specialty);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
