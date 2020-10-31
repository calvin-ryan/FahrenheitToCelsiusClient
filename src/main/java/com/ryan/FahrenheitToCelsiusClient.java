package com.ryan;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * This program asks the user for a temperature in degrees fahrenheit and then
 * calls a server to calculate the temperature in degrees celsius.  This is used
 * to demonstrate the principles of client-server programming.
 * 
 * @author Calvin Ryan
 */
public class FahrenheitToCelsiusClient {
    /**
     * The server port number
     */
    private static final int PORT = 5555;
    /**
     * The name of the machine hosting the server
     */
    private static final String HOST_NAME = "localhost";
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String response;
        String prompt = "Enter a temperature in degrees fahrenheit"
                + " or Q to quit:";
        boolean keepGoing = true;
        while(keepGoing){
            response = getUserInput(prompt);
            if(response.equalsIgnoreCase("Q")){
                keepGoing = false;
            }else{
                try {
                    Double fahrenheit = Double.parseDouble(response);
                    try {
                        double celsius = getCelsiusFromServer(fahrenheit);
                        System.out.printf("Response from server:\n%.2f degrees"
                                + " fahrenheit equals %.2f degrees celsius\n",
                                fahrenheit, celsius);
                    } catch (UnknownHostException uhe) {
                        System.out.println("ERROR: " + uhe.getMessage());
                        System.out.println("Program terminating!");
                        System.exit(-1);
                    } catch(IOException ioe){
                        System.out.println("ERROR: " + ioe.getMessage());
                    }
                } catch (NumberFormatException nfe) {
                    System.out.printf("ERROR: [%s] is not a number!\n"
                            , response);
                    System.out.println("Please try again.\n");
                }
            }
        }
    }

    private static String getUserInput(String prompt) {
        System.out.print(prompt + " ");
        return new Scanner(System.in).nextLine();
    }
    
    /**
     * Sends the temperature to the server and returns the degrees celsius
     * calculated by the server
     * 
     * @param fahrenheit
     * @return 
     */
    private static double getCelsiusFromServer(Double fahrenheit)
            throws UnknownHostException, IOException {
        Socket socket = new Socket(HOST_NAME,PORT);
        DataInputStream inputStream =
                new DataInputStream(socket.getInputStream());
        DataOutputStream outputStream =
                new DataOutputStream(socket.getOutputStream());
        outputStream.writeDouble(fahrenheit);
        outputStream.flush();
        double celsius = inputStream.readDouble();
        inputStream.close();
        outputStream.close();
        return celsius;
    }

}
