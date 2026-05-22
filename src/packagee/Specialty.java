/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package packagee;

/**
 *
 * @author edangulo
 */
//TODO LO QUE TENGA AL LADO UN DOBLE SLASH ES UN CAMBIO O CORRECCION DEL CODIGO
public enum Specialty {
    
    GENERAL_MEDICINE,
    CARDIOLOGY,
    PEDIATRICS,
    NEUROLOGY,
    TRAUMATOLOGY_ORTHOPEDICS,
    GYNECOLOGY_OBSTETRICS,
    DERMATOLOGY,
    PSYCHIATRY,
    ONCOLOGY,
    OPHTHALMOLOGY,
    INTERNAL_MEDICINE; //faltaba un ;
    
    public String toDisplayString() {  //
        return this.name().replaceAll("_", " & ")
                         .replace("& ", "")
                         .replace("GENERAL & MEDICINE", "General Medicine")
                         .toLowerCase()
                         .substring(0, 1).toUpperCase()
                         + this.name().replaceAll("_", " ")
                                      .toLowerCase()
                                      .substring(1);
    }

    public static Specialty fromDisplayString(String display) {  //
        return Specialty.valueOf(
            display.toUpperCase()
                   .replaceAll(" & ", "_")
                   .replaceAll(" ", "_")
        );
    }
}
