/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.web3.dao;

import br.com.web3.model.ItensVenda;
import br.com.web3.model.Venda;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author aula
 */
public class ItensVendaDAO {
    
        private SessionFactory factory;

    public ItensVendaDAO() {
        factory = HibernateUtil.getSessionFactory();
    }

    
    public List<ItensVenda> getListByVenda(Venda venda) {
        try {
            Session session = factory.openSession();
            Criteria criteria = session.createCriteria(ItensVenda.class);
            criteria.add(Restrictions.eq("venda", venda));
            return criteria.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
