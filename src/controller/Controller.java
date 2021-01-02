/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;
    
import integration.FailedToConnectException;
import integration.MusicSchool;
import java.sql.SQLException;
import model.Instrument;
import model.Student;

/**
 *
 * @author Me
 */
public class Controller {
    private final MusicSchool MS;
    private final Student []students;
    
    public Controller() throws FailedToConnectException, SQLException {
        MS = new MusicSchool();
        students = MS.collectStudent();
    }
    public void listAllInstruments() throws SQLException, FailedToConnectException{
        Instrument [] instruments = MS.showInstruments();
        for(Instrument instr: instruments){
            if(instr!=null)
                System.out.println(instr.toString());
        }
        
    }
    
    public void showStudents() throws SQLException, FailedToConnectException{
        for(Student stu: students){
                System.out.println(stu.toString());
        } 
    }
    
    public boolean getStuInfo(String stuId){
        boolean showStudentMenu = false;
        for(Student stu: students){
            if(stu.getStudentID().equals(stuId)){
                System.out.println("***************************************************\n" + 
                "Welcome " + stu.getName() + 
                "\nYour profile\n" + stu.toString() + "\n***************************************************");
                showStudentMenu = true;
            }
                
        }
        return showStudentMenu;    
    }
    public void listStudentsInstrument(String studentId){
        for(Student stu : students)
            if(stu.getStudentID().equals(studentId))
                System.out.println("Instruments: " + stu.getInstrumentsToString());
    }
    public boolean CheckIfRentIsAvailable(String stuId){
        boolean isPossible = false;
        for(Student stu: students){
            if(stu.getStudentID().equals(stuId))
                isPossible = stu.isRentPossible();
        }
        return isPossible;
    }
    public void selectInstrument(int instrID, String studentID) throws SQLException, FailedToConnectException{
        for(Student stu: students)
            if(stu.getStudentID().equals(studentID)){
                MS.rentInstrument(stu, instrID, studentID);
                MS.storeInstrument(stu, instrID);
                System.out.println(stu.getName() + "s" + " rented instruments: \n" + stu.getInstrumentsToString());
            }
    }

    
    
    public void terminateRental(int instrID, String studentID) throws FailedToConnectException{
        for(Student stu: students){
            if(stu.getStudentID().equals(studentID))
                MS.terminateRental(stu, instrID, studentID);
        }
    
    }
    public void closeConnection() throws SQLException{
        MS.closeConnection();
    }
}


