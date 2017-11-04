/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tema1.ejercicio19;

import com.thoughtworks.xstream.converters.SingleValueConverter;

/**
 *
 * @author Perig
 */
public class IDConverter implements SingleValueConverter {
    public String toString(Object obj) {
         return ((ID) obj).getId()+"";
    }

    public Object fromString(String id) {
        return new ID(Integer.parseInt(id));
    }

    public boolean canConvert(Class type) {
        return type.equals(ID.class);
    }
}
