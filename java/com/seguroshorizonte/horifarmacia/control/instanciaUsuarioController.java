/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.seguroshorizonte.horifarmacia.control;

import com.seguroshorizonte.capadeservicios.servicios.GestionDeControlDeUsuarios_Service;
import com.seguroshorizonte.capadeservicios.servicios.GestionDeInstancias_Service;
import com.seguroshorizonte.capadeservicios.servicios.Instancia;
import com.seguroshorizonte.capadeservicios.servicios.Sesion;
import com.seguroshorizonte.capadeservicios.servicios.Usuario;
import com.seguroshorizonte.capadeservicios.servicios.WrInstancia;
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
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.WebServiceRef;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author PANGEA
 */
@ManagedBean(name = "instanciaUsuarioController")
@SessionScoped
public class instanciaUsuarioController {
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_15362/CapaDeServicios/GestionDeControlDeUsuarios.wsdl")
    private GestionDeControlDeUsuarios_Service service_1;
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_15362/CapaDeServicios/GestionDeInstancias.wsdl")
    private GestionDeInstancias_Service service;
    
    /*
     * Objeto de la clase Usuario donde se guardara el objeto de la variable de sesión
     */
    Usuario usuarioLogueo;
    /*
     * Objeto de la clase Sesión donde se guardara el objeto de la variable de sesión
     */
    Sesion sesionLogueo;
    
    /*
     * Objetos de la clase Usuario donde se guardara el obejto de la variable id 
     * de usuario
     */
    private Usuario idusu, idusuario;
    
    /*
     * Objetos de la clase TreeNode en donde se guardara los objetos de la variable 
     * estados que se seleccionan en el arbol
     */
    private TreeNode estact, estadoSeleccionado;
    
    /*
     * Objetos de la clase List<String> en donde se guardara los obejtos de la 
     * variable estado que se cargan o se consultan del servicio
     */
    private List<String> estados;
    
    /*
     * Objetos de la clase WrInstancia en donde se guardara los obejtos de las 
     * instancias que se cargan o se consultan de los servicios
     */
    private WrInstancia instancia, instac;
    
    /*
     * Objetos de la clase List<Instancia> en donde se guardara  la lista de 
     * instancias que se cargan o se consultan en el serivicio
     */
    private List<Instancia> instancias, instanciasOtra;
    
    /*
     * Objetos de la clase Instancia donde se guardara los objetos de la variables
     * de instancia que se cargan o consultan del servicio
     */
    private Instancia inst, insta, instActividad, instCerrar;
    
    /*
     * Objetos de la clase WrResultado en donde se guardara los objetos de la 
     * variable de WrResultado que se cargan o consultan del servicio
     */
    private WrResultado instanciacerrar;
    
    /*
     * Objeto de la clase Sesion en donde se guardara la variable de la sesion 
     * abierta
     */
    private Sesion ses;
    
    /*
     * Objeto de tipo Long en donde se guardara la variable del id de la instancia
     */
    private Long idInsta;
    
    /*
     * Objeto de tipo String en donde se guardara la variable del id del usuario
     */
    private String usuario;
    
    /**
     *
     * @return
     */
    public WrResultado getInstanciacerrar() {
        return instanciacerrar;
    }

    /**
     *
     * @param instanciacerrar
     */
    public void setInstanciacerrar(WrResultado instanciacerrar) {
        this.instanciacerrar = instanciacerrar;
    }
    
    /**
     *
     * @return
     */
    public Instancia getInstCerrar() {
        return instCerrar;
    }

    /**
     *
     * @param instCerrar
     */
    public void setInstCerrar(Instancia instCerrar) {
        this.instCerrar = instCerrar;
    }
    
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
    public Usuario getIdusuario() {
        return idusuario;
    }

