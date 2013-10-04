/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.seguroshorizonte.horifarmacia.control;
import com.pangea.capadeservicios.servicios.Usuario;
import com.pangea.capadeservicios.servicios.Actividad;
import com.pangea.capadeservicios.servicios.GestionDeActividades_Service;
import com.pangea.capadeservicios.servicios.GestionDeControlDeUsuarios_Service;
import com.pangea.capadeservicios.servicios.Sesion;
import com.pangea.capadeservicios.servicios.WrResultado;
import com.seguroshorizonte.capadeservicios.servicios.GestionDeActividades_Service;
import com.seguroshorizonte.capadeservicios.servicios.GestionDeControlDeUsuarios_Service;
import com.seguroshorizonte.capadeservicios.servicios.WrResultado;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.WebServiceRef;

/**
 * @author Pangea
 */

@ManagedBean(name = "actividadController")

@SessionScoped

public class actividadController {
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_15362/CapaDeServiciosAnalistas/GestionDeActividades.wsdl")
    private GestionDeActividades_Service service_1;
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_15362/CapaDeServiciosAnalistas/GestionDeControlDeUsuarios.wsdl")
    private GestionDeControlDeUsuarios_Service service;
   
    
    /*
     * Objeto de la clase Usuario donde se guardara el objeto de la variable de sesión
     */
    Usuario usuarioLogueo;
    /*
     * Objeto de la clase Sesión donde se guardara el objeto de la variable de sesión
     */
    Sesion sesionLogueo;
    
    /*
     * Objetos de la clase List<Actividad> donde se guardara el objeto de la lista
     * de actividades que se cargan o consultan del servicio
     */
    private List<Actividad> actividades, actividad;
    
    /*
     * Objetos de la clase Actividad donde se guardara los objetos de la variables
     * de actividad que se cargan o conultadan del servicio
     */
    private Actividad activi, act, id;
    
    /*
     * Objeto de la clase Actividad donde se guardara el objeto de la variable de id de
     * la actividad
     */
    private Actividad idSesionActividad;
    
    /*
     * Objeto de la clase Sesion en donde se guarda el valor de la variable de la sesion 
     * abierta
     */
    private Sesion ses;
    
    /*
     * Objeto de tipo String en donde se guarda el id del usuario que inicio sesión
     */
    private String usuario;
    
    
    /**
     *
     * @return
     */
    public Sesion getSes() {
        return ses;
    }

    /**
     *
     * @param ses
     */
    public void setSes(Sesion ses) {
        this.ses = ses;
    }

    /**
     *
     * @return
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     *
     * @param usuario
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    
    /**
     *
     * @return
     */
    public Actividad getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(Actividad id) {
        this.id = id;
    }
    
    /**
     *
     * @return
     */
    public Actividad getActivi() {
        return activi;
    }

    /**
     *
     * @param activi
     */
    public void setActivi(Actividad activi) {
        this.activi = activi;
    }

    /**
     *
     * @return
     */
    public List<Actividad> getActividades() {
        return actividades;
    }

    /**
     *
     * @param actividades
     */
    public void setActividades(List<Actividad> actividades) {
        this.actividades = actividades;
    }

    /**
     *
     * @return
     */
    public Actividad getAct() {
        return act;
    }

    /**
     *
     * @param act
     */
    public void setAct(Actividad act) {
        this.act = act;
    }
    
    /**
     * Metodo constructor que se incia al hacer la llamada a la página
     * instanciaUsuario.xhml
     */
    @PostConstruct
    public void init() {
        
        //Carga las actividades con estado pendiente y que no han sido borradas
         
        int j=0;  
        activi= new Actividad(); 
        actividad=listarActividades("pendiente",false);
        actividades=new ArrayList<Actividad>();
        if(actividad.isEmpty()){
            actividades=null;
        }
        while (actividad.size()>j){
            act= actividad.get(j);
            actividades.add(act);
            j++;
        }
    }
    
