package com.seguroshorizonte.horifarmacia.control;
import com.seguroshorizonte.capadeservicios.servicios.Actividad;
import com.seguroshorizonte.capadeservicios.servicios.GestionDeActividades_Service;
import com.seguroshorizonte.capadeservicios.servicios.GestionDeControlDeUsuarios_Service;
import com.seguroshorizonte.capadeservicios.servicios.GestionDeInstancias_Service;
import com.seguroshorizonte.capadeservicios.servicios.Grupo;
import com.seguroshorizonte.capadeservicios.servicios.Sesion;
import com.seguroshorizonte.capadeservicios.servicios.Usuario;
import com.seguroshorizonte.capadeservicios.servicios.WrActividad;
import com.seguroshorizonte.capadeservicios.servicios.WrResultado;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.WebServiceRef;

/**
 * @author PangeaTech
 */
@ManagedBean(name = "actividadesUsuarioController")
@SessionScoped
public class actividadesUsuarioController {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_15362/CapaDeServicios/GestionDeActividades.wsdl")
    private GestionDeActividades_Service service_2;
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_15362/CapaDeServicios/GestionDeInstancias.wsdl")
    private GestionDeInstancias_Service service;
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_15362/CapaDeServicios/GestionDeControlDeUsuarios.wsdl")
    private GestionDeControlDeUsuarios_Service service_1;
    /*
     * Objeto de la clase actividad para mostrar la información de las actividades por instancia en una lista
     */
    private List<Actividad> actividades;
    /*
     * Objeto de la clase actividad para mostrar la información de las actividades de acuerdo al usuario y al grupo
     */
    private Actividad act;
    /*
     * Objeto de la clase actividad usada para guardar el id de la actividad que se liberara 
     */
    private Actividad actividadLibrar;
    /*
     * Objeto de la clase usuario donde se guardara el objeto de la variable de sesión del logueo
     */
    private Usuario usuarioLogueo;
    /*
     * Objeto de la clase sesión donde se guardara el objeto de la variable de sesión del logueo
     */
    private Sesion sesionLogueo;
    /* Las actividades se mostraran de acuerdo a un usuario y al grupo al que pertenecen
     * Objeto de la clase usuario donde se guardara el id del usuario para mostrar sus respectivas actividades
     */
    private Usuario usuarioId;
    /*
     * Objeto de la clase Grupo donde se guardara el id del grupo para mostrar sus respectivas actividades
     */
    private Grupo grupoId;
    /*
     * Obejto de la clase envoltorio de actividades que servira para guardar las actividades abiertas que se traen del servicio
     */
    private WrActividad envoltorioAbiertas;
    /*
     * Obejto de la clase envoltorio de actividades que servira para guardar las actividades pendientes que se traen del servicio
     */
    private WrActividad envoltorioPendientes;

