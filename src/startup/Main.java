
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package startup;

import controller.Controller;
import integration.FailedToConnectException;
import java.sql.SQLException;
import view.Interpreter;


/**
 *
 * @author Me
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
