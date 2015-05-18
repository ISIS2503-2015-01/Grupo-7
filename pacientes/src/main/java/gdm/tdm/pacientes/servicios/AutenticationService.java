/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gdm.tdm.pacientes.servicios;

import gdm.tdm.pacientes.pojos.DTO.UsuarioDTO;
import gdm.tdm.pacientes.seguridad.StormPath;
import javax.servlet.http.Cookie;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

/**
 *
 * @author nicolas
 */
@Path("/auth/")
public class AutenticationService {
    
    @POST
    @Path("login")
    public Response autenticar(UsuarioDTO usuario){
        StormPath sp = new StormPath();
        if(sp.autenticar(usuario)){
            return Response.status(Response.Status.OK).header("autenticado", "true").build();
        }else{
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }
    
    @POST
    @Path("registro")
    public Response registrarUsuario(UsuarioDTO nuevoUsuario){
        StormPath sp = new StormPath();
        if(sp.registrar(nuevoUsuario)){
            return Response.status(Response.Status.OK).build();
        }else{
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }
        
    }
}
