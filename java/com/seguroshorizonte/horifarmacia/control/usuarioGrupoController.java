/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.seguroshorizonte.HoriFarmaciaAnalistas.control;

import com.seguroshorizonte.capadeservicios.servicios.GestionDeControlDeUsuarios_Service;
import com.seguroshorizonte.capadeservicios.servicios.Usuario;
import com.seguroshorizonte.capadeservicios.servicios.GestionDeGrupo_Service;
import com.seguroshorizonte.capadeservicios.servicios.GestionDeUsuarios_Service;
import com.seguroshorizonte.capadeservicios.servicios.Grupo;
import com.seguroshorizonte.capadeservicios.servicios.Sesion;
import com.seguroshorizonte.capadeservicios.servicios.UsuarioGrupoRol;
import com.seguroshorizonte.capadeservicios.servicios.WrResultado;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.xml.ws.WebServiceRef;
import org.primefaces.event.TabChangeEvent;

/**
 * @author HoriFarmaciaAnalistas
 */
@ManagedBean(name = "usuarioGrupoController")
@SessionScoped
public class usuarioGrupoController implements Serializable {

    private static final long serialVersionUID = 1L;
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_15362/CapaDeServiciosAnalistas/GestionDeControlDeUsuarios.wsdl")
    private GestionDeControlDeUsuarios_Service service_1;
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_15362/CapaDeServiciosAnalistas/GestionDeUsuarios.wsdl")
    private GestionDeUsuarios_Service service;
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_15362/CapaDeServiciosAnalistas/GestionDeGrupo.wsdl")
    private GestionDeGrupo_Service service_2;
    
    /*
     * Objeto de la clase usuario donde se guardara el objeto de la variable de sesión
     */
    Usuario usuarioLogueo;
    /*
     * Objeto de la clase sesión donde se guardara el objeto de la variable de sesión
     */
    Sesion sesionLogueo;
    
    /*
     * Objetos de la clase Usuario en donde se guardaran los objetos del usuario
     */
    private Usuario idusu, usuarioGrupoSeleccionado, datosusuarios;    
    
    /*
     * Objeto de la clase Grupo en donde se guardara el obejto del grupo seleccionado 
     * en el menu
     */
    private Grupo grupoSeleccionado;
    
    /*
     * Objetos de la clase List<Grupo> en donde se guardara el objeto de la lista
     * de grupo que se cargan o se consultan en el servicio
     */
    private List<Grupo> grupos;
    
    /*
     * Objetos de la clase UsuarioGrupoRol donde se guardara el objeto de UsuarioGrupoRol
     */
    private UsuarioGrupoRol grup;
    
    /*
     * Objetos de la clase List<UsuarioGrupoRol> donde se se guardara el objeto de
     * List<UsuarioGrupoRol> que se cargan o se consultan en el serivicio
     */
    private List<UsuarioGrupoRol> grupo, grupoUsuarios;
    
    /*
     * Objetos de tipo int en donde se guardara la variable indice de grupo
     * seleccionado
     */
    private int indice;
    
    /*
     * Objetos de tipo String en donde se guardara la variable id del usuario
     */
    private String idUsuario;
    
    /*
     * Objeto de la clase Sesion en donde se guarda el valor de la variable de la sesion 
     * abierta
     */
    private Sesion ses;
    
    /*
     * Objeto de tipo String en donde se guarda el id del usuario que inicio sesión
     */
    private String usuario;
    private String grupoPanel;
    
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
    public Usuario getDatosusuarios() {
        return datosusuarios;
    }

    /**
     *
     * @param datosusuarios
     */
    public void setDatosusuarios(Usuario datosusuarios) {
        this.datosusuarios = datosusuarios;
    }
    
    /**
     *
     * @return
     */
    public Usuario getUsuarioGrupoSeleccionado() {
        return usuarioGrupoSeleccionado;
    }

    /**
     *
     * @param usuarioGrupoSeleccionado
     */
    public void setUsuarioGrupoSeleccionado(Usuario usuarioGrupoSeleccionado) {
        this.usuarioGrupoSeleccionado = usuarioGrupoSeleccionado;
    }

