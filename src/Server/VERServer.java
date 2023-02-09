package Server;

import Services.MovieTicketService;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.rmi.Naming;

public class VERServer extends MovieTicketService {

    public VERServer() throws Exception {
    }

    public static void main(String[] args) throws Exception {
//        DatagramSocket ds = new DatagramSocket(4558);

        MovieTicketService verMovieService = new MovieTicketService("ver","VERMONT");
        Naming.bind("rmi://localhost/ver", verMovieService);
        System.out.println("VER Server started...");

//        Runnable task = () -> {
            requestListener(verMovieService);
//        };

    }
    public static void requestListener(MovieTicketService verMovieService) {
        try{
            String callbackResponse = "";
            DatagramSocket ds = new DatagramSocket(4558);
            byte[] byteRequest = new byte[1024];
            while (true){
                DatagramPacket dp = new DatagramPacket(byteRequest,byteRequest.length);
                ds.receive(dp);
                String requestString = new String(dp.getData(), 0, dp.getLength());
                String[] requestStringArr = requestString.split(";");
                String func = requestStringArr[0];
                String userID = requestStringArr[1];
                String movieName = requestStringArr[2];
                String movieID = requestStringArr[3];
                int noOfTickets = Integer.parseInt(requestStringArr[4]);

                switch (func) {
                    case "listMovieShowAvailability": {
                        String res = verMovieService.listMovieShowAvailabilityUDP(movieName);
                        callbackResponse = res;
                        break;
                    }
                    case "bookMovieTickets": {
                        String res = verMovieService.bookMovieTickets(userID,movieID,movieName,noOfTickets);
                        callbackResponse = res;
                        break;
                    }
                    case "getBookingSchedule": {
                        String res = verMovieService.getBookingScheduleUDP(userID);
                        callbackResponse = res;
                        break;
                    }
                    case "cancelMovieTickets": {
                        String res = verMovieService.cancelMovieTickets(userID,movieID,movieName,noOfTickets);
                        callbackResponse = requestStringArr + ";" + res;
                        break;
                    }
                }
                byte[] byteToSend = callbackResponse.getBytes();
                DatagramPacket response = new DatagramPacket(byteToSend, byteToSend.length, dp.getAddress(),
                        dp.getPort());
                ds.send(response);

            }

        }
        catch (SocketException e){
            System.out.println("Socket exception: " + e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
