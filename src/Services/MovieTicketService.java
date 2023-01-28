package Services;

import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;

import Interface.AdminInterface;
import Interface.CustomerInterface;

public class MovieTicketService extends UnicastRemoteObject implements AdminInterface, CustomerInterface {

    public MovieTicketService() throws Exception{
        super();
    }

    public String addMovieSlots(String movieID, String movieName, int bookingCapacity){
        return null;
    }
    public String removeMovieSlots(String movieID, String movieName){
        return null;
    }
    public String listMovieShowAvailability(String movieName){
        return null;
    }
    public String bookMovieTickets(String customerID, String movieID, String movieName, int noOfTickets) {
        return null;
    }
    public String getBookingSchedule(String customerID){
        return null;
    }
    public String cancelMovieTickets(String customerID, String movieID, String movieName, int noOfTickets){
        return null;
    }
    public void test(){
        System.out.println("Helloooo");
    }
}
