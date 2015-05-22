/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gdm.tdm.pacientes.servicios;

import com.stormpath.sdk.account.Account;
import com.stormpath.sdk.directory.CustomData;
import com.stormpath.sdk.group.GroupMembership;
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
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
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
    @Produces(MediaType.APPLICATION_JSON)
    public Response autenticar(UsuarioDTO usuario){
        StormPath sp = new StormPath();
        Account respuesta = sp.autenticar(usuario);
        if(respuesta!=null){
            Long l = Long.valueOf(((Integer)respuesta.getCustomData().get("id")).longValue()) ;
            String grupo = ((GroupMembership)respuesta.getGroupMemberships().iterator().next()).getGroup().getName();
            if(grupo.equals("pacientes")){
                //Query q = entityManager.createQuery("SELECT u from Paciente u WHERE u.id = :id");
                //q.setParameter("id", l);
                //Paciente p = (Paciente) q.getSingleResult();
                UsuarioDTO ddd= new UsuarioDTO();
                ddd.setGrupo("pacientes");
                ddd.setId(l);
                return Response.status(Response.Status.OK).header("autenticado", "true").entity(ddd).build();
            }else{
                UsuarioDTO ddd= new UsuarioDTO();
                ddd.setGrupo("doctores");
                ddd.setId(l);
                return Response.status(Response.Status.OK).header("autenticado", "true").entity(ddd).build();
            }
        }else{
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }
    
    @POST
    @Path("registro")
    @Produces(MediaType.APPLICATION_JSON)
    public Response registrarUsuario(UsuarioDTO nuevoUsuario){
        StormPath sp = new StormPath();
        Account ac = sp.registrar(nuevoUsuario);
        if(ac != null){
            if(nuevoUsuario.getGrupo().equals("pacientes")){
                Paciente p = new Paciente();
                Doctor d = new Doctor();
                d.setId(nuevoUsuario.getDoc().getId());
                p.setNombre(nuevoUsuario.getGivenName());
                p.setEmail(nuevoUsuario.getEmail());
                p.setClave(nuevoUsuario.getPassword());
                p.setApellido(nuevoUsuario.getSurName());
                p.setCedula(nuevoUsuario.getCedula());
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
                Long ff = -1L;
                if(p!= null)
                    ff = p.getId();
                nuevoUsuario.setId(ff);
                CustomData cd = ac.getCustomData();
                if(!ff.equals( -1L))
                    cd.put("id", ff);
                cd.save();
                return Response.status(200).entity(nuevoUsuario).build();
            }else{
                Doctor d = new Doctor();
                d.setNombre(nuevoUsuario.getGivenName());
                d.setApellido(nuevoUsuario.getSurName());
                d.setEmail(nuevoUsuario.getEmail());
                d.setClave(nuevoUsuario.getPassword());
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
                Long ff = -1L;
                if(d!= null)
                    ff=d.getId();
                nuevoUsuario.setId(ff);
                CustomData cd = ac.getCustomData();
                if(!ff.equals( -1L))
                    cd.put("id", ff);
                cd.save();
                return Response.status(200).entity(nuevoUsuario).build();
            }
        }else{
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }
        
    }
}
