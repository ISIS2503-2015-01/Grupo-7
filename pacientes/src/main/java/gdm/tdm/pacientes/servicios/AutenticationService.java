/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gdm.tdm.pacientes.servicios;

import gdm.tdm.pacientes.persistencia.PersistenceManager;
import gdm.tdm.pacientes.pojos.DTO.UsuarioDTO;
import gdm.tdm.pacientes.pojos.Doctor;
import gdm.tdm.pacientes.pojos.Paciente;
import gdm.tdm.pacientes.seguridad.StormPath;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 *
 * @author nicolas
 */
@Path("/auth/")
public class AutenticationService {
    
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
            if(nuevoUsuario.getGrupo().equals("pacientes")){
                Paciente p = new Paciente();
                Doctor d = new Doctor();
                d.setId(nuevoUsuario.getDoc().getId());
                p.setNombre(nuevoUsuario.getGivenName());
                p.setApellido(nuevoUsuario.getSurName());
                p.setDoc(d);
                try{
                    entityManager.getTransaction().begin();
                    entityManager.persist(p);
                    entityManager.getTransaction().commit();

                }catch(Throwable t){
                    t.printStackTrace();
                    if(entityManager.getTransaction().isActive()){
                        entityManager.getTransaction().rollback();
                    }
                    p = null;
                }finally{
                    entityManager.clear();
                    entityManager.close();
                }
                return Response.status(200).entity("{\"id\":"+p.getId()+"}").build();
            }else{
                Doctor d = new Doctor();
                d.setNombre(nuevoUsuario.getGivenName());
                d.setApellido(nuevoUsuario.getSurName());
                try{
                    entityManager.getTransaction().begin();
                    entityManager.persist(d);
                    entityManager.getTransaction().commit();

                }catch(Throwable t){
                    t.printStackTrace();
                    if(entityManager.getTransaction().isActive()){
                        entityManager.getTransaction().rollback();
                    }
                    d = null;
                }finally{
                    entityManager.clear();
                    entityManager.close();
                }
                return Response.status(200).entity("{\"id\":"+d.getId()+"}").build();
            }
        }else{
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }
        
    }
}
