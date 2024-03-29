package Server;

import Services.MovieTicketService;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.rmi.Naming;

public class OUTServer extends MovieTicketService {

    public OUTServer() throws Exception {
    }

    public static void main(String[] args) throws Exception {
//        DatagramSocket ds = new DatagramSocket(4557);

        MovieTicketService outMovieService = new MovieTicketService("out","OUTREMONT");
        Naming.bind("rmi://localhost/out", outMovieService);
        System.out.println("OUT Server started...");

//        byte[] byteReceiveFromClient = new byte[1024];
//
//        DatagramPacket dp = new DatagramPacket(byteReceiveFromClient,byteReceiveFromClient.length);
//        ds.receive(dp);
//        String str = new String(dp.getData());
//        String[] arr = str.split(" ");
//        switch (arr[0]){
//            case "listMovieShowAvailability":
//                outMovieService.listMovieShowAvailability(arr[1]);
//                break;
//        }

//        System.out.println(str);
//        Runnable task = () -> {
            requestListener(outMovieService);
//        };

    }
    public static void requestListener(MovieTicketService outMovieService) {
        try{
            String callbackResponse = "";
            DatagramSocket ds = new DatagramSocket(4557);
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
                        String res = outMovieService.listMovieShowAvailabilityUDP(movieName);
                        callbackResponse = requestStringArr + ";" + res;
                        break;
                    }
                    case "bookMovieTickets": {
                        String res = outMovieService.bookMovieTickets(userID,movieID,movieName,noOfTickets);
                        callbackResponse = requestStringArr + ";" + res;
                        break;
                    }
                    case "getBookingSchedule": {
                        String res = outMovieService.getBookingScheduleUDP(userID);
                        callbackResponse = requestStringArr + ";" + res;
                        break;
                    }
                    case "cancelMovieTickets": {
                        String res = outMovieService.cancelMovieTickets(userID,movieID,movieName,noOfTickets);
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