    /**
     * Método en el que se obtiene en una variable de sesión el id de 
     * la actividad a la que se desea asignar el usuario y redirecciona 
     * a asignaractividad.xhtml 
     */
    public void actividadAsignar(){
        
        idSesionActividad= new Actividad();
        idSesionActividad.setId(act.getId());
        
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        Object session1 = externalContext.getSession(true);
        HttpSession httpSession = (HttpSession) session1;
        httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        httpSession.removeAttribute("IdActividad");
        httpSession.setAttribute("IdActividad", idSesionActividad);
        
        try {
            FacesContext contex = FacesContext.getCurrentInstance();
            contex.getExternalContext().redirect("/PangeaFlowProyecto/faces/asignarActividad.xhtml");
        } catch (Exception e) {
            System.out.println("----------------------------Error---------------------------------" + e);
        }    
       
      
    }
    
    /**
     * Método encargado de mostrar la fecha en el formato dd/mm/yyyy
     *
     * @param fecha
     * @return fecha en el formato dd/mm/yyyy
     */
    public String formatoFecha(XMLGregorianCalendar fecha) {
        if (fecha != null) {
            Date fechaDate = fecha.toGregorianCalendar().getTime();
            SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
            String fechaCadena = formateador.format(fechaDate);
            return fechaCadena;
        }
        else{
            System.out.println("----------------------------Error---------------------------------" );
        }
        return "";

    }
    
    
    /**
     * Método para verificar si el usuario esta logueado
     *
     * @return un booleano si es true es por que si estaba logueado
     */
    public boolean verificarLogueo() {
        boolean bandera = false, sesionBd = false;
        try {
            //Codigo para guardar sesion y usuario logueado, sino existe redireccionamos a index.xhtml
            FacesContext context = FacesContext.getCurrentInstance();
            ExternalContext externalContext = context.getExternalContext();
            Object session = externalContext.getSession(true);
            HttpSession SesionAbierta = (HttpSession) session;
            usuarioLogueo = (Usuario) (SesionAbierta.getAttribute("Usuario"));
            sesionLogueo = (Sesion) (SesionAbierta.getAttribute("Sesion"));
            
            //Guardo el valor del id del usuario y la sesión
            usuario=usuarioLogueo.getId();
            ses=sesionLogueo;
            
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
            contex.getExternalContext().redirect("/PangeaFlowProyecto/faces/index.xhtml");
        } catch (Exception error) {
            System.out.println("----------------------------Error---------------------------------" + error);
        }
    }

    /**
     * Método encargado de cerrar la sesión del usuario en la base de datos 
     * y a nivel de variables de sesión por tener un tiempo de inactividad 
     * de 4 minutos
     */
    public void cerrarPorInactividad() {
        WrResultado result;
        result = logOut(sesionLogueo);
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        Object session = externalContext.getSession(true);
        HttpSession SesionAbierta = (HttpSession) session;
        SesionAbierta.invalidate();
        Redireccionar();
    }

    
    private WrResultado logOut(com.seguroshorizonte.capadeservicios.servicios.Sesion sesionActual) {
        com.seguroshorizonte.capadeservicios.servicios.GestionDeControlDeUsuarios port = service.getGestionDeControlDeUsuariosPort();
        return port.logOut(sesionActual);
    }

    private boolean logSesion(com.seguroshorizonte.capadeservicios.servicios.Sesion sesionActual) {
        com.seguroshorizonte.capadeservicios.servicios.GestionDeControlDeUsuarios port = service.getGestionDeControlDeUsuariosPort();
        return port.logSesion(sesionActual);
    }

    private java.util.List<com.seguroshorizonte.capadeservicios.servicios.Actividad> listarActividades(java.lang.String estado, boolean borrado) {
        com.seguroshorizonte.capadeservicios.servicios.GestionDeActividades port = service_1.getGestionDeActividadesPort();
        return port.listarActividades(estado, borrado);
    }
    

}