/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gdm.tdm.pacientes.seguridad;

import com.stormpath.sdk.account.Account;
import com.stormpath.sdk.api.ApiKeys;
import com.stormpath.sdk.application.Application;
import com.stormpath.sdk.application.ApplicationList;
import com.stormpath.sdk.application.Applications;
import com.stormpath.sdk.authc.AuthenticationRequest;
import com.stormpath.sdk.authc.AuthenticationResult;
import com.stormpath.sdk.authc.UsernamePasswordRequest;
import com.stormpath.sdk.client.Client;
import com.stormpath.sdk.client.Clients;
import com.stormpath.sdk.group.Group;
import com.stormpath.sdk.resource.ResourceException;
import com.stormpath.sdk.tenant.Tenant;
import gdm.tdm.pacientes.pojos.DTO.UsuarioDTO;
import gdm.tdm.pacientes.servidor.ApiProperties;

/**
 *
 * @author nicolas
 */
public class StormPath {
    
    private final static Client cliente = Clients.builder().setApiKey(ApiKeys.builder().setProperties(new ApiProperties()).build()).build();
    
    private final Application app;
    
    private final Group doctores;
    
    private final Group pacientes;
    
    public StormPath(){
        Tenant tenant = cliente.getCurrentTenant();
        ApplicationList aplicaciones = tenant.getApplications(Applications.where(Applications.name().eqIgnoreCase("nph")));
        app = aplicaciones.iterator().next();
        doctores = cliente.getResource("https://api.stormpath.com/v1/groups/4BkIO3JCjYCLQnEWKCLYQI", Group.class);
        pacientes = cliente.getResource("https://api.stormpath.com/v1/groups/4LnDeY90ewgxc6mX4lUGdC", Group.class);
    }
    
    public boolean autenticar(UsuarioDTO usuario){
        AuthenticationRequest solicitud = new UsernamePasswordRequest(usuario.getEmail(), usuario.getPassword());
        try{
            AuthenticationResult resutl = app.authenticateAccount(solicitud);
            Account cuenta = resutl.getAccount();
            return true;
        }catch(ResourceException e){
            return false;
        }
    }
    
    public boolean registrar(UsuarioDTO nuevoUsuario){
        Account cuenta = cliente.instantiate(Account.class);
        cuenta.setEmail(nuevoUsuario.getEmail());
        cuenta.setGivenName(nuevoUsuario.getGivenName());
        cuenta.setSurname(nuevoUsuario.getSurName());
        cuenta.setPassword(nuevoUsuario.getPassword());
        try{
            app.createAccount(cuenta);
            if(nuevoUsuario.getGrupo().equals("pacientes")){
                cuenta.addGroup(pacientes);
            }else{
                cuenta.addGroup(doctores);
            }
            return true;
        }catch(ResourceException e){
            return false;
        }
    } 
}
