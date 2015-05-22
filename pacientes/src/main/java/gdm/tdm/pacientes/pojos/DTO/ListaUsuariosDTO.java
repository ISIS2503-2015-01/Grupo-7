/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gdm.tdm.pacientes.pojos.DTO;

import java.util.List;

/**
 *
 * @author nicolas
 */
public class ListaUsuariosDTO {
    private List<UsuarioDTO> usuarios;
    
    public ListaUsuariosDTO(){
        
    }
    
    public ListaUsuariosDTO(List<UsuarioDTO> d){
        usuarios = d;
    }

    /**
     * @return the usuarios
     */
    public List<UsuarioDTO> getUsuarios() {
        return usuarios;
    }

    /**
     * @param usuarios the usuarios to set
     */
    public void setUsuarios(List<UsuarioDTO> usuarios) {
        this.usuarios = usuarios;
    }
    
}
