package com.sxdt.house.model;

/**
 * Created by agu on 16/11/3.
 */
public class Option {

    public String value;
    public String text;

    public Option( ) {

    }

    public Option(String value, String text ) {
        this.value = value;
        this.text = text;
    }
    @Override
    public String toString(){
        return text;
    }
}