    /**
     *
     * @return
     */
    public Grupo getGrupoSeleccionado() {
        return grupoSeleccionado;
    }

    /**
     *
     * @param grupoSeleccionado
     */
    public void setGrupoSeleccionado(Grupo grupoSeleccionado) {
        this.grupoSeleccionado = grupoSeleccionado;
    }

    /**
     *
     * @return
     */
    public String getIdUsuario() {
        return idUsuario;
    }

    /**
     *
     * @param idUsuario
     */
    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    /**
     *
     * @return
     */
    public int getIndice() {
        return indice;
    }

    /**
     *
     * @param indice
     */
    public void setIndice(int indice) {
        this.indice = indice;
    }

    /**
     *
     * @return
     */
    public List<UsuarioGrupoRol> getGrupoUsuarios() {
        return grupoUsuarios;
    }

    /**
     *
     * @param grupoUsuarios
     */
    public void setGrupoUsuarios(List<UsuarioGrupoRol> grupoUsuarios) {
        this.grupoUsuarios = grupoUsuarios;
    }

    /**
     *
     * @return
     */
    public UsuarioGrupoRol getGrup() {
        return grup;
    }

    /**
     *
     * @param grup
     */
    public void setGrup(UsuarioGrupoRol grup) {
        this.grup = grup;
    }

    /**
     *
     * @return
     */
    public List<UsuarioGrupoRol> getGrupo() {
        return grupo;
    }

    /**
     *
     * @param grupo
     */
    public void setGrupo(List<UsuarioGrupoRol> grupo) {
        this.grupo = grupo;
    }
    
     /**
     *
     * @return
     */
    public Usuario getIdusu() {
        return idusu;
    }

    /**
     *
     * @param idusu
     */
    public void setIdusu(Usuario idusu) {
        this.idusu = idusu;
    }

    /**
     *
     * @return
     */
    public List<Grupo> getGrupos() {
        return grupos;
    }

    /**
     *
     * @param grupos
     */
    public void setGrupos(List<Grupo> grupos) {
        this.grupos = grupos;
    }
    
    public String getGrupoPanel() {
        return grupoPanel;
    }

    public void setGrupoPanel(String GrupoPanel) {
        this.grupoPanel = GrupoPanel;
    }

    /**
     * Método constructor que se incia al hacer la llamada a la página
     * usuarioGrupo.xhml
     */
    @PostConstruct
    public void init() {
        
        //Creación de las listas con la información de los grupos
        grupoSeleccionado = new Grupo();
        grupos=listarGrupos();
        grupoSeleccionado = grupos.get(0);
        grupoPanel=grupoSeleccionado.getNombre();
        grupo = new ArrayList<UsuarioGrupoRol>();
        grupo=listarUsuariosGrupo(grupoSeleccionado,false);
        
        //Carga la información de la tabla usuario_grupo_rol dependiendo del grupo
        int j=0;
        grupoUsuarios=new ArrayList<UsuarioGrupoRol>();
        
        if(grupo.isEmpty()){
            grupoUsuarios=null;
        }
        while(grupo.size() > j){
            grup=grupo.get(j);
            if(grup.getIdGrupo().getId().compareTo(grupoSeleccionado.getId())!=0){
            }
            else{
                grupoUsuarios.add(grup);
            }
            j++;
        }
        
    }

    /**
     * Método que es llamado al momento de cambiar de grupo y lista 
     * los usuarios que posee dicho grupo
     * @param event un TabChangeEvent que indica si se ha seleccionado
     * otro grupo de la lista
     */
    public void botonActivado() {
       
       
        
        //Carga la información de la tabla usuario_grupo_rol dependiendo del grupo
      
        
        Grupo g = new Grupo();
        for (int i = 0; i < grupos.size(); i++) {
            if (grupos.get(i).getNombre().compareTo(grupoPanel) == 0) {
                g = grupos.get(i);
                indice = i;
                grupoSeleccionado=g;
            }
        }
          grupo = new ArrayList<UsuarioGrupoRol>();
       
        grupo=listarUsuariosGrupo(grupoSeleccionado,false);
        grupoUsuarios=new ArrayList<UsuarioGrupoRol>();
        
        if(grupo.isEmpty()){
            grupoUsuarios=null;
        }
        int j=0;
        while(grupo.size() > j){
            grup=grupo.get(j);
            if(grup.getIdGrupo().getId().compareTo(grupoSeleccionado.getId())==0){
                grupoUsuarios.add(grup);
            }
            j++;
            
        }
    }
    
