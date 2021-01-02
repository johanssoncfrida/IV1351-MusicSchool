/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;


import java.util.ArrayList;
import java.util.HashMap;


/**
 *
 * @author Me
 */
public class Instrument {
    int ID;
    String instrument;
    String brand;
    int price;
    
    public Instrument(int id, String instrument, String brand, int price){
        this.ID = id;
        this.instrument = instrument;
        this.brand = brand;
        this.price = price;
    }
    public Instrument(){
        
    }
    
    public void setID(int id){
        this.ID = id;
    }
    public void setInstrument(String instrument){
        this.instrument = instrument;
    }
    public void setBrand(String brand){
        this.brand = brand;
    }
    public void setPrice(int price){
        this.price = price;
    }
    public int getID(){
        return ID;
    }
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append("Type: ").append(instrument).append("\tBrand: ").
                append(brand).append("\tPrice: ").append(price).append("\t\tID: ").append(ID);
            return sb.toString();
    }
}
        
    