    /**
     * Método constructor que se inicia al hacer la llamada a la pagina
     * actividadesPorUsuario.xhtml donde se muestra las actividades de acuerdo a
     * un usuario y al grupo al que pertenecen
     */
    @PostConstruct
    public void init() {
        if (verificarLogueo()) {
            Redireccionar();
        } else if (verificarUsuarioId()) {
            redireccionarUsuarioGrupo();
        } else {
            //codigo para guardar la lista de actividades por Instancia
            int j = 0;
            Actividad estadoActividad = new Actividad();
            estadoActividad.setEstado("abierta");
            FacesContext context = FacesContext.getCurrentInstance();
            ExternalContext externalContext = context.getExternalContext();
            Object session = externalContext.getSession(true);
            HttpSession SesionAbierta = (HttpSession) session;
            usuarioId = (Usuario) (SesionAbierta.getAttribute("IdUsuario"));
            grupoId = (Grupo) (SesionAbierta.getAttribute("IdGrupo"));
            envoltorioAbiertas = consultarActividades(usuarioId, estadoActividad);
            estadoActividad.setEstado("pendiente");
            envoltorioPendientes = consultarActividades(usuarioId, estadoActividad);
            actividades = new ArrayList<Actividad>();
            if ((envoltorioAbiertas.getActividads().isEmpty() || envoltorioAbiertas.getActividads() == null) && (envoltorioPendientes.getActividads().isEmpty() || envoltorioPendientes.getActividads() == null)) {
                actividades = null;
            }
            while (envoltorioAbiertas.getActividads().size() > j) {
                act = envoltorioAbiertas.getActividads().get(j);
                if (act.getIdInstancia().getIdPeriodoGrupoProceso().getIdGrupo().getId() == grupoId.getId() && act.getIdInstancia().getEstado().compareTo("abierta") == 0) {
                    actividades.add(act);
                }
                j++;
            }
            j = 0;
            while (envoltorioPendientes.getActividads().size() > j) {
                act = envoltorioPendientes.getActividads().get(j);
                if (act.getIdInstancia().getIdPeriodoGrupoProceso().getIdGrupo().getId() == grupoId.getId() && act.getIdInstancia().getEstado().compareTo("abierta") == 0) {
                    actividades.add(act);
                }
                j++;
            }
        }

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
     *
     * @return
     */
    public List<Actividad> getActividades() {
        return actividades;
    }

    /**
     *
     * @param Actividades
     */
    public void setActividades(List<Actividad> Actividades) {
        this.actividades = Actividades;
    }

    /**
     * Método en el cual se libera una actividad especifica y se agrega a la
     * cola
     */
    public void liberarActividadUsuario() {
        actividadLibrar = new Actividad();
        actividadLibrar.setId(act.getId());
        WrResultado Envoltorio = liberarActividad(actividadLibrar, usuarioId);
        if (Envoltorio.getEstatus().compareTo("OK") == 0) {
            int j = 0;
            Actividad estadoActividad = new Actividad();
            estadoActividad.setEstado("abierta");
            envoltorioAbiertas = consultarActividades(usuarioId, estadoActividad);
            estadoActividad.setEstado("pendiente");
            envoltorioPendientes = consultarActividades(usuarioId, estadoActividad);
            actividades = new ArrayList<Actividad>();
            if ((envoltorioAbiertas.getActividads().isEmpty() || envoltorioAbiertas.getActividads() == null) && (envoltorioPendientes.getActividads().isEmpty() || envoltorioPendientes.getActividads() == null)) {
                actividades = null;
            }
            while (envoltorioAbiertas.getActividads().size() > j) {
                act = envoltorioAbiertas.getActividads().get(j);
                if (act.getIdInstancia().getIdPeriodoGrupoProceso().getIdGrupo().getId() == grupoId.getId() && act.getIdInstancia().getEstado().compareTo("abierta") == 0) {
                    actividades.add(act);
                }
                j++;
            }
            j = 0;
            while (envoltorioPendientes.getActividads().size() > j) {
                act = envoltorioPendientes.getActividads().get(j);
                if (act.getIdInstancia().getIdPeriodoGrupoProceso().getIdGrupo().getId() == grupoId.getId() && act.getIdInstancia().getEstado().compareTo("abierta") == 0) {
                    actividades.add(act);
                }
                j++;
            }
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Actividad Liberada", "Se ha liberado la actividad satisfactoriamente"));
        } else {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo ejecutar la acción"));
        }
    }

    /**
     * Método en el cual se libera todas las actividades
     */
    public void liberarActividadesUsuario() {
        WrResultado Envoltorio = liberarActividades(usuarioId);
        if (Envoltorio.getEstatus().compareTo("OK") == 0) {
            int j = 0;
            Actividad estadoActividad = new Actividad();
            estadoActividad.setEstado("abierta");
            envoltorioAbiertas = consultarActividades(usuarioId, estadoActividad);
            estadoActividad.setEstado("pendiente");
            envoltorioPendientes = consultarActividades(usuarioId, estadoActividad);
            actividades = new ArrayList<Actividad>();
            if ((envoltorioAbiertas.getActividads().isEmpty() || envoltorioAbiertas.getActividads() == null) && (envoltorioPendientes.getActividads().isEmpty() || envoltorioPendientes.getActividads() == null)) {
                actividades = null;
            }
            while (envoltorioAbiertas.getActividads().size() > j) {
                act = envoltorioAbiertas.getActividads().get(j);
                if (act.getIdInstancia().getIdPeriodoGrupoProceso().getIdGrupo().getId() == grupoId.getId() && act.getIdInstancia().getEstado().compareTo("abierta") == 0) {
                    actividades.add(act);
                }
                j++;
            }
            j = 0;
            while (envoltorioPendientes.getActividads().size() > j) {
                act = envoltorioPendientes.getActividads().get(j);
                if (act.getIdInstancia().getIdPeriodoGrupoProceso().getIdGrupo().getId() == grupoId.getId() && act.getIdInstancia().getEstado().compareTo("abierta") == 0) {
                    actividades.add(act);
                }
                j++;
            }
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Actividades Liberadas", "Se han liberado todas las actividades satisfactoriamente"));
        } else {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo ejecutar la acción"));
        }
    }

    /**
     * Método para verificar si el usuario esta logueado
     *
     * @return un booleano si es true es por que no estaba logueado
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
     * Método para verificar si existen las variables de sesión del usuario y el
     * grupo al que pertenecen las actividades a mostrar
     *
     * @return un booleano si es true es porque no existen las variables de
     * sesión no existenn
     */
    public boolean verificarUsuarioId() {
        boolean bandera = false;
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            ExternalContext externalContext = context.getExternalContext();
            Object session = externalContext.getSession(true);
            HttpSession SesionAbierta = (HttpSession) session;
            usuarioId = (Usuario) (SesionAbierta.getAttribute("IdUsuario"));
            grupoId = (Grupo) (SesionAbierta.getAttribute("IdGrupo"));
            if (usuarioId == null || grupoId == null) {
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
     * Método para redireccionar a UsuarioGrupo.xhtml si las variables de sesion
     * de usuario y grupo no existen
     */
    public void redireccionarUsuarioGrupo() {
        try {
            FacesContext contex = FacesContext.getCurrentInstance();
            contex.getExternalContext().redirect("/PangeaFlowProyecto/faces/usuarioGrupo.xhtml");
        } catch (Exception error) {
            System.out.println("----------------------------Error---------------------------------" + error);
        }
    }

    /**
     * Método para listar las actividades cuando ya no entra al constructor
     */
    public void listarActividades() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        Object session = externalContext.getSession(true);
        HttpSession SesionAbierta = (HttpSession) session;
        usuarioId = (Usuario) (SesionAbierta.getAttribute("IdUsuario"));
        grupoId = (Grupo) (SesionAbierta.getAttribute("IdGrupo"));
        int j = 0;
        Actividad estadoActividad = new Actividad();
        estadoActividad.setEstado("abierta");
        envoltorioAbiertas = consultarActividades(usuarioId, estadoActividad);
        estadoActividad.setEstado("pendiente");
        envoltorioPendientes = consultarActividades(usuarioId, estadoActividad);
        actividades = new ArrayList<Actividad>();
        if ((envoltorioAbiertas.getActividads().isEmpty() || envoltorioAbiertas.getActividads() == null) && (envoltorioPendientes.getActividads().isEmpty() || envoltorioPendientes.getActividads() == null)) {
            actividades = null;
        }
        while (envoltorioAbiertas.getActividads().size() > j) {
            act = envoltorioAbiertas.getActividads().get(j);
            if (act.getIdInstancia().getIdPeriodoGrupoProceso().getIdGrupo().getId() == grupoId.getId() && act.getIdInstancia().getEstado().compareTo("abierta") == 0) {
                actividades.add(act);
            }
            j++;
        }
        j = 0;
        while (envoltorioPendientes.getActividads().size() > j) {
            act = envoltorioPendientes.getActividads().get(j);
            if (act.getIdInstancia().getIdPeriodoGrupoProceso().getIdGrupo().getId() == grupoId.getId() && act.getIdInstancia().getEstado().compareTo("abierta") == 0) {
                actividades.add(act);
            }
            j++;
        }
    }

    /**
     *
     */
    public void Desactivado() {

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                "Su sesión se cerrara", "Ud ha estado inactivo mas de 4 minutos"));
    }

