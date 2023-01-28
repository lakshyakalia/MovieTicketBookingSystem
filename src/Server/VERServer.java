package Server;

import Services.MovieTicketService;

import java.rmi.Naming;

public class VERServer extends MovieTicketService {

    public VERServer() throws Exception {
    }

    public static void main(String[] args) throws Exception {

        MovieTicketService verMovieService = new MovieTicketService();
        Naming.bind("rmi://localhost/ver", verMovieService);
        System.out.println("VER Server started...");

    }
}
