
package com.seguroshorizonte.horifarmacia.control;

import com.seguroshorizonte.capadeservicios.servicios.Actividad;
import com.seguroshorizonte.capadeservicios.servicios.GestionDeActividades_Service;
import com.seguroshorizonte.capadeservicios.servicios.GestionDeControlDeUsuarios_Service;
import com.seguroshorizonte.capadeservicios.servicios.GestionDeUsuarios_Service;
import com.seguroshorizonte.capadeservicios.servicios.Sesion;
import com.seguroshorizonte.capadeservicios.servicios.Usuario;
import com.seguroshorizonte.capadeservicios.servicios.WrResultado;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.xml.ws.WebServiceRef;

/**
 * @author HoriFarmaciaAnalistas
 */
@ManagedBean(name = "asignarActividadController")
@SessionScoped
public class asignarActividadController {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_15362/CapaDeServiciosAnalistas/GestionDeActividades.wsdl")
    private GestionDeActividades_Service service_2;
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_15362/CapaDeServiciosAnalistas/GestionDeControlDeUsuarios.wsdl")
    private GestionDeControlDeUsuarios_Service service_1;
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_15362/CapaDeServiciosAnalistas/GestionDeUsuarios.wsdl")
    private GestionDeUsuarios_Service service;
    private List<Usuario> usuarios, lista;
    private Usuario usu;
    Usuario usuarioLogueo;
    Sesion sesionLogueo;

    /**
     *
     * @return
     */
    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    /**
     *
     * @param usuarios
     */
    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    /**
     *
     */
    @PostConstruct
    public void init() {
        //codigo para guardar la lista de usuarios
        int j = 0;
        lista = listarUsuarios(false);
        usuarios = new ArrayList<Usuario>();
        if (lista.isEmpty()) {
            usuarios = null;
        }
        while (lista.size() > j) {
            usu = lista.get(j);
            usuarios.add(usu);
            j++;
        }
    }

    /**
     *
     */
    public void asignacionActividad() {

        Actividad actividadAsignada = new Actividad();
        actividadAsignada.setId((long) 14);
        WrResultado envoltorio = asignarActividad(actividadAsignada, usuarioLogueo);
        if (envoltorio.getEstatus().compareTo("OK") == 0) {
            try {
                FacesContext contex = FacesContext.getCurrentInstance();
                contex.getExternalContext().redirect("/HoriFarmaciaAnalistas/faces/actividad.xhtml");
            } catch (Exception error) {
                System.out.println("----------------------------Error---------------------------------" + error);
            }
        }

    }

    /**
     * Método para verificar si el usuario esta logueado
     * @return 
     */
    public boolean verificarLogueo() {
        boolean bandera = false, sesionBd = false;
        try {
            //codigo para guardar sesion y usuario logueado, sino existe redireccionamos a index.xhtml
            FacesContext context = FacesContext.getCurrentInstance();
            ExternalContext externalContext = context.getExternalContext();
            Object session = externalContext.getSession(true);
            HttpSession SesionAbierta = (HttpSession) session;
            usuarioLogueo = (Usuario) (SesionAbierta.getAttribute("Usuario"));
            sesionLogueo = (Sesion) (SesionAbierta.getAttribute("Sesion"));
            sesionBd = logSesion(sesionLogueo);
            if (usuarioLogueo == null || sesionLogueo == null || !sesionBd) {
                bandera = true;
            }
        } catch (Exception e) {
            bandera = true;
        }

        return bandera;
    }

    /**
     * Método para redireccionar a index.xhtml si el usuario no esta logueado
     */
    public void Redireccionar() {
        try {
            FacesContext contex = FacesContext.getCurrentInstance();
            contex.getExternalContext().redirect("/HoriFarmaciaAnalistas/faces/index.xhtml");
        } catch (Exception error) {
            System.out.println("----------------------------Error---------------------------------" + error);
        }
    }

    /**
     *
     */
    public void Desactivado() {

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                "Su sesión se cerrara", "Ud ha estado inactivo mas de 3 minutos"));
    }

    /**
     *
     */
    public void Cerrar() {
        WrResultado result;
        result = logOut(sesionLogueo);
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        Object session = externalContext.getSession(true);
        HttpSession SesionAbierta = (HttpSession) session;
        SesionAbierta.invalidate();
        //   System.out.println("----------------------------oyeeeeee---------------------------------");

        Redireccionar();
    }

    private java.util.List<com.seguroshorizonte.capadeservicios.servicios.Usuario> listarUsuarios(boolean borrado) {
        com.seguroshorizonte.capadeservicios.servicios.GestionDeUsuarios port = service.getGestionDeUsuariosPort();
        return port.listarUsuarios(borrado);
    }

    private boolean logSesion(com.seguroshorizonte.capadeservicios.servicios.Sesion sesionActual) {
        com.seguroshorizonte.capadeservicios.servicios.GestionDeControlDeUsuarios port = service_1.getGestionDeControlDeUsuariosPort();
        return port.logSesion(sesionActual);
    }

    private WrResultado asignarActividad(com.seguroshorizonte.capadeservicios.servicios.Actividad actividadActual, com.seguroshorizonte.capadeservicios.servicios.Usuario usuarioActual) {
        com.seguroshorizonte.capadeservicios.servicios.GestionDeActividades port = service_2.getGestionDeActividadesPort();
        return port.asignarActividad(actividadActual, usuarioActual);
    }

    private WrResultado logOut(com.seguroshorizonte.capadeservicios.servicios.Sesion sesionActual) {
        com.seguroshorizonte.capadeservicios.servicios.GestionDeControlDeUsuarios port = service_1.getGestionDeControlDeUsuariosPort();
        return port.logOut(sesionActual);
    }
}