
package com.seguroshorizonte.horifarmacia.control;

import com.seguroshorizonte.capadeservicios.servicios.GestionDeControlDeUsuarios_Service;
import com.seguroshorizonte.capadeservicios.servicios.Sesion;
import com.seguroshorizonte.capadeservicios.servicios.Usuario;
import com.seguroshorizonte.capadeservicios.servicios.WrResultado;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.xml.ws.WebServiceRef;

/**
 *
 * @author PangeaTech
 */
@ManagedBean(name = "atrasController")
@SessionScoped
public class atrasController {
    /**
     *Método creado para redireccionar a la pagina anterior
     */
    public void Atras() {
            try {
                FacesContext contex = FacesContext.getCurrentInstance();
                contex.getExternalContext().redirect("/PangeaFlowProyecto/faces/usuarioGrupo.xhtml");
            } catch (Exception error) {
                System.out.println("----------------------------Error---------------------------------" + error);
            }
        
    }
    
      public void atrasIntancia() {
            try {
                FacesContext contex = FacesContext.getCurrentInstance();
                contex.getExternalContext().redirect("/PangeaFlowProyecto/faces/instanciaUsuario.xhtml");
            } catch (Exception error) {
                System.out.println("----------------------------Error---------------------------------" + error);
            }
        
    }


    
}
