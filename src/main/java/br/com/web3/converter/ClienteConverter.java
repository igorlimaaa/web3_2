/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.web3.converter;

import br.com.web3.controller.VendaController;
import br.com.web3.model.Cliente;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Eduardo
 */
@FacesConverter("cliente")
public class ClienteConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context,
            UIComponent component,
            String value) {
        if (value != null) {
            for (Cliente cliente : VendaController.CLIENTES) {
                if (value.equals(String.valueOf(cliente.getCodigo()))) {
                    return cliente;
                }
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component,
            Object value) {
        if (value != null && !value.equals("")) {
            Cliente cliente = (Cliente) value;
            return String.valueOf(cliente.getCodigo());
        }
        return null;
    }

}
