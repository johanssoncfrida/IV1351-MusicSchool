
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package integration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



import model.Instrument;
import model.Student;
/**
 *
 * @author Me
 */
public class MusicSchool {
    private Connection connection;
    private final String url = "jdbc:postgresql://localhost:5432/musicschool";
    private final String user = "postgres";
    private final String password = "postgres";
    
    private PreparedStatement createInstrumentsStmt;
    private PreparedStatement createStudentStmt;
    private PreparedStatement createRentInstrStmt;
    private PreparedStatement createcollectIdStmt;
    private PreparedStatement createCollectInstrStmt;
    private PreparedStatement createAddInstrumentToStudentStmt;
    private PreparedStatement createTerminateRentalStmt;
    
    public MusicSchool() throws FailedToConnectException {
        try {
            connectToBankMS();
            prepareStatements();
        } catch (ClassNotFoundException | SQLException exception) {
            throw new FailedToConnectException("Could not connect to datasource.", exception);
        }
    }
    public Instrument[] showInstruments() throws SQLException, FailedToConnectException{
        Instrument [] instruments = new Instrument[8];
        int counter = 0;
        try(ResultSet rs = createInstrumentsStmt.executeQuery()){
            while(rs.next()){
                int id = rs.getInt("id_instrument_rental");
                String type = rs.getString("type_of_instrument");
                String brand = rs.getString("brands");
                int price = rs.getInt("price");
                Instrument instrument = new Instrument(id,type,brand,price);
                instruments[counter] = instrument; 
                if(counter == instruments.length-1)
                    instruments = resizeArray(instruments);
                counter++;
            }
            connection.commit();
        }catch(SQLException ex){
            handleException("Failed to connect with database",ex);
        }
        return instruments;
    }
    private Instrument[] resizeArray(Instrument [] instruments){
        Instrument [] doubleInstrument = new Instrument [instruments.length*2];
        int pos = 0;
        for(Instrument inst: instruments){
             doubleInstrument[pos] = inst;
             pos++;
        }
        return doubleInstrument;
    }
    public Student[] collectStudent() throws SQLException, FailedToConnectException{
        Student [] students = new Student[14];

        int counter = 0;
        try(ResultSet rs = createStudentStmt.executeQuery()){
            while(rs.next()){
     
                String studentID = rs.getString("student_id");
                String level = rs.getString("level");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String personalNumber = rs.getString("personal_number");
                String phoneNumber = rs.getString("phone_no");
                
                Student student = new Student(studentID, level, firstName,  lastName,  personalNumber,  phoneNumber);
                students[counter] = student;
                addStudentsInstrument(student, studentID);
                counter++;
            }
            connection.commit();
        }catch(SQLException ex){
            handleException("Failed to connect with database",ex);    
        }return students;
    }

