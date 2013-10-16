package com.seguroshorizonte.horifarmacia.control;

import com.seguroshorizonte.capadeservicios.servicios.GestionDeGrupo_Service;
import com.seguroshorizonte.capadeservicios.servicios.Grupo;
import com.seguroshorizonte.capadeservicios.servicios.Mensajeria_Service;
import com.seguroshorizonte.capadeservicios.servicios.Post;
import com.seguroshorizonte.capadeservicios.servicios.Rol;
import com.seguroshorizonte.capadeservicios.servicios.Sesion;
import com.seguroshorizonte.capadeservicios.servicios.Usuario;
import com.seguroshorizonte.capadeservicios.servicios.UsuarioGrupoRol;
import com.seguroshorizonte.capadeservicios.servicios.WrResultado;
import com.seguroshorizonte.capadeservicios.servicios.WrRol;
import com.seguroshorizonte.capadeservicios.servicios.WrUsuarioGrupoRol;
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
 * @author HoriFarmaciaAnalistas
 */
@ManagedBean(name = "redactarMensajeController")
@SessionScoped
public class redactarMensajeController {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_15362/CapaDeServiciosAnalistas/GestionDeGrupo.wsdl")
    private GestionDeGrupo_Service service_1;
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_15362/CapaDeServiciosAnalistas/Mensajeria.wsdl")
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
      for (int ii = 0; ii < Grupos.size(); ii++) {
                WrRol resultadoRol = listarRolesPorGrupo(Grupos.get(ii), false);
                if (resultadoRol.getEstatus().compareTo("OK") == 0) {
                    List< Rol> Roles = resultadoRol.getRols();
                    TreeNode nodosRoles[] = new TreeNode[Roles.size()];
                    if (Roles.size() > 0) {
                        nodosGrupos[ii] = new DefaultTreeNode(Grupos.get(ii).getNombre() + "@grupo", root);
                        for (int jj = 0; jj < Roles.size(); jj++) {
                            WrUsuarioGrupoRol resultadoLista = listarUsuariosPorGrupoYRol(Grupos.get(ii), Roles.get(jj));
                            if (resultadoLista.getEstatus().compareTo("OK") == 0) {
                                List< UsuarioGrupoRol> Usuarios = resultadoLista.getUsuarioGrupoRols();
                                if (Usuarios.size() > 0) {
                                    nodosRoles[jj] = new DefaultTreeNode(Roles.get(jj).getNombre() + "@rol", nodosGrupos[ii]);
                                    TreeNode nodosUsuarios[] = new TreeNode[Usuarios.size()];
                                    for (int k = 0; k < Usuarios.size(); k++) {
                                        nodosUsuarios[k] = new DefaultTreeNode(Usuarios.get(k).getIdUsuario().getId() + "@usuario", nodosRoles[jj]);
                                    }
                                }
                            } else {
                                System.out.println("No se pudo mostrar debido a que " + resultadoLista.getObservacion());
                            }

                        }
                    }
                } else {
                    System.out.println("No se pudo mostrar debido a que " + resultadoRol.getObservacion());
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

    private WrResultado enviarPost(com.seguroshorizonte.capadeservicios.servicios.Post mensajeActual) {
        com.seguroshorizonte.capadeservicios.servicios.Mensajeria port = service.getMensajeriaPort();
        return port.enviarPost(mensajeActual);
    }

    private java.util.List<com.seguroshorizonte.capadeservicios.servicios.Grupo> listarGrupos() {
        com.seguroshorizonte.capadeservicios.servicios.GestionDeGrupo port = service_1.getGestionDeGrupoPort();
        return port.listarGrupos();
    }

    private WrRol listarRolesPorGrupo(com.seguroshorizonte.capadeservicios.servicios.Grupo grupousuarios, boolean borrado) {
        com.seguroshorizonte.capadeservicios.servicios.GestionDeGrupo port = service_1.getGestionDeGrupoPort();
        return port.listarRolesPorGrupo(grupousuarios, borrado);
    }

    private WrUsuarioGrupoRol listarUsuariosPorGrupoYRol(com.seguroshorizonte.capadeservicios.servicios.Grupo grupousuarios, com.seguroshorizonte.capadeservicios.servicios.Rol roles) {
        com.seguroshorizonte.capadeservicios.servicios.GestionDeGrupo port = service_1.getGestionDeGrupoPort();
        return port.listarUsuariosPorGrupoYRol(grupousuarios, roles);
    }


}
