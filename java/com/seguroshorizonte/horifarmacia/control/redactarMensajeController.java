package com.seguroshorizonte.horifarmacia.control;

import com.pangea.capadeservicios.servicios.GestionDeGrupo_Service;
import com.pangea.capadeservicios.servicios.Grupo;
import com.pangea.capadeservicios.servicios.Mensajeria_Service;
import com.pangea.capadeservicios.servicios.Post;
import com.pangea.capadeservicios.servicios.Rol;
import com.pangea.capadeservicios.servicios.Sesion;
import com.pangea.capadeservicios.servicios.Usuario;
import com.pangea.capadeservicios.servicios.UsuarioGrupoRol;
import com.pangea.capadeservicios.servicios.WrResultado;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.xml.ws.WebServiceRef;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author Pangeatech
 */
@ManagedBean(name = "redactarMensajeController")
@SessionScoped
public class redactarMensajeController {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_15362/CapaDeServicios/GestionDeGrupo.wsdl")
    private GestionDeGrupo_Service service_1;
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_15362/CapaDeServicios/Mensajeria.wsdl")
    private Mensajeria_Service service;
    private Usuario usuarioLogueo;
    /**
     * Cadena que guarda los contactos a los cuales se les enviara el mensaje
     */
    private String Para;
    /**
     * Cadena que guarda el asunto del mensaje
     */
    private String Asunto;
    /**
     * Cadena que guarda el cuerpo del mensaje
     *
     */
    private String Cuerpo;
    /**
     * Árbol en el cual se guardara los contactos existentes en cuanto a grupos,
     * roles y usuarios
     *
     */
    private TreeNode root;
    /**
     * Arbol en el cual se guardara el evento del contacto(nodo) seleccionado
     */
    private TreeNode selectedNode;

    /**
     * Método constructor en el cual se carga el arbol con los contactos a los
     * cuales se podra enviar el mensaje
     */
    @PostConstruct
    public void init() {
        root = new DefaultTreeNode("Root", null);
        List<Grupo> Grupos = listarGrupos();
        TreeNode nodosGrupos[] = new TreeNode[Grupos.size()];
        for (int i = 0; i < Grupos.size(); i++) {
            List< Rol> Roles = listarRolesPorGrupo(Grupos.get(i), false);
            TreeNode nodosRoles[] = new TreeNode[Roles.size()];
            if (Roles.size() > 0) {
                nodosGrupos[i] = new DefaultTreeNode(Grupos.get(i).getNombre() + "@grupo", root);
                for (int j = 0; j < Roles.size(); j++) {
                    List< UsuarioGrupoRol> Usuarios = listarUsuariosPorGrupoYRol(Grupos.get(i), Roles.get(j));
                    if (Usuarios.size() > 0) {
                        nodosRoles[j] = new DefaultTreeNode(Roles.get(j).getNombre() + "@rol", nodosGrupos[i]);
                        TreeNode nodosUsuarios[] = new TreeNode[Usuarios.size()];
                        for (int k = 0; k < Usuarios.size(); k++) {
                            nodosUsuarios[k] = new DefaultTreeNode(Usuarios.get(k).getIdUsuario().getId() + "@usuario", nodosRoles[j]);
                        }
                    }
                }
            }
        }
    }

    /**
     * Método en el cual se realiza el envio del mensaje
     */
    public void Envio() {
        usuarioLogueo = new Usuario();
        usuarioLogueo.setId("thunder");
        Post mensaje = new Post();
        mensaje.setDe(usuarioLogueo);
        mensaje.setPara(Para);
        mensaje.setAsunto(Asunto);
        mensaje.setTexto(Cuerpo);
        WrResultado envoltorio = enviarPost(mensaje);
        if (envoltorio.getEstatus().compareTo("OK") == 0) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensaje enviado", "El mensaje fue enviado"));
        } else {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia", envoltorio.getObservacion()));
        }
    }

    /**
     *
     * @return
     */
    public String getPara() {
        return Para;
    }

    /**
     *
     * @param Para
     */
    public void setPara(String Para) {
        this.Para = Para;
    }

    /**
     *
     * @return
     */
    public String getAsunto() {
        return Asunto;
    }

    /**
     *
     * @param Asunto
     */
    public void setAsunto(String Asunto) {
        this.Asunto = Asunto;
    }

    /**
     *
     * @return
     */
    public String getCuerpo() {
        return Cuerpo;
    }

    /**
     *
     * @param Cuerpo
     */
    public void setCuerpo(String Cuerpo) {
        this.Cuerpo = Cuerpo;
    }

    /**
     *
     * @return
     */
    public TreeNode getRoot() {
        return root;
    }

    /**
     *
     * @return
     */
    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    /**
     *
     * @param selectedNode
     */
    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    /**
     * Método llamado al seleccionar un contacto para agregarlo en el campo de
     * texto "Para"
     *
     * @param event
     */
    public void onNodeSelect(NodeSelectEvent event) {
        if (Para.compareTo("") == 0) {
            Para = event.getTreeNode().toString() + ";";
        } else {
            Para = Para + event.getTreeNode().toString() + ";";
        }
    }
//Servicios usados de la capa de servicios

    private WrResultado enviarPost(com.pangea.capadeservicios.servicios.Post mensajeActual) {
        com.pangea.capadeservicios.servicios.Mensajeria port = service.getMensajeriaPort();
        return port.enviarPost(mensajeActual);
    }

    private java.util.List<com.pangea.capadeservicios.servicios.Grupo> listarGrupos() {
        com.pangea.capadeservicios.servicios.GestionDeGrupo port = service_1.getGestionDeGrupoPort();
        return port.listarGrupos();
    }

    private java.util.List<com.pangea.capadeservicios.servicios.Rol> listarRolesPorGrupo(com.pangea.capadeservicios.servicios.Grupo grupousuarios, boolean borrado) {
        com.pangea.capadeservicios.servicios.GestionDeGrupo port = service_1.getGestionDeGrupoPort();
        return port.listarRolesPorGrupo(grupousuarios, borrado);
    }

    private java.util.List<com.pangea.capadeservicios.servicios.UsuarioGrupoRol> listarUsuariosPorGrupoYRol(com.pangea.capadeservicios.servicios.Grupo grupousuarios, com.pangea.capadeservicios.servicios.Rol roles) {
        com.pangea.capadeservicios.servicios.GestionDeGrupo port = service_1.getGestionDeGrupoPort();
        return port.listarUsuariosPorGrupoYRol(grupousuarios, roles);
    }
}