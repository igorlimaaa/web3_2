package br.com.web3.controller;

import br.com.web3.dao.ItensVendaDAO;
import br.com.web3.dao.ProdutoDAO;
import br.com.web3.dao.TipoProdutoDAO;
import br.com.web3.dao.VendaDAO;
import br.com.web3.model.ItensVenda;
import br.com.web3.model.Produto;
import br.com.web3.model.TipoProduto;
import br.com.web3.model.Venda;
import br.com.web3.relatorio.Relatorio;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletContext;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;

@ManagedBean
@SessionScoped
public class ProdutoController {

    private Produto produto;
    private ProdutoDAO produtoDAO;
    private List<Produto> produtoList;
    private List<String> images;
    private TipoProdutoDAO tipoProdutoDAO;
    private Relatorio relatorio;
    public static List<TipoProduto> tipos;
    private TipoProduto tipoProduto;
    private ItensVendaDAO itensvendaDAO;
    private ItensVenda itensvenda;
    private Venda venda;
    private VendaDAO vendaDAO;

    public ProdutoController() {
        produtoDAO = new ProdutoDAO();
        tipoProdutoDAO = new TipoProdutoDAO();
        tipos = new ArrayList<TipoProduto>();
        relatorio = new Relatorio();
    }

    private void lista() {
        tipos.clear();
        for (TipoProduto p : tipoProdutoDAO.pesquisarTodos()) {
            tipos.add(p);
        }
    }

    public List<Produto> getProdutoList() {
        produtoList = produtoDAO.pesquisarTodos();
        return produtoList;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    // Incluir novo produto
    public void novo(ActionEvent actionEvent) {
        lista();
        produto = new Produto();
    }

    // Incluir novo produto
    public void gravar(ActionEvent actionEvent) {

        if (tipoProduto != null) {

            produto.setTipoProduto(tipoProduto);
            produtoDAO.inserir(produto);
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("dialogCadastrarProduto.hide()");

        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "A categoria não foi informada.", ""));
        }

    }

    // Alterar novo produto
    public void inicioAlterarProduto(ActionEvent actionEvent) {
        //tipoProduto = produto.getTipoProduto();

    }

    // Alterar novo produto
    public void fimAlterarProduto(ActionEvent actionEvent) {
        produtoDAO.atualizar(produto);
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("dialogCadastrarProduto.hide()");
    }

    // Excluir novo produto
    public void excluirProduto(Produto produto) {

        boolean erro = produtoDAO.excluir(produto/*, itensvenda, venda*/);

        if (erro) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Exclusão não pode ser realizada! Produto associado à venda.", ""));
        }
    }

    public void doUpload(FileUploadEvent fileUploadEvent) {
        try {

            FacesContext facesContext = FacesContext.getCurrentInstance();
            ServletContext context = (ServletContext) facesContext.getExternalContext().getContext();

            String realPath = context.getRealPath("/");

            // Aqui cria o diretorio caso não exista 
            File file = new File(realPath + "/resources/imagens/" + produto.getCodigo() + "/");
            file.mkdirs();

            InputStream inputStream = fileUploadEvent.getFile().getInputstream();

            OutputStream out = new FileOutputStream(new File(file,
                    fileUploadEvent.getFile().getFileName()));

            int read = 0;

            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {

                out.write(bytes, 0, read);

            }

            inputStream.close();

            out.flush();

            out.close();

        } catch (Exception ex) {
            System.out.println("Erro no upload de imagem" + ex);
        }
    }

    public void visualizarImagem() {
        getImages();
    }

    public List<String> getImages() {
        if (produto == null) {
            return null;
        }
        File arrayArquivos[] = null;
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ServletContext context = (ServletContext) facesContext.getExternalContext().getContext();
        String realPath = context.getRealPath("/");
        File file = new File(realPath + "/resources/imagens/" + produto.getCodigo() + "/");
        if (file.isDirectory()) {
            arrayArquivos = file.listFiles();

            images = new ArrayList<String>();
            for (int i = 0; i < arrayArquivos.length; i++) {
                images.add(arrayArquivos[i].getName());
            }

        }
        return images;
    }

    public void gerarRelatorio() {
        try {
            List<TipoProduto> listagemResultado = tipoProdutoDAO.pesquisarTodos();
            HashMap paramRel = new HashMap();
            String nomeRelatorio = "relTipoProduto";
            relatorio.gerarRelatorio(nomeRelatorio, paramRel, listagemResultado);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void gerarRelatorioProduto() {
        try {
            List<Produto> listagemResultado = produtoDAO.pesquisarTodos();
            HashMap paramRel = new HashMap();
            String nomeRelatorio = "relProduto";
            relatorio.gerarRelatorio(nomeRelatorio, paramRel, listagemResultado);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TipoProduto getTipoProduto() {
        return tipoProduto;
    }

    public void setTipoProduto(TipoProduto tipoProduto) {
        this.tipoProduto = tipoProduto;
    }

    public List<TipoProduto> getTipos() {
        return tipos;
    }

}
