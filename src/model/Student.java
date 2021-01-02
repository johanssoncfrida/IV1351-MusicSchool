/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;


/**
 *
 * @author Me
 */
public class Student extends Person{
    String studentID;
    String level;
    Instrument [] instrument;
   
    public Student(String studentID,String level,String firstName, String lastName, String personalNumber, String phoneNo) {
        super(firstName, lastName, personalNumber, phoneNo);
        this.studentID = studentID;
        this.level = level;
        instrument = new Instrument[2];
    }
    public Student(){
        
    }
    public String getStudentID(){
        return studentID;
    }
    public String getName(){
        return super.getFirstName();
    }

    public void pushInstrument(Instrument instr){
        for(int i = 0; i < instrument.length; i++){
                if(instrument[i] == null){
                    instrument[i] = instr;
                    break;
                }          
        }
    }
    public void popInstrument(int instrId){
        Instrument [] newInstru = new Instrument[2];
        int counter = 0;
        for (Instrument instr : instrument){
            if(instr.getID()!=instrId)
                newInstru[counter] = instr;
        counter ++;
        }
        instrument = newInstru;
    }
    public boolean isRentPossible(){
        for (Instrument instr : instrument) {
            if (instr == null) 
                return true;
        }
    return false;
    }
    public Instrument[] getInstruments(){
        return instrument;
    }
    public String getInstrumentsToString(){
        StringBuilder sb = new StringBuilder();
        for(Instrument inst: instrument)
            if(inst != null) 
                sb.append(inst);
        return sb.toString();
    }
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append("Stu ID: ").append(studentID).append("\t").append("Level: ").append(level)
                .append("\nName: ").append(super.getFirstName()).append(" ").append(super.getLastName())
                .append("\nIdentification: ").append(super.getPersNo()).append("\t")
                .append("Phone no: ").append(super.getPhoneNo());
        for(Instrument inst: instrument)
            if(inst != null) 
                sb.append(inst);
        return sb.toString();
    }
    
}
