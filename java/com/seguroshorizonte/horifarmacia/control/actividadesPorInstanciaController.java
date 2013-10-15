package com.seguroshorizonte.horifarmacia.control;

import com.seguroshorizonte.capadeservicios.servicios.Actividad;
import com.seguroshorizonte.capadeservicios.servicios.GestionDeActividades_Service;
import com.seguroshorizonte.capadeservicios.servicios.GestionDeControlDeUsuarios_Service;
import com.seguroshorizonte.capadeservicios.servicios.GestionDeInstancias_Service;
import com.seguroshorizonte.capadeservicios.servicios.Instancia;
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
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.WebServiceRef;

/**
 * @author HoriFarmaciaAnalistas
 */
@ManagedBean(name = "actividadesPorInstanciaController")
@SessionScoped
public class actividadesPorInstanciaController {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_15362/CapaDeServiciosAnalistas/GestionDeActividades.wsdl")
    private GestionDeActividades_Service service_2;
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_15362/CapaDeServiciosAnalistas/GestionDeInstancias.wsdl")
    private GestionDeInstancias_Service service;
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_15362/CapaDeServiciosAnalistas/GestionDeControlDeUsuarios.wsdl")
    private GestionDeControlDeUsuarios_Service service_1;
    /*
     * Objeto de la clase actividad para mostrar la información de las actividades por instancia en una lista
     */
    private List<Actividad> actividades;
    /*
     * Objeto de la clase actividad para mostrar la información de las actividades por instancia
     */
    private Actividad act;
    /*
     * Objeto de la clase usuario donde se guardara el objeto de la variable de sesión del logueo
     */
    Usuario usuarioLogueo;
    /*
     * Objeto de la clase sesión donde se guardara el objeto de la variable de sesión del logueo
     */
    Sesion sesionLogueo;
    /*
     * Objeto de la clase Instancia donde se guardara el objeto de la instancia a la que pertenecen las actividades
     */
    Instancia Instancia;

