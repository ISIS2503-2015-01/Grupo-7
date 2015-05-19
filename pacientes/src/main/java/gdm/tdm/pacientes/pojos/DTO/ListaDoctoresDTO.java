/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gdm.tdm.pacientes.pojos.DTO;

import gdm.tdm.pacientes.pojos.Doctor;
import java.util.List;

/**
 *
 * @author nicolas
 */
public class ListaDoctoresDTO {
    private List<DoctorDTO> doctotres;

    public ListaDoctoresDTO() {
    }
    
    
    
    public ListaDoctoresDTO(List<DoctorDTO> ds){
        doctotres = ds;
    }

    /**
     * @return the doctotres
     */
    public List<DoctorDTO> getDoctotres() {
        return doctotres;
    }

    /**
     * @param doctotres the doctotres to set
     */
    public void setDoctotres(List<DoctorDTO> doctotres) {
        this.doctotres = doctotres;
    }
    
}
