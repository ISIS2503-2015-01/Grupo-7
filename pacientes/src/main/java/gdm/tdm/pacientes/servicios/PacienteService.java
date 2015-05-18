package gdm.tdm.pacientes.servicios;

import gdm.tdm.pacientes.persistencia.PersistenceManager;
import gdm.tdm.pacientes.pojos.Causa;
import gdm.tdm.pacientes.pojos.DTO.AlarmaDTO;
import gdm.tdm.pacientes.pojos.DTO.EpisodioDTO;
import gdm.tdm.pacientes.pojos.DTO.ListaAlarmasDTO;
import gdm.tdm.pacientes.pojos.DTO.ListaCausasDTO;
import gdm.tdm.pacientes.pojos.DTO.ListaEpisodiosDTO;
import gdm.tdm.pacientes.pojos.DTO.ListaMedicamentosDTO;
import gdm.tdm.pacientes.pojos.DTO.RespuestaDTO;
import gdm.tdm.pacientes.pojos.Episodio;
import gdm.tdm.pacientes.pojos.Medicamento;
import gdm.tdm.pacientes.pojos.Paciente;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Root resource (exposed at "paciente" path)
 */
@Path("/paciente/")
public class PacienteService {

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
    
    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
        return "Hello, desde Paciente";
    }
    
    /*
     *Metodo para crear pacientes
    */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response crearPaciente(Paciente nuevo){
        try{
            entityManager.getTransaction().begin();
            entityManager.persist(nuevo);
            entityManager.getTransaction().commit();
            
        }catch(Throwable t){
            t.printStackTrace();
            if(entityManager.getTransaction().isActive()){
                entityManager.getTransaction().rollback();
            }
            nuevo = null;
        }finally{
            entityManager.clear();
            entityManager.close();
        }
        return Response.status(200).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE, OPTIONS").header("Access-Control-Allow-Headers", "Content-Type, Content-Range, Content-Disposition, Content-Description").entity("{\"id\":"+nuevo.getId()+"}").build();
    }
    
    /*
     *Metodo para retornar la lista de causas de un paciente
    */
    @GET
    @Path("{id}/causa")
    @Produces(MediaType.APPLICATION_JSON)
    public Response darCausasPaciente(@PathParam("id") String id){
        Response r;
        try{
            Long pacienteID = Long.parseLong(id);
            Query query = entityManager.createNativeQuery("select id, nombre from Causa where paciente_id = ?1").setParameter(1, pacienteID);
            List<Object[]> rawPaciente = query.getResultList();
            List<RespuestaDTO> causasPaciente = new LinkedList<>();
            for(Object[] f: rawPaciente){
                RespuestaDTO resDTO = new RespuestaDTO();
                resDTO.setId((Long)f[0]);
                resDTO.setNombre((String)f[1]);
                causasPaciente.add(resDTO);
            }
            ListaCausasDTO respuesta = new ListaCausasDTO(causasPaciente);
            r = Response.status(200).header("Access-Control-Allow-Origin", "*").entity(respuesta).build();
        }catch(Exception e){
            r= Response.status(400).header("Access-Control-Allow-Origin", "*").build();
        }
        return r;
    }
    /*
     *Metodo para crear causas asociadas un paciente
    */
    @POST
    @Path("{id}/causa")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response nuevaCausa(@PathParam("id") String id,Causa nuevo){
        try{
            Long idPaciente = Long.parseLong(id);
            
            entityManager.getTransaction().begin();
            Paciente p = new Paciente();
            p.setId(Long.parseLong(id));
            nuevo.setPaciente(p);
            entityManager.persist(nuevo);
            entityManager.getTransaction().commit();
            
        }catch(Throwable t){
            if(entityManager.getTransaction().isActive()){
                entityManager.getTransaction().rollback();
            }
            nuevo = null;
        }finally{
            entityManager.clear();
            entityManager.close();
        }
        return Response.status(200).header("Access-Control-Allow-Origin", "*").entity("{\"id\":"+nuevo.getId()+"}").build();
    }
    
    /*
     *Metodo para generar reportes de la informcion asociada a las causas
    */
    @GET
    @Path("{id}/reporteCausas")
    @Produces(MediaType.APPLICATION_JSON)
    public Response darReporteCausas(@PathParam("id") String id){
        String query = "select c.nombre, COUNT(c.id) from episodio_causa ec inner join causa c on ec.causas_id = c.id GROUP BY c.id HAVING c.paciente_id = ?1";
        Query q = entityManager.createNativeQuery(query);
        q.setParameter(1, Long.parseLong(id));
        List<Object[]> resultados = q.getResultList();
        List<AlarmaDTO> listaAlarmas = new LinkedList<>();
        for(Object[] l : resultados){
            AlarmaDTO dto = new AlarmaDTO((String)l[0], (Long)l[1]);
            listaAlarmas.add(dto);
        }
        return Response.status(200).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE, OPTIONS").header("Access-Control-Allow-Headers", "Content-Type, Content-Range, Content-Disposition, Content-Description").entity(new ListaAlarmasDTO(listaAlarmas)).build();
    }
    
    /*
     *Metodo para crear medicamentos de un paciente
    */
    @POST
    @Path("{id}/medicamento")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response nuevoMedicamento(@PathParam("id") String id,Medicamento nuevo){
        try{
            Paciente p = new Paciente();
            p.setId(Long.parseLong(id));
            nuevo.setPaciente(p);
            
            entityManager.getTransaction().begin();
            entityManager.persist(nuevo);
            entityManager.getTransaction().commit();
            
        }catch(Throwable t){
            if(entityManager.getTransaction().isActive()){
                entityManager.getTransaction().rollback();
            }
            nuevo = null;
        }finally{
            entityManager.clear();
            entityManager.close();
        }
        return Response.status(200).header("Access-Control-Allow-Origin", "*").entity("{\"id\":"+nuevo.getId()+"}").build();
    }
    
    /*
     *Metodo para obtener los medicamentos de un paciente
    */
    @GET
    @Path("{id}/medicamento")
    @Produces(MediaType.APPLICATION_JSON)
    public Response darMedicamentoPaciente(@PathParam("id") String id){
        Response r;
        try{
            Long pacienteID = Long.parseLong(id);
            Query query = entityManager.createNativeQuery("select id, nombre from medicamento where paciente_id = ?1").setParameter(1, pacienteID);
            List<Object[]> rawPaciente = query.getResultList();
            List<RespuestaDTO> causasPaciente = new LinkedList<>();
            for(Object[] f: rawPaciente){
                RespuestaDTO resDTO = new RespuestaDTO();
                resDTO.setId((Long)f[0]);
                resDTO.setNombre((String)f[1]);
                causasPaciente.add(resDTO);
            }
            ListaMedicamentosDTO respuesta = new ListaMedicamentosDTO(causasPaciente);
            r = Response.status(200).header("Access-Control-Allow-Origin", "*").entity(respuesta).build();
        }catch(Exception e){
            r= Response.status(400).build();
        }
        return r;
    }
    
    /*
     *Metodo para crear un episodio asociado un paciente
    */
    @POST
    @Path("{id}/episodio")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response crearEpisodio(@PathParam("id") String id,Episodio nuevo){
        try{
           Paciente p = new Paciente();
           p.setId(Long.parseLong(id));
           nuevo.setPaciente(p);
           entityManager.getTransaction().begin();
           entityManager.persist(nuevo);
           entityManager.getTransaction().commit();   
        }catch(Exception e){
            if(entityManager.getTransaction().isActive()){
                entityManager.getTransaction().rollback();
            }
        }finally{
            entityManager.clear();
            entityManager.close();
        }
        return Response.status(200).header("Access-Control-Allow-Origin", "*").entity("{\"id\":"+nuevo.getId()+"}").build();
    }
    
    /*
     *Metodo que retorna la lista de episodios de un paciente
    */
    @GET
    @Path("{id}/episodio")
    @Produces(MediaType.APPLICATION_JSON)
    public Response darEpisodiosPaciente(@PathParam("id") String id){
        
        Query q = entityManager.createQuery("select u from Episodio u where u.paciente.id = ?1 ");
        q.setParameter(1, Long.parseLong(id));
        List<Episodio> episodios = q.getResultList();
        System.out.println(episodios.size());
        List<EpisodioDTO> episodiosDTO = new LinkedList<>();
        for(Episodio original: episodios){
            EpisodioDTO dto = new EpisodioDTO();
            dto.setFunciono(original.isFunciono());
            dto.setFecha(original.getFecha());
            dto.setMedicamento(original.getMedicamento().getNombre());
            dto.setIntensidad(original.getIntensidad());
            List<RespuestaDTO> listaCausasDTO = new LinkedList<>();
            for(Causa causaOriginal: original.getCausas()){
                RespuestaDTO causaDTO = new RespuestaDTO();
                causaDTO.setId(causaOriginal.getId());
                causaDTO.setNombre(causaOriginal.getNombre());
                listaCausasDTO.add(causaDTO);
            }
            dto.setCausas(listaCausasDTO);
            episodiosDTO.add(dto);
        }
        return Response.status(Response.Status.OK).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE, OPTIONS").header("Access-Control-Allow-Headers", "Content-Type, Content-Range, Content-Disposition, Content-Description").entity(new ListaEpisodiosDTO(episodiosDTO)).build();
    }
}
