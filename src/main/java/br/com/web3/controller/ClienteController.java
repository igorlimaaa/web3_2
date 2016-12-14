package br.com.web3.controller;

import static br.com.web3.controller.ClienteController.clienteListener;
import br.com.web3.dao.ClienteDAO;
import br.com.web3.dao.VendaDAO;
import br.com.web3.model.Cliente;
import br.com.web3.model.Venda;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.primefaces.context.RequestContext;

@ManagedBean
@SessionScoped
public class ClienteController {

    private Cliente cliente;
    private ClienteDAO clienteDAO;
    private Venda venda;
    private VendaDAO vendaDAO;
    private List<Cliente> clienteList;
    public static List<Cliente> clienteListener;

    public ClienteController() {
        clienteDAO = new ClienteDAO();
        cliente = new Cliente();
        clienteListener = new ArrayList<Cliente>();
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void novo(ActionEvent actionEvent) {
        cliente = new Cliente();
    }

    public void gravar(ActionEvent actionEvent) {

        clienteDAO.cadastrarCliente(cliente);
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("dialogCadastrarCliente.hide()");

    }
      
    public void excluirCliente(ActionEvent actionEvent) {
        clienteDAO.excluirCliente(cliente);
    }

    public ClienteDAO getClienteDAO() {
        return clienteDAO;
    }

    public void setClienteDAO(ClienteDAO clienteDAO) {
        this.clienteDAO = clienteDAO;
    }

    public Venda getVenda() {
        return venda;
    }

    public void setVenda(Venda venda) {
        this.venda = venda;
    }

    public VendaDAO getVendaDAO() {
        return vendaDAO;
    }

    public void setVendaDAO(VendaDAO vendaDAO) {
        this.vendaDAO = vendaDAO;
    }

    public List<Cliente> getClienteList() {
        clienteList = clienteDAO.getList();
        return clienteList;
    }

    public void setClienteList(List<Cliente> clienteList) {
        this.clienteList = clienteList;
    }

    public static List<Cliente> getClienteListener() {
        return clienteListener;
    }

    public static void setClienteListener(List<Cliente> clienteListener) {
        ClienteController.clienteListener = clienteListener;
    }
    
    

}
