package com.googlecode.gwtstreamer.test.client.shared;

import com.googlecode.gwtstreamer.client.Streamable;

/**
 * Created by akuranov on 21/01/2015.
 */
public class UninitBean implements Streamable {
    public String name;
    public String surname;

    public UninitBean(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    @Override
    public String toString() {
        return "UninitBean{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                '}';
    }
}
