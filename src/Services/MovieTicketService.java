package Services;

import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

import Interface.AdminInterface;
import Interface.CustomerInterface;

import static java.net.HttpURLConnection.*;
//import static java.net.HttpURLConnection.HTTP_OK;

public class MovieTicketService extends UnicastRemoteObject implements AdminInterface, CustomerInterface {
    public HashMap<String, HashMap<String, Integer>> movieMap = new HashMap<>();


    public MovieTicketService() throws Exception{
        super();
    }

    public int addMovieSlots(String movieID, String movieName, int bookingCapacity){
        movieMap.put(movieName,new HashMap<String, Integer>());
        movieMap.get(movieName).put(movieID,bookingCapacity);
        return HTTP_CREATED;
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
