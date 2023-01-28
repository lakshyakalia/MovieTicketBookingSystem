package Server;

import Services.MovieTicketService;

import java.rmi.Naming;

public class Server extends MovieTicketService {
    public Server() throws Exception {
    }

    public static void main(String[] args) throws Exception{

        MovieTicketService movieService = new MovieTicketService();
        Naming.bind("hi", movieService);
        System.out.println("Server started");
    }
}
