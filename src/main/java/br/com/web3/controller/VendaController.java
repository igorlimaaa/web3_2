package br.com.web3.controller;

import br.com.web3.dao.ClienteDAO;
import br.com.web3.dao.ItensVendaDAO;
import br.com.web3.dao.ProdutoDAO;
import br.com.web3.dao.VendaDAO;
import br.com.web3.model.Cliente;
import br.com.web3.model.ItensVenda;
import br.com.web3.model.Produto;
import br.com.web3.model.Venda;
import br.com.web3.relatorio.Relatorio;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.primefaces.context.RequestContext;

@ManagedBean
@SessionScoped
public class VendaController implements Serializable {

    private Venda venda;
    private ItensVenda itensVenda;
    private Produto produto;
    private Relatorio relatorio;
    private Cliente cliente;

    private VendaDAO vendaDAO;
    private ProdutoDAO produtoDAO;
    private ItensVendaDAO itensVendaDAO;
    private ClienteDAO clienteDAO;

    private List<Venda> vendaList;
    private List<ItensVenda> carrinho;
    public static List<Produto> PRODUTOS;
    public static List<Cliente> CLIENTES;

    private Boolean desabilitar = true;
    private String tipoRel;

    public VendaController() {
        vendaDAO = new VendaDAO();
        produtoDAO = new ProdutoDAO();
        itensVendaDAO = new ItensVendaDAO();
        PRODUTOS = new ArrayList<Produto>();
        CLIENTES = new ArrayList<Cliente>();
        relatorio = new Relatorio();
        tipoRel = "PDF";
    }

    private void lista() {
        PRODUTOS.clear();
        for (Produto p : produtoDAO.pesquisarTodos()) {
            PRODUTOS.add(p);
        }
//        for (Cliente q : clienteDAO.getList()){
//            CLIENTES.add(q);
//        }
    }
    
    private void listaC(){
        CLIENTES.clear();
        for (Cliente q : clienteDAO.getList()){
            CLIENTES.add(q);
        }
    }

    public void novo(ActionEvent actionEvent) {
        
        lista();
        //listaC();
        carrinho = new ArrayList<ItensVenda>();
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("formCadastro:tabProduto");
        context.update("formCadastro:produto");
        context.update("formCadastro:cliente");        
        venda = new Venda();
        //cliente = new Cliente();
        //produto = new Produto();
        itensVenda = new ItensVenda();
    }

    public void gravar() {
        boolean gravar = true;
        String descProduto = "";
        String nameCliente = "";
        for (ItensVenda iv : carrinho) {
            descProduto = iv.getProduto().getDescricao();
            if (iv.getQuantidade() > iv.getProduto().getQuantidade()) {
                gravar = false;
                break;
            }
            
//            for (ItensVenda ive :carrinho){
//                nameCliente = ive.getCliente().getNomeCompleto();
//            }
        }
        if (gravar) {
            vendaDAO.save(venda, carrinho);
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("dialogCadastrarVenda.hide()");
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Produto fora de estoque: " + descProduto, "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public List<ItensVenda> adicionarProdutos() {
        boolean valid = true;
        
        if (cliente == null) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Cliente não informado", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            valid = false;
        }

        if (produto == null) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Produto não informado", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            valid = false;
        }

        if (itensVenda.getQuantidade() <= 0) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "A quantidade não pode ser igual ou menor que 0", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            valid = false;
        }

        if (valid) {

            ItensVenda iv = new ItensVenda();
            iv.setCliente(cliente);
            iv.setProduto(produto);
            iv.setQuantidade(itensVenda.getQuantidade());
            carrinho.add(iv);
            itensVenda = new ItensVenda();
            cliente = new Cliente();
            produto = new Produto();
            desabilitar = false;

        }

        return carrinho;
    }
    
    public void gerarRelatorio() {
        List<Venda> listaVenda = null;
        listaVenda = vendaDAO.getList(venda.getCodigo());

        List<ItensVenda> listagemSubRel = itensVendaDAO.getListByVenda(venda);
        HashMap paramRel = new HashMap();
        paramRel.put("titulo", "Venda: " + venda.getCodigo());
        String nomeRelatorio = "relVendas";
        String subNomeRelatorio = "relItensVenda";
        relatorio.gerarRelatorioSub(nomeRelatorio, paramRel, listaVenda, 
                listagemSubRel, subNomeRelatorio, tipoRel);
    }

    public void excluirProduto(ItensVenda iv) {
        carrinho.remove(iv);
    }

    public List<Produto> getProdutos() {
        return PRODUTOS;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Venda getVenda() {
        return venda;
    }

    public void setVenda(Venda venda) {
        this.venda = venda;
    }

    public List<Venda> getVendaList() {
        vendaList = vendaDAO.getList();
        return vendaList;
    }

    public List<ItensVenda> getCarrinho() {
        return carrinho;
    }

    public void setCarrinho(List<ItensVenda> carrinho) {
        this.carrinho = carrinho;
    }

    public ItensVenda getItensVenda() {
        return itensVenda;
    }

    public void setItensVenda(ItensVenda itensVenda) {
        this.itensVenda = itensVenda;
    }

    public Boolean getDesabilitar() {
        return desabilitar;
    }

    public void setDesabilitar(Boolean desabilitar) {
        this.desabilitar = desabilitar;
    }

    public String getTipoRel() {
        return tipoRel;
    }

    public void setTipoRel(String tipoRel) {
        this.tipoRel = tipoRel;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<Cliente> getCLIENTES() {
        return CLIENTES;
    }
    
    

}
