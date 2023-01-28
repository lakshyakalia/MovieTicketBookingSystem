package Server;

import Services.MovieTicketService;

import java.rmi.Naming;

public class ATWServer extends MovieTicketService {

    public ATWServer() throws Exception {
    }

    public static void main(String[] args) throws Exception {

        MovieTicketService atwMovieService = new MovieTicketService();
        Naming.bind("rmi://localhost/atw", atwMovieService);
        System.out.println("ATW Server started...");
    }
}
