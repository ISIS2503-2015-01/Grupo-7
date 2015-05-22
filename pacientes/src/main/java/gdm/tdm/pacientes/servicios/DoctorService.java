/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gdm.tdm.pacientes.servicios;

import gdm.tdm.pacientes.persistencia.PersistenceManager;
import gdm.tdm.pacientes.pojos.DTO.DoctorDTO;
import gdm.tdm.pacientes.pojos.DTO.ListaDoctoresDTO;
import gdm.tdm.pacientes.pojos.DTO.ListaUsuariosDTO;
import gdm.tdm.pacientes.pojos.DTO.UsuarioDTO;
import gdm.tdm.pacientes.pojos.Doctor;
import gdm.tdm.pacientes.pojos.Paciente;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author nicolas
 */
@Path("/doctores/")
public class DoctorService {
    @PersistenceContext(unitName = "PacientesPu")
    EntityManager entityManager;
    
    @PostConstruct
    public void init(){
        try{
            entityManager = PersistenceManager.getInstance().getEntityManagerFactory().createEntityManager();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response darDoctores(){
        
        Query q = entityManager.createQuery("select u from Doctor u");
        List<Doctor> ds = q.getResultList();
        List<DoctorDTO> lds = new ArrayList<>();
        for(Doctor d: ds){
            lds.add(new DoctorDTO(d));
        }
        Response r = null;
        try{
            r = Response.status(Response.Status.OK).entity(new ListaDoctoresDTO(lds)).build();
        }catch(Exception e){
            e.printStackTrace();
            return Response.status(500).build();
        }
        return r;
    }
    
    @GET
    @Path("{id}/pacientes")
    @Produces(MediaType.APPLICATION_JSON)
    public Response darListaPacientes(@PathParam("id") Long id){
        Query q = entityManager.createQuery("SELECT u from Paciente u WHERE u.doc.id = :id");
        q.setParameter("id", id);
        List<Paciente> pacientes = q.getResultList();
        List<UsuarioDTO> respuesta = new ArrayList<>();
        for(Paciente p: pacientes){
            UsuarioDTO d = new UsuarioDTO();
            d.setGivenName(p.getNombre());
            d.setSurName(p.getApellido());
            d.setId(p.getId());
            d.setCedula(p.getCedula());
            respuesta.add(d);
        }
        return Response.status(Response.Status.OK).entity(new ListaUsuariosDTO(respuesta)).build();
    }
}
