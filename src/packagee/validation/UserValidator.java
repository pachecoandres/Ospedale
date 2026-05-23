package packagee.validation;

public class UserValidator {

    public boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    public boolean isValidId(long id) {
        return id > 0 && String.valueOf(id).length() == 12;
    }

    public boolean isValidEmail(String email) {
        return email != null && email.matches("^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,}$");
    }

    public boolean isValidPhone(String phone) {
        return phone != null && phone.matches("\\d{10}");
    }

    public boolean isValidMedicalLicence(String licence) {
        return licence != null && licence.matches("L-\\d{10} MTL");
    }

    public boolean isValidOffice(String office) {
        return office != null && office.matches("O-\\d{3}");
    }
}
