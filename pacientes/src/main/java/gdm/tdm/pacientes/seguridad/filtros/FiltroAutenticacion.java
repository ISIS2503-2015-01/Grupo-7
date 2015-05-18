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
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author nicolas
 */
public class FiltroAutenticacion implements Filter{

    @Override
    public void init(FilterConfig fc) throws ServletException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void doFilter(ServletRequest sr, ServletResponse sr1, FilterChain fc) throws IOException, ServletException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        HttpServletRequest request = (HttpServletRequest) sr;
        if(request.getRequestURI().startsWith("/api/auth"))
            fc.doFilter(sr, sr1);
        else{
            String auntenticado = request.getHeader("autenticado");
            if(auntenticado!=null)
                fc.doFilter(sr, sr1);
            else{
                HttpServletResponse r = (HttpServletResponse) sr1;
                r.setStatus(403);
            }
                
        }
    }

    @Override
    public void destroy() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
