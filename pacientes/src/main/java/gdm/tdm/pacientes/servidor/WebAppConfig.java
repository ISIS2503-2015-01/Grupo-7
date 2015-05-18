/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gdm.tdm.pacientes.servidor;

import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;

/**
 *
 * @author nicolas
 */
@ApplicationPath("/api")
public class WebAppConfig extends ResourceConfig {
    public WebAppConfig(){
        packages("gdm.tdm.pacientes.servicios");
    }
}