    public void rentInstrument(Student student,int instr, String studentID) throws SQLException, FailedToConnectException{
        try{
            int id = collectPersonId(studentID);
            createRentInstrStmt.setInt(1, id);
            createRentInstrStmt.setInt(2, instr);
            
            int updatedRows = createRentInstrStmt.executeUpdate();
            if (updatedRows != 1) {
                handleException("No rows was updated!", null);
            }
            connection.commit();
         }catch(SQLException ex){
            handleException("Failed to connect with database",ex);  
        }

    }
    public void addStudentsInstrument(Student student, String stuID) throws SQLException, FailedToConnectException{
        
        createAddInstrumentToStudentStmt.setString(1, stuID);
        try(ResultSet rs = createAddInstrumentToStudentStmt.executeQuery()){
            while(rs.next()){
                int id_rental = rs.getInt("id_instrument_rental");
                String type = rs.getString("type_of_instrument");
                String brand = rs.getString("brands");
                int price = rs.getInt("price");
                Instrument instrument = new Instrument(id_rental,type,brand,price);
                student.pushInstrument(instrument);
            }
            connection.commit();
        }catch(SQLException ex){
            handleException("Failed to connect with database",ex);
        }
    }
    public void storeInstrument(Student student,int instrID) throws FailedToConnectException, SQLException{
       
        createCollectInstrStmt.setInt(1, instrID);
        try(ResultSet rs = createCollectInstrStmt.executeQuery()){
            while(rs.next()){
                int id_rental = rs.getInt("id_instrument_rental");
                String type = rs.getString("type_of_instrument");
                String brand = rs.getString("brands");
                int price = rs.getInt("price");
                Instrument instrument = new Instrument(id_rental,type,brand,price);
                student.pushInstrument(instrument);
            }
            connection.commit();
        }catch(SQLException ex){
            handleException("Failed to connect with database",ex);
        }
      
    }
    private int collectPersonId(String studentID) throws FailedToConnectException{
        int id = 0;
        try {
            createcollectIdStmt.setString(1, studentID);
            ResultSet rs = createcollectIdStmt.executeQuery();
            while(rs.next()){
                id = rs.getInt("person_id");
            }
            connection.commit();
        }catch(SQLException ex){
            handleException("Failed to connect with database",ex);  
        }
        return id;
    }
    public void terminateRental(Student student,int instr, String studentID) throws FailedToConnectException{
        student.popInstrument(instr);
        try{
            createTerminateRentalStmt.setInt(1, instr);
            
            int updatedRows = createTerminateRentalStmt.executeUpdate();
            if (updatedRows != 1) {
                handleException("No rows was updated!", null);
            }
            connection.commit();
            
         }catch(SQLException ex){
            handleException("Failed to connect with database",ex);  
        }
    }
    private void connectToBankMS() throws ClassNotFoundException, SQLException, FailedToConnectException {
        try {
            Class.forName("org.postgresql.Driver");        
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/musicschool","postgres", "postgres");
            connection.setAutoCommit(false);
        } catch (ClassNotFoundException | SQLException ex) {
            handleException("Failed to connect with database",ex);
        }
       
    }
    
    private void prepareStatements() throws SQLException {
       
        createInstrumentsStmt = connection.prepareStatement("SELECT id_instrument_rental,type_of_instrument, brands, price FROM instrument_rental WHERE rented = false");
        createStudentStmt = connection.prepareStatement("SELECT stu.student_id, stu.level, per.first_name, per.last_name, per.personal_number, per.phone_no " +
                "FROM student stu INNER JOIN person per ON stu.person_id = per.id ");
        createTerminateRentalStmt = connection.prepareStatement("UPDATE instrument_rental SET rented = false, person_id = null WHERE id_instrument_rental = ?");
        createRentInstrStmt = connection.prepareStatement("UPDATE instrument_rental SET rented = true, person_id = ? WHERE id_instrument_rental = ?");
        createcollectIdStmt = connection.prepareStatement("SELECT person_id FROM student WHERE student_id = ?");
        createCollectInstrStmt = connection.prepareStatement("SELECT id_instrument_rental,type_of_instrument, brands, price FROM instrument_rental WHERE id_instrument_rental = ?");
        createAddInstrumentToStudentStmt = connection.prepareStatement("SELECT id_instrument_rental, type_of_instrument, brands, price from instrument_rental " +
                    "WHERE instrument_rental.person_id IN (SELECT person_id from student where student_id = ?)");
    }
    
    public void closeConnection() throws SQLException{
        try {
            connection.close();
        } catch (SQLException e) {
            throw new SQLException(" Could not close result set.", e);
        }
    }
    private void handleException(String failureMsg, Exception cause) throws FailedToConnectException {
        String fullExceptionMsg = failureMsg;
        try {
            connection.rollback();
        } catch (SQLException rollbackExc) {
            fullExceptionMsg = fullExceptionMsg + 
            ". Also failed to rollback transaction because of: " + rollbackExc.getMessage();
        }

        if (cause != null) {
            throw new FailedToConnectException(failureMsg, cause);
        } else {
            throw new FailedToConnectException(failureMsg);
        }
    }
}