    /**
     * Método constructor que se incia al hacer la llamada a la pagina
     * actividadesPorInstancia.xhtml donde se muestra las actividades de una
     * determinada instancia
     */
    @PostConstruct
    public void init() {
        if (verificarLogueo()) {
            Redireccionar();
        } else if (verificarInstancia()) {
            redireccionarInstanciaUsuario();
        } else {
            //codigo para guardar la lista de actividades por Instancia
            FacesContext context = FacesContext.getCurrentInstance();
            ExternalContext externalContext = context.getExternalContext();
            Object session = externalContext.getSession(true);
            HttpSession SesionAbierta = (HttpSession) session;
            Instancia = (Instancia) (SesionAbierta.getAttribute("IdInstancia"));
            int j = 0;
            WrActividad Envoltorio, datosActividad;
            Envoltorio = consultarActividadesPorInstancia(Instancia);
            actividades = new ArrayList<Actividad>();
            if (Envoltorio.getActividads().isEmpty()) {
                actividades = null;
            }
            while (Envoltorio.getActividads().size() > j) {
                datosActividad = consultarActividad(Envoltorio.getActividads().get(j));
                if (datosActividad.getEstatus().compareTo("OK") == 0) {
                    act = datosActividad.getActividads().get(0);
                    actividades.add(act);
                }
                j++;
            }
        }
        // SesionAbierta.removeAttribute("IdInstancia");
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
     * Método para verificar si el usuario esta logueado
     *
     * @return un booleano si es true es por que si estaba logueado
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
    public boolean verificarInstancia() {
        boolean bandera = false;
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            ExternalContext externalContext = context.getExternalContext();
            Object session = externalContext.getSession(true);
            HttpSession SesionAbierta = (HttpSession) session;
            Instancia = (Instancia) (SesionAbierta.getAttribute("IdInstancia"));
            if (Instancia == null) {
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

    public void redireccionarInstanciaUsuario() {
        try {
            FacesContext contex = FacesContext.getCurrentInstance();
            contex.getExternalContext().redirect("/HoriFarmaciaAnalistas/faces/instanciaUsuario.xhtml");
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
        Instancia = (Instancia) (SesionAbierta.getAttribute("IdInstancia"));
        int j = 0;
        WrActividad Envoltorio, datosActividad;
        Envoltorio = consultarActividadesPorInstancia(Instancia);
        actividades = new ArrayList<Actividad>();
        if (Envoltorio.getActividads().isEmpty()) {
            actividades = null;
        }
        while (Envoltorio.getActividads().size() > j) {
            datosActividad = consultarActividad(Envoltorio.getActividads().get(j));
            if (datosActividad.getEstatus().compareTo("OK") == 0) {
                act = datosActividad.getActividads().get(0);
                actividades.add(act);
            }
            j++;
        }
    }

    /**
     * Mensaje a mostrar si el usuario deja la cuenta sin usar luego de 4 min
     */
    public void Desactivado() {

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                "Su sesión se cerrara", "Ud ha estado inactivo más de 4 minutos"));
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
     * @return fecha en el formato dd/mm/yyyy
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

    /**
     * Método encargado de sombrear las actividades en caso de que halla cerrado
     * la actividad luego de que caducara el periodo de tiempo asignado
     *
     * @param actividadx objeto de la actividad a la cual se evaluara el periodo
     * para sombrearse de un color determinado
     * @return el color del sombreado de la actividad
     * @throws DatatypeConfigurationException
     */
    public String estilo(Actividad actividadx) throws DatatypeConfigurationException {

        if (actividadx != null) {
            if (actividadx.getEstado().compareTo("cerrada") == 0) {
                if (actividadx.getFechaCierre() != null && actividadx.getFechaApertura() != null) {
                    XMLGregorianCalendar FC1;
                    XMLGregorianCalendar FA1;
                    FC1 = actividadx.getFechaCierre();
                    FA1 = actividadx.getFechaApertura();
                    long duracion = actividadx.getDuracion().longValue();
                    long f = FC1.toGregorianCalendar().getTimeInMillis();
                    long f2 = FA1.toGregorianCalendar().getTimeInMillis();
                    long resta = f - f2;
                    long dias = resta / (24 * 60 * 60 * 1000);
                    if (dias > duracion) {
                        return "background-color:  #FF8888;";
                    }

                    return " background-color: white";
                }
            } else {
                if (actividadx.getFechaCierre() != null) {
                    XMLGregorianCalendar FC1;
                    XMLGregorianCalendar FA1;
                    FC1 = actividadx.getFechaCierre();
                    Date fc = FC1.toGregorianCalendar().getTime();
                    Date fecha = new Date();

                    if (fc.before(fecha)) {
                        return "background-color:  #FF8888";
                    } else if (actividadx.getFechaAlerta() != null) {
                        FA1 = actividadx.getFechaAlerta();
                        Date fa = FA1.toGregorianCalendar().getTime();
                        if (fa.before(fecha)) {
                            return "background-color: orange;";
                        }
                        return "background-color: white";
                    }
                }
            }
        }

        return " background-color: white;";
    }

    /**
     * Metodo que permite colocar el estilo de una fila mediante un color
     *
     * @param actividadx
     * @return
     */
//    public String estilo(Actividad actividadx) {
//        if (actividadx != null) {
//            Date fecha = actividadx.getFechaCierre().toGregorianCalendar().getTime();
//            SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
//            String fechaSistema = formateador.format(new Date());
//            String fechaCierre = formateador.format(fecha);
//            boolean resultado = compararFechasConDate(fechaCierre, fechaSistema);
//            if (resultado) {
//                return " background-color: #FF8888;";
//            }
//        }
//        return " background-color: white;";
//    }
    /**
     * Método encargado de comparar fechas enviandolas coomo String en formato
     * dd/mm/yyyy
     *
     * @param fecha1
     * @param fechaActual
     * @return
     */
    public boolean compararFechasConDate(String fecha1, String fechaActual) {
        boolean resultado = false;
        try {
            // Obtenemos las fechas enviadas en el formato a comparar
            SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
            Date fechaDate1 = formateador.parse(fecha1);
            Date fechaDate2 = formateador.parse(fechaActual);
            if (fechaDate2.before(fechaDate1)) {
                resultado = true;
            }
        } catch (Exception e) {
            System.out.println("Se Produjo un Error!!!  " + e.getMessage());
        }
        return resultado;
    }
//Servicios de Capa de Servicios

    private boolean logSesion(com.seguroshorizonte.capadeservicios.servicios.Sesion sesionActual) {
        com.seguroshorizonte.capadeservicios.servicios.GestionDeControlDeUsuarios port = service_1.getGestionDeControlDeUsuariosPort();
        return port.logSesion(sesionActual);
    }

    private WrResultado logOut(com.seguroshorizonte.capadeservicios.servicios.Sesion sesionActual) {
        com.seguroshorizonte.capadeservicios.servicios.GestionDeControlDeUsuarios port = service_1.getGestionDeControlDeUsuariosPort();
        return port.logOut(sesionActual);
    }

    private com.seguroshorizonte.capadeservicios.servicios.WrActividad consultarActividadesPorInstancia(com.seguroshorizonte.capadeservicios.servicios.Instancia instanciaActual) {
        com.seguroshorizonte.capadeservicios.servicios.GestionDeInstancias port = service.getGestionDeInstanciasPort();
        return port.consultarActividadesPorInstancia(instanciaActual);
    }

    private WrActividad consultarActividad(com.seguroshorizonte.capadeservicios.servicios.Actividad actividadActual) {
        com.seguroshorizonte.capadeservicios.servicios.GestionDeActividades port = service_2.getGestionDeActividadesPort();
        return port.consultarActividad(actividadActual);
    }
}