    /**
     * Método encargado de cerrar la sesión del usuario en la base de datos y a
     * nivel de variables de sesión por tener un tiempo de inactividad de
     * 4minutos
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

    /**
     * Método encargado de mostrar la fecha en el formato dd/mm/yyyy
     *
     * @param fecha
     * @return
     */
    public String formatoFecha(XMLGregorianCalendar fecha) {
        if (fecha != null) {
            Date fechaDate = fecha.toGregorianCalendar().getTime();
            SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
            String fechaCadena = formateador.format(fechaDate);
            return fechaCadena;
        }
        return "";
    }

    private boolean logSesion(com.seguroshorizonte.capadeservicios.servicios.Sesion sesionActual) {
        com.seguroshorizonte.capadeservicios.servicios.GestionDeControlDeUsuarios port = service_1.getGestionDeControlDeUsuariosPort();
        return port.logSesion(sesionActual);
    }

    private WrResultado logOut(com.seguroshorizonte.capadeservicios.servicios.Sesion sesionActual) {
        com.seguroshorizonte.capadeservicios.servicios.GestionDeControlDeUsuarios port = service_1.getGestionDeControlDeUsuariosPort();
        return port.logOut(sesionActual);
    }

    private WrActividad consultarActividades(com.seguroshorizonte.capadeservicios.servicios.Usuario usuarioActual, com.seguroshorizonte.capadeservicios.servicios.Actividad actividadActual) {
        com.seguroshorizonte.capadeservicios.servicios.GestionDeActividades port = service_2.getGestionDeActividadesPort();
        return port.consultarActividades(usuarioActual, actividadActual);
    }

    private WrResultado liberarActividades(com.seguroshorizonte.capadeservicios.servicios.Usuario usuarioActual) {
        com.seguroshorizonte.capadeservicios.servicios.GestionDeActividades port = service_2.getGestionDeActividadesPort();
        return port.liberarActividades(usuarioActual);
    }

    private WrResultado liberarActividad(com.seguroshorizonte.capadeservicios.servicios.Actividad actividadActual, com.seguroshorizonte.capadeservicios.servicios.Usuario usuarioActual) {
        com.seguroshorizonte.capadeservicios.servicios.GestionDeActividades port = service_2.getGestionDeActividadesPort();
        return port.liberarActividad(actividadActual, usuarioActual);
    }
}