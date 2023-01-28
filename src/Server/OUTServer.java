package Server;

import Services.MovieTicketService;

import java.rmi.Naming;

public class OUTServer extends MovieTicketService {

    public OUTServer() throws Exception {
    }

    public static void main(String[] args) throws Exception {

        MovieTicketService outMovieService = new MovieTicketService();
        Naming.bind("rmi://localhost/out", outMovieService);
        System.out.println("OUT Server started...");

    }
}
