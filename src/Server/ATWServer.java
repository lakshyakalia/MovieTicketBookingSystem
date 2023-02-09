package Server;

import Services.MovieTicketService;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.rmi.Naming;

public class ATWServer extends MovieTicketService {

    public ATWServer() throws Exception {
        super();
    }

    public static void main(String[] args) throws Exception {
        DatagramSocket ds = new DatagramSocket(4556);

        MovieTicketService atwMovieService = new MovieTicketService("atw","ATWATER");
        Naming.bind("rmi://localhost/atw", atwMovieService);
        System.out.println("ATW Server started...");

//        Runnable task = () -> {
            requestListener(atwMovieService,ds);
//        };

    }
    public static void requestListener(MovieTicketService atwMovieService, DatagramSocket ds) {
        try{
            String callbackResponse = "";

            byte[] byteRequest = new byte[1024];
            while (true){

                DatagramPacket dp = new DatagramPacket(byteRequest,byteRequest.length);
                ds.receive(dp);
                String requestString = new String(dp.getData(), 0, dp.getLength()).trim();
                String[] requestStringArr = requestString.split(";");
                String func = requestStringArr[0];
                String userID = requestStringArr[1];
                String movieName = requestStringArr[2];
                String movieID = requestStringArr[3];
                int noOfTickets = Integer.parseInt(requestStringArr[4]);

                switch (func) {
                    case "listMovieShowAvailability": {
                        String res = atwMovieService.listMovieShowAvailabilityUDP(movieName);
                        callbackResponse = requestStringArr + ";" + res;
                        break;
                    }
                    case "bookMovieTickets": {
                        String res = atwMovieService.bookMovieTickets(userID,movieID,movieName,noOfTickets);
                        callbackResponse = requestStringArr + ";" + res;
                        break;
                    }
                    case "getBookingSchedule": {
                        String res = atwMovieService.getBookingScheduleUDP(userID);
                        callbackResponse = requestStringArr + ";" + res;
                        break;
                    }
                    case "cancelMovieTickets": {
                        String res = atwMovieService.cancelMovieTickets(userID,movieID,movieName,noOfTickets);
                        callbackResponse = requestStringArr + ";" + res;
                        break;
                    }
                }
//                byte[] byteToSend = callbackResponse.trim().getBytes();
                byte[] byteToSend = callbackResponse.getBytes();
                InetAddress ia = InetAddress.getLocalHost();
                DatagramPacket response = new DatagramPacket(byteToSend, byteToSend.length, ia,
                        dp.getPort());
                ds.send(response);
//                ds.close();
            }

        }
        catch (SocketException e){
            System.out.println("Socket exception: " + e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
