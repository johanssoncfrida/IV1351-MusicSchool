
package integration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Instrument;
import model.Student;
/**
 * The handler of the database of SoundGood Musicschool
 * @author Frida Johansson
 */
public class MusicSchoolDbHandler {
    private Connection connection;
    
    private PreparedStatement createInstrumentsStmt;
    private PreparedStatement createStudentStmt;
    private PreparedStatement createRentInstrStmt;
    private PreparedStatement createcollectIdStmt;
    private PreparedStatement createCollectInstrStmt;
    private PreparedStatement createAddInstrumentToStudentStmt;
    private PreparedStatement createTerminateRentalStmt;
    
    public MusicSchoolDbHandler() throws FailedToConnectException {
        try {
            connectToBankMS();
            prepareStatements();
        } catch (ClassNotFoundException | SQLException exception) {
            throw new FailedToConnectException("Could not connect to datasource.", exception);
        }
    }
    
    /**
     * Collects the instruments from the database by the prepared
     * @return
     * @throws FailedToConnectException 
     */
    public Instrument[] showInstruments() throws FailedToConnectException{
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
            handleException("Failed to list the instruments from database",ex);
        }
        return instruments;
    }
    
    /**
     * Method to resize an array
     * @param instruments the array to be resized
     * @return a new resized array of instruments
     */
    private Instrument[] resizeArray(Instrument [] instruments){
        Instrument [] doubleInstrument = new Instrument [instruments.length*2];
        int pos = 0;
        for(Instrument inst: instruments){
             doubleInstrument[pos] = inst;
             pos++;
        }
        return doubleInstrument;
    }
    
    /**
     * Collects the students from the database
     * @return an array of students
     * @throws SQLException if preparedStatement fails on the connection
     * @throws FailedToConnectException if connection fails
     */
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
            handleException("Failed to collect the students",ex);    
        }return students;
    }
    
    private void addStudentsInstrument(Student student, String stuID) throws SQLException, FailedToConnectException{   
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
        
    /**
     * Rents an instrument and handles the database updates
     * @param instr represents the instrument id
     * @param studentID represents the student identification
     * @throws SQLException if preparedStatement fails on the connection
     * @throws FailedToConnectException if connection fails
     */
    public void rentInstrument(int instr, String studentID) throws SQLException, FailedToConnectException{
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
    
    /**
     * After renting an instrument you store the instrument in the students
     * array of instruments. 
     * @param student representing the student who's renting the instrument
     * @param instrID representing the instrument id
     * @throws SQLException if preparedStatement fails on the connection
     * @throws FailedToConnectException if connection fails
     */
    public void storeInstrument(Student student,int instrID) throws SQLException, FailedToConnectException{
       
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
    
    /**
     * Terminates a rental on a student
     * @param student representing the student who's terminating the rental
     * @param instr representing the instrument id
     * @param studentID representing the student identification
     * @throws SQLException if preparedStatement fails on the connection
     * @throws FailedToConnectException if connection fails 
     */
    public Instrument terminateRental(Student student,int instr, String studentID) throws SQLException, FailedToConnectException{
        Instrument instrument = student.getInstrument(instr);
        
        try{
            student.popInstrument(instr);
            createTerminateRentalStmt.setInt(1, instr);
            int updatedRows = createTerminateRentalStmt.executeUpdate();
            if (updatedRows != 1) {
                handleException("No rows was updated!", null);
            }
            connection.commit();
            
         }catch(SQLException ex){
            handleException("Failed to connect with database",ex);  
        }
        return instrument;
    }
    
    /**
     * Starts the connection of the database
     * @throws ClassNotFoundException if Driver could not be loaded
     * @throws SQLException if preparedStatement fails on the connection
     * @throws FailedToConnectException if connection fails 
     */
    private void connectToBankMS() throws ClassNotFoundException, SQLException, FailedToConnectException {
        try {
            Class.forName("org.postgresql.Driver");        
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/musicschool","postgres", "postgres");
            connection.setAutoCommit(false);
        } catch (ClassNotFoundException | SQLException ex) {
            handleException("Failed to connect with database",ex);
        }
       
    }
    
    /**
     * Prepare statements for the querys
     * @throws SQLException if preparedStatement fails on the connection
     */
    private void prepareStatements() throws SQLException {
       
        createInstrumentsStmt = connection.prepareStatement("SELECT id_instrument_rental,type_of_instrument, brands, price FROM instrument_rental WHERE rented = false");
        createStudentStmt = connection.prepareStatement("SELECT stu.student_id, stu.level, per.first_name, per.last_name, per.personal_number, con.phone_no " +
                "FROM student AS stu INNER JOIN person AS per ON stu.person_id = per.id INNER JOIN contact_details AS con ON con.person_id = per.id");
        createTerminateRentalStmt = connection.prepareStatement("UPDATE instrument_rental SET rented = false, person_id = null WHERE id_instrument_rental = ?");
        createRentInstrStmt = connection.prepareStatement("UPDATE instrument_rental SET rented = true, person_id = ? WHERE id_instrument_rental = ?");
        createcollectIdStmt = connection.prepareStatement("SELECT person_id FROM student WHERE student_id = ?");
        createCollectInstrStmt = connection.prepareStatement("SELECT id_instrument_rental,type_of_instrument, brands, price FROM instrument_rental WHERE id_instrument_rental = ?");
        createAddInstrumentToStudentStmt = connection.prepareStatement("SELECT id_instrument_rental, type_of_instrument, brands, price from instrument_rental " +
                    "WHERE instrument_rental.person_id IN (SELECT person_id from student where student_id = ?)");
    }
    
    /**
     * Closes the connection for the database
     * @throws SQLException if preparedStatement fails on the connection
     */
    public void closeConnection() throws SQLException{
        try {
            connection.close();
        } catch (SQLException e) {
            throw new SQLException(" Could not close result set.", e);
        }
    }
    
    /**
     * Exceptionshandler that handles exception and rollback
     * @param failureMsg represents the error message
     * @param cause represents the cause of the error
     * @throws FailedToConnectException if connection fails 
     */
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
