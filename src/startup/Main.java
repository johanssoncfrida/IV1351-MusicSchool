

package startup;

import controller.Controller;
import integration.FailedToConnectException;
import java.sql.SQLException;
import view.Interpreter;


/**
 * Main class, program starts here
 * @author Frida Johansson
 */
public class Main {
    public static void main(String[] args) throws SQLException {
        try{
            new Interpreter(new Controller()).handleCmds();
        } catch(FailedToConnectException ex){
            System.out.println("Could not connect to school's database.");
            ex.printStackTrace();
    }
    }
}
