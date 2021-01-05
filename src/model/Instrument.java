
package model;


/**
 * The class Instrument represents the Soundgood Musicschools instruments.
 * 
 * @author Frida Johansson
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
    /**
     * Sets the id of the instrument
     * @param id the id of the instrument to be set
     */
    public void setID(int id){
        this.ID = id;
    }
    /**
     * Sets the type of the instrument
     * @param instrument represents the type
     */
    public void setInstrument(String instrument){
        this.instrument = instrument;
    }
    /**
     * Sets the brand of the instrument
     * @param brand the brand to be set
     */
    public void setBrand(String brand){
        this.brand = brand;
    }
    /**
     * Sets the price of the instrument
     * @param price representes the rental price of the instrument
     */
    public void setPrice(int price){
        this.price = price;
    }
    /**
     * 
     * @return the id of the instrument
     */
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
        
    