    /**
     * Método que es llamado para ver las actividades que posee un usuario 
     * en especifico guarda el id del usuario en una variable de sesión y 
     * redirecciona a la página actividadesPorUsuario.xhtml
     */
    public void verActividadesUsuario(){
        
        idUsuario=grup.getIdUsuario().getId();
        usuarioGrupoSeleccionado = new Usuario();
        usuarioGrupoSeleccionado.setId(idUsuario);
        
        //Creación de la variable de Sesión para el id de usuario y del id de grupo
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        Object sessionInstancia = externalContext.getSession(true);
        HttpSession httpSession = (HttpSession) sessionInstancia;
        httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        httpSession.removeAttribute("IdUsuario");
        httpSession.removeAttribute("IdGrupo");
        httpSession.setAttribute("IdUsuario", usuarioGrupoSeleccionado);
        httpSession.setAttribute("IdGrupo", grupoSeleccionado);
        
        System.out.println("Usuariooooooo: "+usuarioGrupoSeleccionado.getId());
        
        try {
            FacesContext contex = FacesContext.getCurrentInstance();
            contex.getExternalContext().redirect("/HoriFarmaciaAnalistas/faces/actividadesPorUsuario.xhtml");
        } catch (Exception e) {
            System.out.println("----------------------------Error---------------------------------" + e);
        }
    }
    
    /**
     * Método que es llamado para mostrar la información del usuario seleccionado
     */
    public void verDatosUsuario(){
        
        idUsuario=grup.getIdUsuario().getId();
        usuarioGrupoSeleccionado = new Usuario();
        usuarioGrupoSeleccionado.setId(idUsuario);
        datosusuarios=buscarUsuario(usuarioGrupoSeleccionado);
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
            contex.getExternalContext().redirect("/HoriFarmaciaAnalistas/faces/index.xhtml");
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

    private java.util.List<com.seguroshorizonte.capadeservicios.servicios.Grupo> listarGrupos() {
        com.seguroshorizonte.capadeservicios.servicios.GestionDeGrupo port = service_2.getGestionDeGrupoPort();
        return port.listarGrupos();
    }

    private java.util.List<com.seguroshorizonte.capadeservicios.servicios.UsuarioGrupoRol> listarUsuariosGrupo(com.seguroshorizonte.capadeservicios.servicios.Grupo grupousuarios, boolean borrado) {
        com.seguroshorizonte.capadeservicios.servicios.GestionDeGrupo port = service_2.getGestionDeGrupoPort();
        return port.listarUsuariosGrupo(grupousuarios, borrado);
    }

    private Usuario buscarUsuario(com.seguroshorizonte.capadeservicios.servicios.Usuario usuarioActual) {
        com.seguroshorizonte.capadeservicios.servicios.GestionDeUsuarios port = service.getGestionDeUsuariosPort();
        return port.buscarUsuario(usuarioActual);
    }

    private boolean logSesion(com.seguroshorizonte.capadeservicios.servicios.Sesion sesionActual) {
        com.seguroshorizonte.capadeservicios.servicios.GestionDeControlDeUsuarios port = service_1.getGestionDeControlDeUsuariosPort();
        return port.logSesion(sesionActual);
    }

    private WrResultado logOut(com.seguroshorizonte.capadeservicios.servicios.Sesion sesionActual) {
        com.seguroshorizonte.capadeservicios.servicios.GestionDeControlDeUsuarios port = service_1.getGestionDeControlDeUsuariosPort();
        return port.logOut(sesionActual);
    }

}