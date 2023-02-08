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
//    MovieName > MovieID : BookingCapacity
    public HashMap<String, HashMap<String, Integer>> movieMap = new HashMap<>();
//    UserID > MovieName > MovieID : noOfTickets
    public HashMap<String, HashMap<String, HashMap<String, Integer>>> userMap = new HashMap<>();
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


        String responseString = movieMap.get(movieName).keySet().toString();


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
        responseString = responseString + "\n" + serverOneResponse + "\n" + serverTwoResponse;
//        listShows.add(serverTwoResponse);
        return responseString;
    }

    public String bookMovieTickets(String userID, String movieID, String movieName, int noOfTickets) throws IOException {
        String targetServer = movieID.substring(0,3).toLowerCase();
        String serverResponse = "";

        if(this.serverID.equals(targetServer)){
            if(movieMap.containsKey(movieName)){
                if(movieMap.get(movieName).containsKey(movieID)){
                    int capacity = movieMap.get(movieName).get(movieID);
                    if(capacity >= noOfTickets){
                        if(userMap.containsKey(userID)){
                            if(userMap.get(userID).containsKey(movieID)){
                                userMap.get(userID).get(movieID).put(movieName,noOfTickets);
                                movieMap.get(movieName).put(movieName,capacity - noOfTickets);
                                serverResponse = "Tickets Booked.";
                            }
                            else {
                                userMap.get(userID).put(movieID,new HashMap<String,Integer>());
                                userMap.get(userID).get(movieID).put(movieName,noOfTickets);
                                movieMap.get(movieName).put(movieName,capacity - noOfTickets);
                                serverResponse = "Tickets Booked.";
                            }
                        }
                        else{
                            userMap.put(userID,new HashMap<String, HashMap<String, Integer>>());
                            userMap.get(userID).put(movieID, new HashMap<String, Integer>());
                            userMap.get(userID).get(movieID).put(movieName,noOfTickets);
                            movieMap.get(movieName).put(movieName,capacity - noOfTickets);
                            serverResponse = "Tickets Booked.";
                        }
                    }
                    else{
                        serverResponse = "Enter value for tickets is more than booking capacity.";
                    }
                }
                else{
                    serverResponse = "Incorrect MovieID. No Movie show exists.";
                }
            }
            else {
                serverResponse = "Incorrect Movie Name. Movie does not exists.";
            }

        } else if (targetServer.equals("atw")) {
            serverResponse = sendMsgToServer("bookMovieTickets",userID,movieName,movieID,noOfTickets,atwPort);
        } else if (targetServer.equals("out")) {
            serverResponse = sendMsgToServer("bookMovieTickets",userID,movieName,movieID,noOfTickets,outPort);
        } else if (targetServer.equals("ver")) {
            serverResponse = sendMsgToServer("bookMovieTickets",userID,movieName,movieID,noOfTickets,verPort);
        }
        return serverResponse;
    }
    public String getBookingSchedule(String userID) throws IOException {
        String serverOneResponse = "";
        String serverTwoResponse = "";
        String responseString = "";

        if(!userMap.isEmpty()){
            if(userMap.containsKey(userID)){
                responseString = userMap.get(userID).keySet().toString();

                if(this.serverID.equals("atw")){
                    serverOneResponse = sendMsgToServer("getBookingSchedule",userID,null,null,0,outPort);
                    serverTwoResponse = sendMsgToServer("getBookingSchedule",userID,null,null,0,verPort);
                } else if(this.serverID.equals("out")){
                    serverOneResponse = sendMsgToServer("getBookingSchedule",userID,null,null,0,atwPort);
                    System.out.println(serverOneResponse);
                    serverTwoResponse = sendMsgToServer("getBookingSchedule",userID,null,null,0,verPort);
                } else if (this.serverID.equals("ver")) {
                    serverOneResponse = sendMsgToServer("getBookingSchedule",userID,null,null,0,atwPort);
                    serverTwoResponse = sendMsgToServer("getBookingSchedule",userID,null,null,0,outPort);
                }
                responseString = responseString + "\n" + serverOneResponse + "\n" + serverTwoResponse;
            }
        }
        else {
            responseString = "No bookings found.";
        }
        return responseString;
    }
    public String cancelMovieTickets(String userID, String movieID, String movieName, int noOfTickets) throws IOException {
        String targetServer = movieID.substring(0,3).toLowerCase();
        String serverResponse = "";

        if(this.serverID.equals(targetServer)){
            if(movieMap.get(movieName).get(movieID) == noOfTickets){
//                TODO: Remove movieID instead of userID
                userMap.remove(userID);
            }else {
                userMap.get(userID).get(movieID).put(movieName,noOfTickets);
                return "201";
            }
        } else if (targetServer.equals("atw")) {
            serverResponse = sendMsgToServer("cancelMovieTickets",userID,movieName,movieID,noOfTickets,atwPort);
        } else if (targetServer.equals("out")) {
            serverResponse = sendMsgToServer("cancelMovieTickets",userID,movieName,movieID,noOfTickets,outPort);
        } else if (targetServer.equals("ver")) {
            serverResponse = sendMsgToServer("cancelMovieTickets",userID,movieName,movieID,noOfTickets,verPort);
        }
        return serverResponse;
//        return null;
    }
//    public void test(){
//        System.out.println("Helloooo");
//    }
    public String listMovieShowAvailabilityUDP(String movieName) {
        return movieMap.get(movieName).keySet().toString();
    }
    public String getBookingScheduleUDP(String userID){
        String responseString = "";
        if(!userMap.isEmpty()){
            if(userMap.containsKey(userID)){
                responseString = userMap.get(userID).keySet().toString();
            }
        }
        return responseString;
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
