
package model;


/**
 * Represents a student who attends to Soundgood musicschool.
 * It is the superclass of the class <code>Person</code>.
 * @author Frida Johansson
 */
public class Student extends Person{
    String studentID;
    String level;
    Instrument [] instruments;
   
    public Student(String studentID,String level,String firstName, String lastName, String personalNumber, String phoneNo) {
        super(firstName, lastName, personalNumber, phoneNo);
        this.studentID = studentID;
        this.level = level;
        instruments = new Instrument[2];
    }
    public Student(){
        
    }
    /**
     * 
     * @return studentID 
     */
    public String getStudentID(){
        return studentID;
    }
    /**
     * 
     * @return the students name from subclass <code>Person</code>
     */
    public String getName(){
        return super.getFirstName();
    }
    
    /**
     * 
     * @return the student's instrument.
     */
    public Instrument[] getInstruments(){
        return instruments;
    }
    
    public Instrument getInstrument(int id){
        Instrument instrument = null;
        for(Instrument instr : instruments)
            if(id == instr.getID())
                instrument = instr;
        return instrument;
    }
    
    /**
     * When renting a instrument you use this method to add it to the student's 
     * pool of instruments. 
     * @param instr represents the instruments to be added to the student's pool
     */
    public void pushInstrument(Instrument instr){
        for(int i = 0; i < instruments.length; i++){
                if(instruments[i] == null){
                    instruments[i] = instr;
                    break;
                }          
        }
    }
    /**
     * When terminate a instrument this method is used to pop that instrument.
     * @param instrId represent the ID of the instrument to be popped.
     */
    public void popInstrument(int instrId){
        Instrument [] newInstru = new Instrument[2];
        int counter = 0;
        for (Instrument instr : instruments){
            if(instr.getID()!= instrId)
                newInstru[counter] = instr;
        counter ++;
        }
        instruments = newInstru;
    }
    /**
     * Checks if the student already has the maximum rented quota of instrument
     * or if it is possible to rent another one.
     * @return true if possible otherwise false
     */
    public boolean isRentPossible(){
        for (Instrument instr : instruments) {
            if (instr == null) 
                return true;
        }
    return false;
    }
    
    /**
     * 
     * @return the student's instrument as a readable string.
     */
    public String getInstrumentsToString(){
        StringBuilder sb = new StringBuilder();
        for(Instrument inst: instruments)
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
        for(Instrument inst: instruments)
            if(inst != null) 
                sb.append(inst);
        return sb.toString();
    }
    
}
