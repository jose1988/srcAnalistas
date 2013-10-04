/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.seguroshorizonte.horifarmacia.control;

import com.seguroshorizonte.capadeservicios.servicios.Actividad;
import com.seguroshorizonte.capadeservicios.servicios.Condicion;
import com.seguroshorizonte.capadeservicios.servicios.Sesion;
import com.seguroshorizonte.capadeservicios.servicios.GestionDeActividades_Service;
import com.seguroshorizonte.capadeservicios.servicios.WrResultado;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.xml.ws.WebServiceRef;
import org.primefaces.model.UploadedFile;

/**
 * @author Pangea
 */
@ManagedBean(name = "documentacionController")
@SessionScoped
public class documentacionController {
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_15362/CapaDeServicios/GestionDeActividades.wsdl")
    private GestionDeActividades_Service service;

   
    private List<Condicion> cond;
    private List<Actividad> act;
    private Condicion condactual;
    private Actividad actividad_actual;
    private Sesion sesionactual;
    private Condicion condicion_actual;
    private WrResultado resultado;
    private UploadedFile file;  
      
    @PostConstruct
    public void init() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        HttpSession sesion = (HttpSession) ec.getSession(true);
        actividad_actual = (Actividad) (sesion.getAttribute("actividadactual"));
        cargar();
        
     
       
    }

  
    
    public void upload() {  
        FacesMessage msg = new FacesMessage("Ok", "Fichero " + file.getFileName() + " subido correctamente.");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }  
    
    public void cargar(){
        
       act.add(actividad_actual); 
    }
    
     public void finalizar() {

       

        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        HttpSession sesion = (HttpSession) ec.getSession(true);
        actividad_actual = (Actividad) (sesion.getAttribute("actividadactual"));
        sesionactual = (Sesion) (sesion.getAttribute("Sesion"));
        condicion_actual = (Condicion) (sesion.getAttribute("condicionactual"));
        
         
         resultado=finalizarActividad(actividad_actual, sesionactual, condicion_actual);
        
        try {
             ec.redirect("/PangeaFlowProyecto/faces/actividadusuario.xhtml");
        } catch (Exception e) {
            System.out.println("----------------------------Error---------------------------------" + e);
        }
    }
     
      public List<Actividad> getAct() {
        return act;
    }

    public void setAct(List<Actividad> act) {
        this.act = act;
    }
   public UploadedFile getFile() {  
        return file;  
    }  
   
    public void setFile(UploadedFile file) {  
        this.file = file;  
    }  
      public Actividad getActividad_actual() {
        return actividad_actual;
    }

    public void setActividad_actual(Actividad actividad_actual) {
        this.actividad_actual = actividad_actual;
    }

    private WrResultado finalizarActividad(com.seguroshorizonte.capadeservicios.servicios.Actividad actividadActual, com.seguroshorizonte.capadeservicios.servicios.Sesion sesionActual, com.seguroshorizonte.capadeservicios.servicios.Condicion condicionActual) {
        com.seguroshorizonte.capadeservicios.servicios.GestionDeActividades port = service.getGestionDeActividadesPort();
        return port.finalizarActividad(actividadActual, sesionActual, condicionActual);
    }
    
}