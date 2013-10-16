package com.seguroshorizonte.horifarmacia.control;

import com.seguroshorizonte.capadeservicios.servicios.GestionDeControlDeUsuarios_Service;
import com.seguroshorizonte.capadeservicios.servicios.GestionDeUsuarios_Service;
import com.seguroshorizonte.capadeservicios.servicios.Sesion;
import com.seguroshorizonte.capadeservicios.servicios.Usuario;
import com.seguroshorizonte.capadeservicios.servicios.WrSesion;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.NetworkInterface;
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
@ManagedBean(name = "logIntOutController")
@SessionScoped
public class logIntOutController implements Serializable{
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_15362/CapaDeServiciosAnalistas/GestionDeUsuarios.wsdl")
    private GestionDeUsuarios_Service service_1;
     
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_15362/CapaDeServiciosAnalistas/GestionDeControlDeUsuarios.wsdl")
    private GestionDeControlDeUsuarios_Service service;
    /**
     * objeto con el cual se haran las validaciones en cuanto al inicio y el
     * cerrar sesión de usuario
     */
    private Usuario usuarioLogeo;
    /**
     * objeto con el cual se guardara la información del usuario para guardar en
     * la variable de sesión
     */
    private Usuario usuarioSesion;
    /**
     * cadena donde se guardara el nombre de usuario en el xhtml
     */
    private String User;
    /**
     * cadena donde se guardara la contraseña en el xhtml
     */
    private String Contrasena;
    /**
     * objeto de la clase de sesión creada para crear la sesión en la bd con el
     * servicio
     */
    private Sesion sesionUsuario;

    /**
     * Método constructor que se incia al hacer la llamada a la pagina
     * index.xhtml donde se verifica si el usuario esta logueado
     */
    @PostConstruct
    public void init() {
        if (verificarLogueo()) {
            Redireccionar();
        }
    }

    /**
     * Método para hacer el Inicio de Sesión Nota: De acuerdo a la dirección MAC
     * del equipo, si por alguna circunstancia no se puede obtener esa
     * dirección, se busca el nombre junto con la ip del equipo , y si tampoco
     * se puede obtener se guardara 127.0.0.1
     */
    public void logeoInt() {
        sesionUsuario = new Sesion();
        usuarioLogeo = new Usuario();
        usuarioLogeo.setId(User);
        usuarioLogeo.setClave(Contrasena);

        NetworkInterface Address;
        StringBuilder direccionMac = new StringBuilder();
        try {
            Address = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
            byte[] mac = Address.getHardwareAddress();
            for (int i = 0; i < mac.length; i++) {
                direccionMac.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            }
        } catch (Exception e) {
            System.out.println(e);
            try {
                direccionMac.setLength(0);
                direccionMac.append(InetAddress.getLocalHost().toString());
            } catch (Exception ee) {
                direccionMac.setLength(0);
                direccionMac.append("127.0.0.1");
            }
        }
        sesionUsuario.setIdUsuario(usuarioLogeo);
        sesionUsuario.setIp(direccionMac.toString());
        sesionUsuario.setBorrado(false);
        WrSesion envoltorio;
        envoltorio = logIn(sesionUsuario);
        if (envoltorio.getEstatus().compareTo("OK") == 0) {
            usuarioSesion = buscarUsuario(usuarioLogeo);
            //Se Crea la sesión llamada usuarioSesion junto con el objeto usuario
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            HttpSession Sesion = (HttpSession) ec.getSession(true);
            Sesion.invalidate();
            Sesion = (HttpSession) ec.getSession(true);
            Sesion.setAttribute("Usuario", usuarioSesion);
            Sesion.setAttribute("Sesion", envoltorio.getSesions().get(0));
            try {
                FacesContext contex = FacesContext.getCurrentInstance();
                contex.getExternalContext().redirect("/HoriFarmaciasAnalistas/faces/actividadgrupousuario.xhtml");
            } catch (Exception e) {
                System.out.println("----------------------------Error---------------------------------" + e);
            }
        } else {
            String mensajeError;
            if (User.compareTo("") == 0 || Contrasena.compareTo("") == 0) {
                mensajeError = "Debe agregar el usuario y la contraseña";
            } else {
                mensajeError = envoltorio.getObservacion();
            }
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia", mensajeError));

        }


    }

    /**
     * Método para verificar si el usuario esta logueado
     *
     * @return un booleano si es true es por que si estaba logueado
     */
    public boolean verificarLogueo() {
        boolean bandera = true, sesionBd = false;
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            HttpSession SesionAbierta = (HttpSession) ec.getSession(true);
            Usuario usuarioLogueo = (Usuario) (SesionAbierta.getAttribute("Usuario"));
            Sesion sesionLogueo = (Sesion) (SesionAbierta.getAttribute("Sesion"));
            sesionBd = logSesion(sesionLogueo);
            if (usuarioLogueo == null || sesionLogueo == null || !sesionBd) {
                bandera = false;
            }
        } catch (Exception e) {
            bandera = false;
        }

        return bandera;
    }

    /**
     * Método para redireccionar a actividadgrupousuario.xhtml si el usuario
     * esta logueado
     */
    public void Redireccionar() {
        try {
            FacesContext contex = FacesContext.getCurrentInstance();
            contex.getExternalContext().redirect("/HoriFarmaciasAnalistas/faces/actividadgrupousuario.xhtml");
        } catch (Exception error) {
            System.out.println("----------------------------Error---------------------------------" + error);
        }
    }

    /**
     *
     * @return
     */
    public String getUser() {
        return User;
    }

    /**
     *
     * @param User
     */
    public void setUser(String User) {
        this.User = User;
    }

    /**
     *
     * @return
     */
    public String getContrasena() {
        return Contrasena;
    }

    /**
     *
     * @param Contrasena
     */
    public void setContrasena(String Contrasena) {
        this.Contrasena = Contrasena;
    }

    /**
     * Servicio consumido de la capa de servicios para inicio de sesión
     */
    private WrSesion logIn(com.seguroshorizonte.capadeservicios.servicios.Sesion sesionActual) {
        com.seguroshorizonte.capadeservicios.servicios.GestionDeControlDeUsuarios port = service.getGestionDeControlDeUsuariosPort();
        return port.logIn(sesionActual);
    }


    private boolean logSesion(com.seguroshorizonte.capadeservicios.servicios.Sesion sesionActual) {
        com.seguroshorizonte.capadeservicios.servicios.GestionDeControlDeUsuarios port = service.getGestionDeControlDeUsuariosPort();
        return port.logSesion(sesionActual);
    }

    private Usuario buscarUsuario(com.seguroshorizonte.capadeservicios.servicios.Usuario usuarioActual) {
        com.seguroshorizonte.capadeservicios.servicios.GestionDeUsuarios port = service_1.getGestionDeUsuariosPort();
        return port.buscarUsuario(usuarioActual);
    }

   

  

}