package Server;

import Services.MovieTicketService;
import movieTicketInterfaceApp.movieTicketInterface;
import movieTicketInterfaceApp.movieTicketInterfaceHelper;
import org.omg.CORBA.ORB;
import org.omg.PortableServer.POA;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.rmi.Naming;

public class VERServer {

    public VERServer() throws Exception {
    }

    public static void main(String[] args){
        try{
            // create and initialize the ORB
            ORB orb = ORB.init(args, null);
            POA rootpoa = (POA)orb.resolve_initial_references("RootPOA");
            rootpoa.the_POAManager().activate();

            MovieTicketService verMovieService = new MovieTicketService("ver","VER");
            verMovieService.setORB(orb);

            org.omg.CORBA.Object ref = rootpoa.servant_to_reference(verMovieService);
            movieTicketInterface href = movieTicketInterfaceHelper.narrow(ref);

            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            org.omg.CosNaming.NamingContextExt ncRef = org.omg.CosNaming.NamingContextExtHelper.narrow(objRef);

            String name = "ver";
            org.omg.CosNaming.NameComponent path[] = ncRef.to_name(name);
            ncRef.rebind(path, href);

            System.out.println("VER Server ready and waiting ...");

            requestListener(verMovieService);
            while (true) {
                orb.run();
            }


        }
        catch (Exception e){
            e.printStackTrace();
        }
//        DatagramSocket ds = new DatagramSocket(4558);

//        MovieTicketService verMovieService = new MovieTicketService("ver","VER");
//        Naming.bind("rmi://localhost/ver", verMovieService);
//        System.out.println("VER Server started...");

//        Runnable task = () -> {
//            requestListener(verMovieService);
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
                        callbackResponse = res;
                        break;
                    }
                    case "exchangeTickets":{
                        String new_movieID = requestStringArr[5];
                        String res = verMovieService.exchangeTickets(userID,movieID,new_movieID,movieName,noOfTickets);
                        callbackResponse = res;
                        break;
                    }
                    case "exchangeTicketsCapacityUDP": {
                        String res = verMovieService.exchangeTicketsCapacityUDP(userID,movieID,movieName,noOfTickets);
                        callbackResponse = res;
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
