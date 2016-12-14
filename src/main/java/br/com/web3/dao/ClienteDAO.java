package br.com.web3.dao;

import br.com.web3.model.Cliente;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;

public class ClienteDAO {
    private SessionFactory factory;
    
    public ClienteDAO(){
        factory = HibernateUtil.getSessionFactory();
    }
    
    public void cadastrarCliente(Cliente cliente) {
        Session session = factory.openSession();
        try {
            session.beginTransaction();
            session.save(cliente);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();

        }
    }
    
    public void excluirCliente(Cliente cliente) {
        Session session = factory.openSession();
        try {
            session.beginTransaction();
            session.delete(cliente);
            session.getTransaction().commit();//executa o commit 
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
    public void atualizar(Cliente cliente) {
        Session session = factory.openSession();
        try {
            session.beginTransaction();
            session.update(cliente);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

    }
    
    public List<Cliente> pesquisarTodos() {
        try {
            Session session = factory.openSession();
            Criteria criteria = session.createCriteria(Cliente.class);
            criteria.createAlias("cpf", "cpf");
            criteria.addOrder(Order.asc("cpf"));
            criteria.addOrder(Order.asc("cpf.nomeCompleto"));
            return criteria.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
    
    public List<Cliente> getList() {
        try {
            Session session = factory.openSession();
            Criteria criteria = session.createCriteria(Cliente.class);
            return criteria.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    
}
