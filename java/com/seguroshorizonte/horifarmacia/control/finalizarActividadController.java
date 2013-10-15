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
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.xml.ws.WebServiceRef;

/**
 * @author HoriFarmaciaAnalistas
 */
@ManagedBean(name = "finalizarActController")
@SessionScoped
public class finalizarActividadController {
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_15362/CapaDeServiciosAnalistas/GestionDeActividades.wsdl")
    private GestionDeActividades_Service service;

   
    private List<Condicion> cond;
    private Condicion condactual;
    private Actividad actividad_actual;
    private Sesion sesionactual;
    private WrResultado resultado;

    @PostConstruct
    public void init() {

        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        HttpSession sesion = (HttpSession) ec.getSession(true);
        actividad_actual = (Actividad) (sesion.getAttribute("actividadactual"));

        cond = condicionesTransiciones(actividad_actual);

    }
    
     public void finalizar() {

        System.out.println(condactual.getId());

        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        HttpSession sesion = (HttpSession) ec.getSession(true);
        actividad_actual = (Actividad) (sesion.getAttribute("actividadactual"));
        sesionactual = (Sesion) (sesion.getAttribute("Sesion"));
        
        resultado=finalizarActividad(actividad_actual, sesionactual, condactual);
        
        try {
             ec.redirect("/HoriFarmaciaAnalistas/faces/actividadusuario.xhtml");
        } catch (Exception e) {
            System.out.println("----------------------------Error---------------------------------" + e);
        }
    }
     
     
      public void finalizardoc() {

        System.out.println(condactual.getId());

        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        HttpSession sesion = (HttpSession) ec.getSession(true);
        sesion.setAttribute("condicionactual", condactual);
        
        try {
             ec.redirect("/HoriFarmaciaAnalistas/faces/documentacion.xhtml");
        } catch (Exception e) {
            System.out.println("----------------------------Error---------------------------------" + e);
        }
    }
     
     
     

    public List<Condicion> getCond() {
        return cond;
    }

    public void setCond(List<Condicion> cond) {
        this.cond = cond;
    }

    public Actividad getActividad_actual() {
        return actividad_actual;
    }

    public void setActividad_actual(Actividad actividad_actual) {
        this.actividad_actual = actividad_actual;
    }

    public Condicion getCondactual() {
        return condactual;
    }

    public void setCondactual(Condicion condactual) {
        this.condactual = condactual;
    }

    private java.util.List<com.seguroshorizonte.capadeservicios.servicios.Condicion> condicionesTransiciones(com.seguroshorizonte.capadeservicios.servicios.Actividad actividad) {
        com.seguroshorizonte.capadeservicios.servicios.GestionDeActividades port = service.getGestionDeActividadesPort();
        return port.condicionesTransiciones(actividad);
    }

    private WrResultado finalizarActividad(com.seguroshorizonte.capadeservicios.servicios.Actividad actividadActual, com.seguroshorizonte.capadeservicios.servicios.Sesion sesionActual, com.seguroshorizonte.capadeservicios.servicios.Condicion condicionActual) {
        com.seguroshorizonte.capadeservicios.servicios.GestionDeActividades port = service.getGestionDeActividadesPort();
        return port.finalizarActividad(actividadActual, sesionActual, condicionActual);
    }

    
}