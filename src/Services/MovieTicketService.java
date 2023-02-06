package Services;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Interface.AdminInterface;
import Interface.CustomerInterface;

import static java.net.HttpURLConnection.*;
//import static java.net.HttpURLConnection.HTTP_OK;

public class MovieTicketService extends UnicastRemoteObject implements AdminInterface, CustomerInterface {
    public HashMap<String, HashMap<String, Integer>> movieMap = new HashMap<>();
    DatagramSocket dss;
    String serverID = "";
    String serverName = "";
    int atwPort = 4556;
    int outPort = 4557;
    int verPort = 4558;


    public MovieTicketService(String serverID,String serverName) throws Exception{
        super();
        this.serverID = serverID;
    }

    public MovieTicketService(DatagramSocket ds) throws Exception{
        super();
        this.dss = ds;
    }

    public MovieTicketService() throws RemoteException {
        super();

    }

    public int addMovieSlots(String movieID, String movieName, int bookingCapacity){
        movieMap.put(movieName,new HashMap<String, Integer>());
        movieMap.get(movieName).put(movieID,bookingCapacity);
        System.out.println(movieMap);
        return HTTP_CREATED;
    }
    public int removeMovieSlots(String movieID, String movieName){
        movieMap.get(movieName).remove(movieID);
        /**
         * TODO: Shift customer with existing movie slot
         * TODO: to the next available movie show
         */
        System.out.println(movieMap);
        return 200;
    }
    public String listMovieShowAvailability(String movieName) throws IOException {
        String serverOneResponse = "";
        String serverTwoResponse = "";


        String s = movieMap.get(movieName).keySet().toString();


        if(this.serverID.equals("atw")){
            serverOneResponse = sendMsgToServer("listMovieShowAvailability",null,movieName,null,0,outPort);
            serverTwoResponse = sendMsgToServer("listMovieShowAvailability",null,movieName,null,0,verPort);
        } else if(this.serverID.equals("out")){
            serverOneResponse = sendMsgToServer("listMovieShowAvailability",null,movieName,null,0,atwPort);
            System.out.println(serverOneResponse);
            serverTwoResponse = sendMsgToServer("listMovieShowAvailability",null,movieName,null,0,verPort);
        } else if (this.serverID.equals("ver")) {
            serverOneResponse = sendMsgToServer("listMovieShowAvailability",null,movieName,null,0,atwPort);
            serverTwoResponse = sendMsgToServer("listMovieShowAvailability",null,movieName,null,0,outPort);
        }
        s = s + "\n" + serverOneResponse + "\n" + serverTwoResponse;
//        listShows.add(serverTwoResponse);

        return s;
    }
    public String listMovieShowAvailabilityUDP(String movieName) {
        return movieMap.get(movieName).keySet().toString();
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
    public String sendMsgToServer(String func, String userID,String movieName, String movieID,int noOfTickets, int port) throws IOException {
        /**
         * UDP request to
         * remote server
         */
        DatagramSocket ds = new DatagramSocket();
        String requestString = func + ";" + userID + ";" + movieName + ";" + movieID + ";" + noOfTickets;
        byte[] request = requestString.getBytes();
        InetAddress ia = InetAddress.getLocalHost();
        DatagramPacket dp = new DatagramPacket(request,request.length,ia,port);
        ds.send(dp);

        /**
         * UDP response from
         * remote server
         */
        byte[] byteReceive = new byte[2048];
        DatagramPacket dpReceived = new DatagramPacket(byteReceive,byteReceive.length);
        ds.receive(dpReceived);
        System.out.println(dpReceived.getData());
        return new String(dpReceived.getData()).trim();
    }
}
