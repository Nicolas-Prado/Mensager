/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import org.primefaces.PrimeFaces;
import persistencia.MensageiroDAO;
import persistencia.UsuarioDAO;
import vo.Mensageiro;
import vo.Usuario;

/**
 *
 * @author 2info2020
 */
@ManagedBean
@SessionScoped
public class TelaMensagemBean implements Serializable {

    private Usuario usuario = new Usuario();
    private Mensageiro mensageiro = new Mensageiro();
    private UsuarioDAO pu = new UsuarioDAO();
    private MensageiroDAO pm = new MensageiroDAO();
    private DataModel<Usuario> usuarios;
    private DataModel<Mensageiro> mensagens;
    private List<Usuario> listaUsuarios;
    private List<String> listaMembros;
    private String destino;

    public String login() {
        Usuario usuarioativo = pu.localiza(usuario.getLogin());
        if (usuario.getLogin().equals("admin") && usuario.getSenha().equals("1234")) {
            usuario = new Usuario();
            return "cadastro";
        }
        if (usuarioativo != null && usuario.getSenha().equals(usuarioativo.getSenha())) {
            mensagens = new ListDataModel(pm.mensagensusu(usuarioativo.getLogin()));
            usuario=usuarioativo;
            return "tprincipal";
        } else {
            usuario = new Usuario();
            FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_INFO, "Login ou senha errada!", "Login ou senha errada!");
            PrimeFaces.current().dialog().showMessageDynamic(fm);
            return "index";
        }
    }

    public String cadastrar() {
        Usuario usuariotest = new Usuario();
        usuariotest = pu.localiza(usuario.getLogin());
        if (usuariotest == null) {

            pu.salva(usuario);
            usuario = new Usuario();
            FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_INFO, "O usuario foi cadastrado com sucesso!", "O usuario foi cadastrado com sucesso!");
            PrimeFaces.current().dialog().showMessageDynamic(fm);
            return "cadastro";
        } else {
            usuario = new Usuario();
            FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_INFO, "O usuario ja existe!", "O usuario ja existe!");
            PrimeFaces.current().dialog().showMessageDynamic(fm);
            return "cadastro";
        }
    }

    public String gocadastro() {
        usuario = new Usuario();
        return "cadastro";
    }

    public String goteleditar() {
        usuario = new Usuario();
        usuarios = (DataModel<Usuario>) new ListDataModel(pu.listausuario());
        return "escedita";
    }

    public String goeditar() {
        usuario = usuarios.getRowData();
        return "edita";
    }

    public String goindex() {
        usuario = new Usuario();
        return "index";
    }

    public String vermensagem() {
        mensageiro = mensagens.getRowData();
        return mensageiro.getMensagem();
    }

    public String goenviar() {
        mensageiro = new Mensageiro();
        listaUsuarios = pu.listausuario();
        listaMembros = new ArrayList();
        for (int c = 0; c < listaUsuarios.size(); c++) {
            listaMembros.add(listaUsuarios.get(c).getLogin());
        }
        return "enviar";
    }

    public void editar() {
        pu.edita(usuario);
        FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_INFO, "O usuario foi editado com sucesso!", "O usuario foi editado com sucesso!");
        PrimeFaces.current().dialog().showMessageDynamic(fm);
    }

    public String enviar() {
        mensageiro.setRemetente(usuario.getLogin());
        Date date = new Date();
        mensageiro.setDataa(date);
        pm.salva(mensageiro);
        mensageiro = new Mensageiro();
        return "tprincipal";
    }

    public String excluirusu() {
        if (pm.mensagensusu(usuario.getLogin()).isEmpty()==true && pm.recebidausu(usuario.getLogin()).isEmpty()==true) {
            pu.exclui(usuario);
            FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_INFO, "O usuario foi excluido com sucesso!", "O usuario foi excluido com sucesso!");
            PrimeFaces.current().dialog().showMessageDynamic(fm);
            usuario = new Usuario();
            return "escedita";
        } else {
            FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO. Nao foi possivel deletar o usuario", "O usuario possui mensagens enviadas ou recebidas");
            PrimeFaces.current().dialog().showMessageDynamic(fm);
            return "";
        }
    }
    
    public void excluirmensagem(){
        pm.exclui(mensageiro);
        mensagens = new ListDataModel(pm.mensagensusu(usuario.getLogin()));
        mensageiro = new Mensageiro();
    }

    public String voltarde() {
        mensageiro = new Mensageiro();
        return "tprincipal";
    }
    
    public String responder(){
        Mensageiro agora = new Mensageiro();
        agora.setAssunto("Re:"+mensageiro.getAssunto());
        agora.setRemetente(usuario.getLogin());
        agora.setDestinatario(mensageiro.getRemetente()); 
        mensageiro = new Mensageiro();
        mensageiro = agora;
        mensageiro.setId(0);
        return "resposta";
    }
    
    public void atualizar(){
        mensagens = new ListDataModel(pm.mensagensusu(usuario.getLogin()));
    }
    
    

    /**
     * @return the usuario
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * @param usuario the usuario to set
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * @return the mensageiro
     */
    public Mensageiro getMensageiro() {
        //System.out.println(mensageiro.getId()+mensageiro.getMensagem());
        return mensageiro;
    }

    /**
     * @param mensageiro the mensageiro to set
     */
    public void setMensageiro(Mensageiro mensageiro) {
        this.mensageiro = mensageiro;
    }

    /**
     * @return the pu
     */
    public UsuarioDAO getPu() {
        return pu;
    }

    /**
     * @param pu the pu to set
     */
    public void setPu(UsuarioDAO pu) {
        this.pu = pu;
    }

    /**
     * @return the pm
     */
    public MensageiroDAO getPm() {
        return pm;
    }

    /**
     * @param pm the pm to set
     */
    public void setPm(MensageiroDAO pm) {
        this.pm = pm;
    }

    /**
     * @return the usuarios
     */
    public DataModel<Usuario> getUsuarios() {
        return usuarios;
    }

    /**
     * @param usuarios the usuarios to set
     */
    public void setUsuarios(DataModel<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    /**
     * @return the mensagens
     */
    public DataModel<Mensageiro> getMensagens() {
        return mensagens;
    }

    /**
     * @param mensagens the mensagens to set
     */
    public void setMensagens(DataModel<Mensageiro> mensagens) {
        this.mensagens = mensagens;
    }

    /**
     * @return the listaUsuarios
     */
    public List<Usuario> getListaUsuarios() {
        return listaUsuarios;
    }

    /**
     * @param listaUsuarios the listaUsuarios to set
     */
    public void setListaUsuarios(List<Usuario> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    /**
     * @return the listaMembros
     */
    public List<String> getListaMembros() {
        return listaMembros;
    }

    /**
     * @param listaMembros the listaMembros to set
     */
    public void setListaMembros(List<String> listaMembros) {
        this.listaMembros = listaMembros;
    }

    /**
     * @return the destino
     */
    public String getDestino() {
        return destino;
    }

    /**
     * @param destino the destino to set
     */
    public void setDestino(String destino) {
        this.destino = destino;
    }
}