    /**
     *
     * @param idusuario
     */
    public void setIdusuario(Usuario idusuario) {
        this.idusuario = idusuario;
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
    public WrInstancia getInstac() {
        return instac;
    }

    /**
     *
     * @param instac
     */
    public void setInstac(WrInstancia instac) {
        this.instac = instac;
    }

    /**
     *
     * @return
     */
    public Instancia getInstActividad() {
        return instActividad;
    }

    /**
     *
     * @param instActividad
     */
    public void setInstActividad(Instancia instActividad) {
        this.instActividad = instActividad;
    }

    /**
     *
     * @return
     */
    public Long getIdInsta() {
        return idInsta;
    }

    /**
     *
     * @param idInsta
     */
    public void setIdInsta(Long idInsta) {
        this.idInsta = idInsta;
    }

    /**
     *
     * @return
     */
    public TreeNode getEstadoSeleccionado() {
        return estadoSeleccionado;
    }

    /**
     *
     * @param estadoSeleccionado
     */
    public void setEstadoSeleccionado(TreeNode estadoSeleccionado) {
        this.estadoSeleccionado = estadoSeleccionado;
    }

    /**
     *
     * @return
     */
    public Instancia getInsta() {
        return insta;
    }

    /**
     *
     * @param insta
     */
    public void setInsta(Instancia insta) {
        this.insta = insta;
    }

    /**
     *
     * @return
     */
    public WrInstancia getInstancia() {
        return instancia;
    }

    /**
     *
     * @param instancia
     */
    public void setInstancia(WrInstancia instancia) {
        this.instancia = instancia;
    }

    /**
     *
     * @return
     */
    public List<Instancia> getInstancias() {
        return instancias;
    }

    /**
     *
     * @param instancias
     */
    public void setInstancias(List<Instancia> instancias) {
        this.instancias = instancias;
    }

    /**
     *
     * @return
     */
    public Instancia getInst() {
        return inst;
    }

    /**
     *
     * @param inst
     */
    public void setInst(Instancia inst) {
        this.inst = inst;
    }

    /**
     *
     * @return
     */
    public TreeNode getSelectedNode() {
        return estact;
    }

    /**
     *
     * @param selectedNode
     */
    public void setSelectedNode(TreeNode selectedNode) {
        this.estact = selectedNode;
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
    public TreeNode getEstact() {
        return estact;
    }

    /**
     *
     * @param estact
     */
    public void setEstact(TreeNode estact) {
        this.estact = estact;
    }

    /**
     *
     * @return
     */
    public List<String> getEstados() {
        return estados;
    }

    /**
     *
     * @param estados
     */
    public void setEstados(List<String> estados) {
        this.estados = estados;
    }
    
    /**
     * Metodo constructor que se incia al hacer la llamada a la página
     * instanciaUsuario.xhml
     */
    @PostConstruct
    public void init() {
        
        //Llamo al método verificar logueo apenas ingrese al xhtml
        verificarLogueo();
        
        estact = new DefaultTreeNode("root", null);
        idusu = new Usuario();        
        idusu.setId(usuario);
        
        String icono;
        estados = buscarEstados();
        int i = 0;
        
        //Carga el arbol con los estados existentes
        while (estados.size() > i) {
            if ("abierta".equals(estados.get(i))) {
                icono = "a";
                TreeNode inbox = new DefaultTreeNode(icono, estados.get(i), estact);
            } else if ("cerrada".equals(estados.get(i))) {
                icono = "c";
                TreeNode inbox = new DefaultTreeNode(icono, estados.get(i), estact);
            }
            i++;
        }
        estadoSeleccionado = estact.getChildren().get(0);
        estact.getChildren().get(0).setSelected(true);
        
        //Carga la lista de instancias dependiendo del estado y del usuario
        int j = 0;
        insta = new Instancia();
        insta.setEstado(estados.get(j));
        instancia = consultarInstancias(idusu, insta);
        instancias = new ArrayList<Instancia>();
        if (instancia.getInstancias().isEmpty()) {
            instancias = null;
        }
        else{
            instancias=instancia.getInstancias();
        }
        
    }

    /**
     * Método que recarga la información del arbol dependiendo del 
     * estado que se seleccione
     * @param event un onNodeSelect que indica si se ha seleccionado
     * la opción abierta o cerrada
     */
    public void onNodeSelect(NodeSelectEvent event) {
        
        //Carga la lista de instancias dependiendo del estado y del usuario
        int j = 0;
        insta = new Instancia();
        insta.setEstado(event.getTreeNode().toString());
        instancia = consultarInstancias(idusu, insta);
        instancias = new ArrayList<Instancia>();
        if (instancia.getInstancias().isEmpty()) {
            instancias = null;
        }
       else{
            instancias=instancia.getInstancias();
        }
        
    }
    
    /**
     * Método que recibe el id de la instancia a la que se desea consultar 
     * las actividades asociadas a ella y crea una variable de sesión con 
     * dicho id y redirecciona a actividadesPorInstancia.xhtml
     */
    public void listarActividades(){
        
        idInsta=inst.getId();
        instActividad= new Instancia();
        instActividad.setId(idInsta);
       
        //Creación de la variable de Sesión con el id de la instancia
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        Object sessionInstancia = externalContext.getSession(true);
        HttpSession httpSession = (HttpSession) sessionInstancia;
        httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        httpSession.removeAttribute("IdInstancia");
        httpSession.setAttribute("IdInstancia", instActividad);
        
        try {
            FacesContext contex = FacesContext.getCurrentInstance();
            contex.getExternalContext().redirect("/PangeaFlowProyecto/faces/actividadesPorInstancia.xhtml");
        } catch (Exception e) {
            System.out.println("----------------------------Error---------------------------------" + e);
        }
        
        
    }
    
    /**
     * Método con el cual se cierra una instancia, se necesitan la sesión 
     * actual y la instancia que se desea cerrar y recargo la información
     */
    
    public void cerrarInstanciaSeleccionada(){
        
        idInsta=inst.getId();
        instCerrar= new Instancia();
        instCerrar.setId(idInsta);
        instanciacerrar=cerrarInstancia(instCerrar, ses);
        
        //Carga la lista de instancias dependiendo del estado y del usuario
        int j = 0;
        insta = new Instancia();
        insta.setEstado(estados.get(j));
        instancia = consultarInstancias(idusu, insta);
        instancias = new ArrayList<Instancia>();
        if (instancia.getInstancias().isEmpty()) {
            instancias = null;
        }
        else{
            instancias=instancia.getInstancias();
        }
        
    }
    
    /**
     * Metodo que permite colocar el estilo de una fila mediante un color
     *
     * @param instanciaPintar parametro que indica la condición para determinar 
     * si se pinta la fila en rosa o blanco si se ha pasado la fecha de cerrar 
     * o no en las instancias que aun estan abiertas
     * @return el color del sombreado de la instancia abierta
     * @throws DatatypeConfigurationException
     */
    public String estiloAbierta(Instancia instanciaPintar) throws DatatypeConfigurationException {
        
      if(instanciaPintar!=null){
          if(instanciaPintar.getFechaCierre()!=null){
            XMLGregorianCalendar cod;
            cod=instanciaPintar.getFechaCierre();
            Date fech=cod.toGregorianCalendar().getTime();
            Date fecha= new Date();
          
            if(fech.before(fecha)) {
                return "background-color: #FF8888;";
            }
          }
          return " background-color: #FFFFFF;";
          }
       return " background-color: #FFFFFF;";
    }
    
    /**
     * Metodo que permite colocar el estilo de una fila mediante un color
     *
     * @param instanciaPintar parametro que indica la condición para determinar 
     * si se pinta la fila en rosa o blanco si se paso la fecha de periodo 
     * o no en las instancias que estan cerradas
     * @return el color del sombreado de la instancia cerrada
     * @throws DatatypeConfigurationException
     */
    public String estiloCerrada(Instancia instanciaPintar) throws DatatypeConfigurationException {
        
      if(instanciaPintar!=null){
          int j=0;
          if(instanciaPintar.getIdPeriodoGrupoProceso().getIdPeriodo().getFechaHasta()!=null && instanciaPintar.getFechaCierre()!=null){
            XMLGregorianCalendar cierre, hasta;
            
            cierre=instanciaPintar.getFechaCierre();
            hasta=instanciaPintar.getIdPeriodoGrupoProceso().getIdPeriodo().getFechaHasta();
            
            Date fechainstancia=cierre.toGregorianCalendar().getTime();
            Date fechaperiodo=hasta.toGregorianCalendar().getTime();
          
            if(fechainstancia.after(fechaperiodo)){
                return "background-color: #FF8888;";
            }
          }
          j=j+1;
          return " background-color: #FFFFFF;";
          }
       return " background-color: #FFFFFF;";
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

    private WrInstancia consultarInstancias(com.seguroshorizonte.capadeservicios.servicios.Usuario usuarioActual, com.seguroshorizonte.capadeservicios.servicios.Instancia instanciaActual) {
        com.seguroshorizonte.capadeservicios.servicios.GestionDeInstancias port = service.getGestionDeInstanciasPort();
        return port.consultarInstancias(usuarioActual, instanciaActual);
    }

    private WrResultado logOut(com.seguroshorizonte.capadeservicios.servicios.Sesion sesionActual) {
        com.seguroshorizonte.capadeservicios.servicios.GestionDeControlDeUsuarios port = service_1.getGestionDeControlDeUsuariosPort();
        return port.logOut(sesionActual);
    }

    private boolean logSesion(com.seguroshorizonte.capadeservicios.servicios.Sesion sesionActual) {
        com.seguroshorizonte.capadeservicios.servicios.GestionDeControlDeUsuarios port = service_1.getGestionDeControlDeUsuariosPort();
        return port.logSesion(sesionActual);
    }

    private java.util.List<java.lang.String> buscarEstados() {
        com.seguroshorizonte.capadeservicios.servicios.GestionDeInstancias port = service.getGestionDeInstanciasPort();
        return port.buscarEstados();
    }

    private WrResultado cerrarInstancia(com.seguroshorizonte.capadeservicios.servicios.Instancia instanciaActual, com.seguroshorizonte.capadeservicios.servicios.Sesion sesionActual) {
        com.seguroshorizonte.capadeservicios.servicios.GestionDeInstancias port = service.getGestionDeInstanciasPort();
        return port.cerrarInstancia(instanciaActual, sesionActual);
    }

}
