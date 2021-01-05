
package controller;
    
import integration.FailedToConnectException;
import integration.MusicSchoolDbHandler;
import java.sql.SQLException;
import java.util.Scanner;
import model.Instrument;
import model.InstrumentException;
import model.Student;
import model.StudentException;

/**
 * The class controller handles all the calls 
 * to the model and integration packages. 
 * @author Frida Johansson
 */
public class Controller {
    private final MusicSchoolDbHandler MS;
    private final Student []students;

    public Controller() throws FailedToConnectException, SQLException {
        MS = new MusicSchoolDbHandler();
        students = MS.collectStudent();
    }
    /**
     * List all the instruments available for rent
     * @throws InstrumentException if instruments not could be listed
     */
    public void listAllInstruments() throws InstrumentException{
        try{
            Instrument [] instruments = MS.showInstruments();
            for(Instrument instr: instruments){
                if(instr!=null)
                    System.out.println(instr.toString());
            }
        }catch(Exception e){
            throw new InstrumentException("Could not list the instruments", e);
        }

    }
    /**
     * Shows the studets attending to the musicschool
     */
    public void showStudents(){
        for(Student stu: students){
                System.out.println(stu.toString());
        } 
    }
    /**
     * Gets the information about the student thats been chosen by the user 
     * @param stuId represents the student identification
     * @return true or false depending if the student exists or not with that id
     */
    public boolean getStuInfo(String stuId){
        boolean showStudentMenu = false;
        for(Student stu: students){
            if(stu.getStudentID().equals(stuId)){
                showWelcomeText(stu);
                showStudentMenu = true;
            }

        }
        return showStudentMenu;    
    }

    private void showWelcomeText(Student stu){
        System.out.println("***************************************************\n" + 
        "Welcome " + stu.getName() + 
        "\nYour profile\n" + stu.toString() + "\n***************************************************");
    }
    
    /**
     * List the students instruments
     * @param stuId represents the student identification 
     */
    public void listStudentsInstrument(String stuId){
        for(Student stu : students)
            if(stu.getStudentID().equals(stuId))
                System.out.println("Instruments: " + stu.getInstrumentsToString());
    }
    
    /**
     * Check if it is possible for a student to rent another instrument
     * or if the the max limit is already reached.
     * @param stuId represents the student identification
     * @return true or false depending if possible or not
     */
    public boolean CheckIfRentIsAvailable(String stuId){
        boolean isPossible = false;
        for(Student stu: students){
            if(stu.getStudentID().equals(stuId))
                isPossible = stu.isRentPossible();
        }
        return isPossible;
    }
    /**
     * Rents an instrument chosen by the user and store it in the program 
     * so user will see it is rented.
     * @param instrID represents the id of the instrument to be rented
     * @param stuID represents the student identification
     * @throws InstrumentException if rent was not possible
     */
    public void selectInstrument(int instrID, String stuID) throws InstrumentException{
        try{
        for(Student stu: students)
            if(stu.getStudentID().equals(stuID)){
                MS.rentInstrument(instrID, stuID);
                MS.storeInstrument(stu, instrID);
                System.out.println(stu.getName() + "s" + " rented instruments: \n" + stu.getInstrumentsToString());
            }
        }catch(Exception e){
            throw new InstrumentException("Could not rent the instrument", e);
        }
    }


    /**
     * Terminates the rental of an instrument choseb by id of user
     * @param instrID represents the id of the instrument
     * @param studentID represents the student identification
     * @throws InstrumentException if not possible to terinate rental
     */
    public void terminateRental(int instrID, String studentID) throws InstrumentException{
        try{
        for(Student stu: students){
            if(stu.getStudentID().equals(studentID))
                MS.terminateRental(stu, instrID, studentID);
        }
        }catch(Exception e){
            throw new InstrumentException("Could not terminate the rental", e);
        }

    }
    
    /**
     * Close the connection
     * @throws SQLException If not possible to close
     */
    public void closeConnection() throws SQLException{
        MS.closeConnection();
    }

}