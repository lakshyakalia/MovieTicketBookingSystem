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
//    TODO: Correct the hashmap
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

    public String addMovieSlots(String movieID, String movieName, int bookingCapacity){
        if(!movieMap.isEmpty() && movieMap.containsKey(movieName)){
            HashMap<String,Integer> temp = new HashMap<>();
            for (var x : movieMap.get(movieName).entrySet()) {
                temp.put(x.getKey(), x.getValue());
            }
            temp.put(movieID,bookingCapacity);
            movieMap.put(movieName,temp);
        }
        else {
            HashMap<String,Integer> temp = new HashMap<>();
            temp.put(movieID,bookingCapacity);
            movieMap.put(movieName,temp);
        }
        return "Movie Slot Added.";
    }
    public String removeMovieSlots(String movieID, String movieName){
        String responseString = "";
        if(!movieMap.isEmpty() && movieMap.containsKey(movieName) && movieMap.get(movieName).containsKey(movieID)){
            movieMap.get(movieName).remove(movieID);
            responseString = "Movie slot deleted.";
            /**
             * TODO: Shift customer with existing movie slot
             * TODO: to the next available movie show
             */
        }
        else {
            responseString = "Movie slot does not exists.";
        }
        return responseString;
    }
    public String listMovieShowAvailability(String movieName) throws IOException {
        String serverOneResponse = "";
        String serverTwoResponse = "";

        String responseString = "";

        if(!movieMap.isEmpty()){
            if(movieMap.containsKey(movieName)) {
                for (var x : movieMap.get(movieName).entrySet()) {
                    responseString = responseString + "Movie Show: " + x.getKey() + " Capacity: " + x.getValue() + "\n";
                }
            }
        }

        if (this.serverID.equals("atw")) {
            serverOneResponse = sendMsgToServer("listMovieShowAvailability", null, movieName, null, 0, outPort);
            serverTwoResponse = sendMsgToServer("listMovieShowAvailability", null, movieName, null, 0, verPort);
        } else if (this.serverID.equals("out")) {
            serverOneResponse = sendMsgToServer("listMovieShowAvailability", null, movieName, null, 0, atwPort);
            System.out.println(serverOneResponse);
            serverTwoResponse = sendMsgToServer("listMovieShowAvailability", null, movieName, null, 0, verPort);
        } else if (this.serverID.equals("ver")) {
            serverOneResponse = sendMsgToServer("listMovieShowAvailability", null, movieName, null, 0, atwPort);
            serverTwoResponse = sendMsgToServer("listMovieShowAvailability", null, movieName, null, 0, outPort);
        }

        responseString = responseString + "\n" + serverOneResponse + "\n" + serverTwoResponse;
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
                            if(userMap.get(userID).containsKey(movieName)){
                                if(userMap.get(userID).get(movieName).containsKey(movieID)){
                                    int oldNoOfTickets = userMap.get(userID).get(movieName).get(movieID);
                                    userMap.get(userID).get(movieName).put(movieID,oldNoOfTickets + noOfTickets);
                                    movieMap.get(movieName).put(movieID,capacity - noOfTickets);
                                    serverResponse = "Tickets Booking Updated.";
                                }
                                else {
                                    HashMap<String,Integer> temp = new HashMap<>();
                                    for (var x : userMap.get(userID).get(movieName).entrySet()) {
                                        temp.put(x.getKey(), x.getValue());
                                    }
                                    temp.put(movieID,noOfTickets);
                                    userMap.get(userID).put(movieName,temp);
                                    movieMap.get(movieName).put(movieID,capacity - noOfTickets);
                                    serverResponse = "Tickets Booked.";
                                }
                            }
                            else {
                                HashMap<String,Integer> subTemp = new HashMap<>();
                                subTemp.put(movieID,noOfTickets);
                                HashMap<String, HashMap<String, Integer>> temp = new HashMap<>();
                                temp.put(movieName,subTemp);
                                userMap.put(userID,temp);

//                            userMap.get(userID).put(movieID, new HashMap<String, Integer>());
//                            userMap.get(userID).get(movieID).put(movieName,noOfTickets);

                                movieMap.get(movieName).put(movieID,capacity - noOfTickets);
                                serverResponse = "Tickets Booked.";
                            }
                        }
                        else{
                            HashMap<String,Integer> subTemp = new HashMap<>();
                            subTemp.put(movieID,noOfTickets);
                            HashMap<String, HashMap<String, Integer>> temp = new HashMap<>();
                            temp.put(movieName,subTemp);
                            userMap.put(userID,temp);

//                            userMap.get(userID).put(movieID, new HashMap<String, Integer>());
//                            userMap.get(userID).get(movieID).put(movieName,noOfTickets);

                            movieMap.get(movieName).put(movieID,capacity - noOfTickets);
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

        if(!userMap.isEmpty()) {
            if (userMap.containsKey(userID)) {
                for (var x : userMap.get(userID).entrySet()) {
                    responseString = responseString + "Tickets booked for movie: " + x.getKey() + " at: " + x.getValue() + "\n";
                }
            }
        }

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
        return responseString;
    }
    public String cancelMovieTickets(String userID, String movieID, String movieName, int noOfTicketsToCancel) throws IOException {
        String targetServer = movieID.substring(0,3).toLowerCase();
        String serverResponse = "";

        if(this.serverID.equals(targetServer)){
            if(movieMap.containsKey(movieName)){
                if(movieMap.get(movieName).containsKey(movieID)){
                    if(userMap.containsKey(userID) &&  userMap.get(userID).containsKey(movieName) && userMap.get(userID).get(movieName).containsKey(movieID)){
                        int ticketsBooked = userMap.get(userID).get(movieName).get(movieID);
                        if(noOfTicketsToCancel <= ticketsBooked){
                            if(noOfTicketsToCancel < ticketsBooked){
                                userMap.get(userID).get(movieName).put(movieID,ticketsBooked - noOfTicketsToCancel);
                                int capacity = movieMap.get(movieName).get(movieID);
                                movieMap.get(movieName).put(movieID,capacity + noOfTicketsToCancel);
                                serverResponse = "Tickets successfully cancelled!";
                            }
                            else {
                                userMap.get(userID).get(movieName).remove(movieID);
                                int capacity = movieMap.get(movieName).get(movieID);
                                movieMap.get(movieName).put(movieID,capacity + noOfTicketsToCancel);
                                serverResponse = "Tickets successfully cancelled!";
                            }
                        }
                        else {
                            serverResponse = "No. of tickets to cancel is more than booked tickets";
                        }
                    }
                    else {
                        serverResponse = "No booking found for user.";
                    }
                }
                else {
                    serverResponse = "No movie show found.";
                }
            }
            else {
                serverResponse = "No movie found.";
            }
        } else if (targetServer.equals("atw")) {
            serverResponse = sendMsgToServer("cancelMovieTickets",userID,movieName,movieID,noOfTicketsToCancel,atwPort);
        } else if (targetServer.equals("out")) {
            serverResponse = sendMsgToServer("cancelMovieTickets",userID,movieName,movieID,noOfTicketsToCancel,outPort);
        } else if (targetServer.equals("ver")) {
            serverResponse = sendMsgToServer("cancelMovieTickets",userID,movieName,movieID,noOfTicketsToCancel,verPort);
        }
        return serverResponse;
    }

    public String listMovieShowAvailabilityUDP(String movieName) {
        String responseString = "";
        if(movieMap.containsKey(movieName)){
            for(var x: movieMap.get(movieName).entrySet()){
                responseString = responseString + "Movie Show: " + x.getKey() + " Capacity: " + x.getValue() + "\n";
            }
        }
        return responseString;
    }
    public String getBookingScheduleUDP(String userID){
        String responseString = "";
        if(!userMap.isEmpty()){
            if(userMap.containsKey(userID)){
                for(var x : userMap.get(userID).entrySet()){
                    responseString = responseString + "Tickets booked for movie: " + x.getKey() + " at: " + x.getValue() + "\n";
                }
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
