/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.Controller;
import java.util.Scanner;
import model.StudentException;


/**
 * The view for user that handles the user input
 * @author Frida Johansson
 */
public class Interpreter {
    private final Controller ctrl;
    private boolean keepReceivingCmds = false;
    private boolean keepReceivingStuCmds = false;
    private boolean keepReceivingRentCmds = false;

    /**
     * Creates a new instance that will use the specified controller for all operations.
     * 
     * @param ctrl The controller used by this instance.
     */
    public Interpreter(Controller ctrl) {
        this.ctrl = ctrl;
    }

    /**
     * Interprets and performs user commands. This method will not return until the
     * UI has been stopped. The UI is stopped when user is entering the corresponding 
     * numbered choice for quit.
     */
    public void handleCmds() {
        keepReceivingCmds = true;
        while (keepReceivingCmds) {
            System.out.println("Go to students menu: 1");
            System.out.println("Quit: 2");
            try{
                Scanner scan = new Scanner(System.in);
                int choice = scan.nextInt();
                switch (choice) {
                    case 1:
                        handleStudents();
                        break;
                    case 2:
                        ctrl.closeConnection();
                        keepReceivingCmds = false;
                        break;
                    
                    default:
                        System.out.println("illegal command");
                }
            } catch (Exception e) {
                System.out.println("Operation failed");
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }   
    private void handleStudents(){
        String studentId;
        while(keepReceivingStuCmds){
            getMenu();
            try{
                Scanner scan = new Scanner(System.in);
                int choice = scan.nextInt();
                switch (choice) {
                    case 1:
                        ctrl.listAllInstruments();
                        
                        break;
                    case 2:
                        ctrl.showStudents();
                        break;
                    case 3:
                        handleListOfChoice();
                        Scanner scanner = new Scanner(System.in);
                        int selection = scanner.nextInt();
                        if(selection == 1)
                            break;
                        else if(selection == 2){
                            System.out.println("Enter student id: ");
                            Scanner scanStu = new Scanner(System.in);
                            studentId = scanStu.nextLine().toLowerCase();
                            handleStudent(studentId);
                            break;
                        }
                        else{
                            System.out.println("illegal command");
                            break;
                        }
                    case 4:
                        keepReceivingStuCmds = true;
                        break;
                    default:
                        System.out.println("illegal command");
                    }
            }catch(Exception e){
                System.out.println("Operation failed");
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }
    private void handleRent(String id){
        while(keepReceivingRentCmds){
            handleRentChoices();
            try{
                Scanner scan = new Scanner(System.in);
                int choice = scan.nextInt();
                switch (choice) {
                    case 1:
                        System.out.println("Instruments");
                        ctrl.listAllInstruments();
                        System.out.println("\nPress enter to go back");
                        boolean cmd = waitForUserInput();
                        if(cmd)
                            break;

                    case 2:
                        boolean isPossible = ctrl.CheckIfRentIsAvailable(id);
                        if(isPossible){
                            System.out.println("Enter the id of the instrument to rent it");
                            System.out.println("Enter 1 to go back to list instruments");
                            System.out.println("Enter 2 to rent a instrument");

                            Scanner scanner = new Scanner(System.in);
                            Scanner instrScan = new Scanner(System.in);
                            int selection = scanner.nextInt();
                            if(selection == 1)
                                break;
                            else if(selection == 2){
                                System.out.println("Enter id of instrument");
                                int instrumentID = instrScan.nextInt();
                                ctrl.selectInstrument(instrumentID,id);
                            break;
                            }

                        }
                        else
                            System.out.println("You can't rent more instruments, you need to return one first!");
                            break;
                    case 3:
                        ctrl.listStudentsInstrument(id);
                        boolean waitingForEnter = waitForUserInput();
                        if(waitingForEnter)
                            break;

                    case 4:
                        ctrl.listStudentsInstrument(id);
                        System.out.println("Write the ID of instrument you wish to return\n");
                        Scanner instrScan = new Scanner(System.in); 
                        int selection = instrScan.nextInt();
                        ctrl.terminateRental(selection, id);
                        break;
                    case 5:
                        keepReceivingRentCmds = true;
                        break;
                    default:
                        System.out.println("illegal command");
                    }
            }catch(Exception e){
                System.out.println("Operation failed");
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
}
    
    private void handleRentChoices(){
        System.out.println("\nStudent menu");
        System.out.println("Please select a number on what you would like to do");
        System.out.println("1. List available instruments for rent");
        System.out.println("2. Rent Instrument ");
        System.out.println("3. List your instruments");
        System.out.println("4. Return instrument");
        System.out.println("5. Quit");
    }
    private void handleListOfChoice(){
        System.out.println("Enter your student ID: ");
        System.out.println("List of students is at choice 2 in menu.");
        System.out.println("\n\tEnter 1 to go back");
        System.out.println("\tEnter 2 to enter your student id");
    }
    private void handleStudent(String studentId) throws StudentException{
        Boolean showRentMenu = ctrl.getStuInfo(studentId);
        if(showRentMenu)
            handleRent(studentId);
        else
            System.out.println("There is no student registered at that id");
        
    }
    private void getMenu(){
        System.out.println("\nMenu");
        System.out.println("Please select a number on what you would like to do");
        System.out.println("1. List available instruments for rent");
        System.out.println("2. Students of school");
        System.out.println("3. Select your student");
        System.out.println("4. Quit");
    }
    
    /**
     * Just a small method that prevents the menu to show directly
     * if not the user commands it with enter button
     * @return true or false depending on input from user
     */
    private boolean waitForUserInput(){
        Scanner scanner = new Scanner(System.in);
        if(scanner.hasNextLine())
            return true;
        return false;
    }
    
    
}
