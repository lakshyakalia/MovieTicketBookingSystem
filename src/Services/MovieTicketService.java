package Services;

import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

import Interface.AdminInterface;
import Interface.CustomerInterface;

public class MovieTicketService extends UnicastRemoteObject implements AdminInterface, CustomerInterface {
//    public Map<String, Object> movieMap =new HashMap<String, Object>();
//    public Map<String,Object> movieIDMap =new HashMap<String, Object>();
    public HashMap<String, HashMap<String, String>> movieMap = new HashMap<>();


    public MovieTicketService() throws Exception{
        super();
//        initializeHashedMap();
    }

    public String addMovieSlots(String movieID, String movieName, int bookingCapacity){
        System.out.println(movieID + movieName + bookingCapacity);
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
    public void initializeHashedMap(){

    }
}
