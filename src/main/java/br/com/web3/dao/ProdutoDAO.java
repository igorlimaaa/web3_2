package br.com.web3.dao;

import br.com.web3.model.ItensVenda;
import br.com.web3.model.Produto;
import br.com.web3.model.Venda;
import java.io.Serializable;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;

public class ProdutoDAO implements Serializable {

    private SessionFactory factory;
    //private ItensVenda itensvenda;

    public ProdutoDAO() {
        factory = HibernateUtil.getSessionFactory();
    }

    public void inserir(Produto produto) {
        Session session = factory.openSession();
        try {
            session.beginTransaction();
            session.save(produto);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();

        }
    }

    public boolean excluir(Produto produto/*, ItensVenda itensvenda, Venda venda*/) {
        boolean erro = false;
        Session session = factory.openSession();
        try {
            session.beginTransaction();
            //session.delete(itensvenda.getProduto());
            //session.delete(venda.getCliente());
            session.delete(produto);
            session.getTransaction().commit();//executa o commit 
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            erro = true;
        } finally {
            session.close();
        }
        
        return erro;
        
    }

    public Produto getProduto(int id) {
        try {
            Session session = factory.openSession();
            return (Produto) session.get(Produto.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Produto> pesquisarTodos() {
        try {
            Session session = factory.openSession();
            Criteria criteria = session.createCriteria(Produto.class);
            criteria.createAlias("tipoProduto", "tipo");
            criteria.addOrder(Order.asc("tipoProduto"));
            criteria.addOrder(Order.asc("tipo.descricao"));
            return criteria.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public void atualizar(Produto produto) {
        Session session = factory.openSession();
        try {
            session.beginTransaction();
            session.update(produto);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

    }

}
