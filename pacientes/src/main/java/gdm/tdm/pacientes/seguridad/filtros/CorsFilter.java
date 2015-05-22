/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gdm.tdm.pacientes.seguridad.filtros;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author nicolas
 */
public class CorsFilter implements Filter {

    @Override
    public void init(FilterConfig fc) throws ServletException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void doFilter(ServletRequest sr, ServletResponse sr1, FilterChain fc) throws IOException, ServletException {
        
        HttpServletRequest a = (HttpServletRequest) sr;
        HttpServletResponse k = (HttpServletResponse) sr1;
        
            if(a.getMethod().equals("OPTIONS")){
                k.setHeader("Access-Control-Allow-Origin", "*");
                k.setHeader("Access-Control-Allow-Methods", "GET,POST");
                k.setHeader("Access-Control-Allow-Headers", "autenticado,Content-Type");
                fc.doFilter(sr, k);
            }else{
                if(a.getRequestURI().startsWith("/api/auth")){
                    k.setHeader("Access-Control-Allow-Origin", "*");
                    k.setHeader("Access-Control-Allow-Methods", "GET,POST");
                    k.setHeader("Access-Control-Allow-Headers", "autenticado,Content-Type");
                    fc.doFilter(sr, k);
                }else{
                    if(a.getHeader("autenticado")==null){
                        k.setStatus(403);
                    }else{
                        k.setHeader("Access-Control-Allow-Origin", "*");
                        k.setHeader("Access-Control-Allow-Methods", "GET,POST");
                        k.setHeader("Access-Control-Allow-Headers", "autenticado,Content-Type");
                        fc.doFilter(sr, k);   
                    } 
                }
                
            } 
        
       
        
    }

    @Override
    public void destroy() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
