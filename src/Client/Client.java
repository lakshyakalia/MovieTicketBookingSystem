package Client;

import Constants.Constant;
import Interface.AdminInterface;
import Interface.CustomerInterface;

import java.io.FileWriter;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Client extends Constant {
    public static String currentUser;
    public static String log;
    public static void main(String[] args) throws Exception {
        startProg();
    }
    public static void startProg() throws IOException, NotBoundException {
        System.out.println("Please enter user id: ");
        Scanner sc = new Scanner(System.in);
        String userID = sc.nextLine();

        boolean isAdmin = checkAdmin(userID);
        String serverPort = getServerPort(userID);
        currentUser = userID;

        if(isAdmin){
            AdminInterface adminRef = (AdminInterface) Naming.lookup(serverPort);

            while(true){
                int option = Integer.parseInt(showAdminMenu());
                Scanner sc2 = new Scanner(System.in);
                switch (option){
                    case 1:
                    {
                        String addMovieID = "";

                        /**
                         * Condition for admin adding only
                         * those movie which falls in his
                         * server domain
                         */
                        while (true){
                            System.out.println("Please enter Movie ID");
                            addMovieID = sc2.nextLine();
                            if(addMovieID.substring(0,3).equals(userID.substring(0,3))){
                                break;
                            }
                            else {
                                System.out.println("Incorrect Server. Please try again.");
                            }
                        }

                        System.out.println("Please enter Movie Name");
                        String addMovieName = sc2.nextLine();
                        System.out.println("Please enter Booking Capacity");
                        int addBookingCapacity = Integer.parseInt(sc2.nextLine());
                        log="Add Movie Slots";
                        String res = adminRef.addMovieSlots(addMovieID, addMovieName, addBookingCapacity);
                        writeToLogFile("addMovieSlots",userID+" "+addMovieID+" "+addMovieName+" "+addBookingCapacity,res);
                        System.out.println(res);
                        break;
                    }
                    case 2:
                    {
                        System.out.println("Please enter Movie Name");
                        String removeMovieName = sc2.nextLine();

                        String removeMovieID = "";

                        while (true){
                            System.out.println("Please enter Movie ID");
                            removeMovieID = sc2.nextLine();
                            if(removeMovieID.substring(0,3).equals(userID.substring(0,3))){
                                break;
                            }
                            else {
                                System.out.println("Incorrect Server. Please try again.");
                            }
                        }

                        log="Remove Movie Slots";
                        String res = adminRef.removeMovieSlots(removeMovieID, removeMovieName);
                        writeToLogFile("removeMovieSlots",userID+" "+removeMovieID+" "+removeMovieName,res);
                        System.out.println(res);
                        break;
                    }
                    case 3:{
                        System.out.println("Please enter Movie Name");
                        String listMovieName = sc2.nextLine();
                        log="List Movie Show Availability";
                        String res = adminRef.listMovieShowAvailability(listMovieName);
                        writeToLogFile("listMovieShowsAvailability",userID+" "+listMovieName,res);
                        System.out.println(res);
                        break;
                    }
                    case 4:{
                        System.out.println("Please enter Movie ID");
                        String bookMovieID = sc2.nextLine();
                        System.out.println("Please enter Movie Name");
                        String bookMovieName = sc2.nextLine();
                        System.out.println("Please enter No of Tickets to Book");
                        int bookNumberOfTickets = Integer.parseInt(sc2.nextLine());
                        log="Book Movie Tickets";
                        String res = adminRef.bookMovieTickets(userID, bookMovieID, bookMovieName, bookNumberOfTickets);
                        writeToLogFile("bookMovieTickets",userID+" "+bookMovieID+" "+bookMovieName+" "+bookNumberOfTickets,res);
                        System.out.println(res.split(";")[res.split(";").length-1]);
                        break;
                    }
                    case 5:{
                        log="Get Booking Done by user";
                        String res = adminRef.getBookingSchedule(userID);
                        writeToLogFile("getBookingSchedule",userID,res);
                        System.out.println(res);
                        break;
                    }
                    case 6:{
                        System.out.println("Please enter Movie ID");
                        String cancelBookMovieID = sc2.nextLine();
                        System.out.println("Please enter Movie Name");
                        String cancelBookMovieName = sc2.nextLine();
                        System.out.println("Please enter No of Tickets to Cancel");
                        int cancelBookNumberOfTickets = Integer.parseInt(sc2.nextLine());
                        log="Tickets Cancelled.";
                        String res = adminRef.cancelMovieTickets(userID, cancelBookMovieID, cancelBookMovieName, cancelBookNumberOfTickets);
                        writeToLogFile("cancelMovieTickets",userID+" "+cancelBookMovieID+" "+cancelBookMovieName+" "+cancelBookNumberOfTickets,res);
                        System.out.println(res);
                        break;
                    }
                    case 7:{
                        startProg();
                        break;
                    }
                    default:
                        break;
                }
            }
        }
        else {
            CustomerInterface customerRef = (CustomerInterface) Naming.lookup(serverPort);
            while(true){
                int option = Integer.parseInt(showCustomerMenu());
                Scanner sc2 = new Scanner(System.in);

                switch (option){
                    case 1:
                    {
                        System.out.println("Please enter Movie ID");
                        String bookMovieID = sc2.nextLine();
                        System.out.println("Please enter Movie Name");
                        String bookMovieName = sc2.nextLine();
                        System.out.println("Please enter No of Tickets to Book");
                        int bookNumberOfTickets = Integer.parseInt(sc2.nextLine());
                        log="Book Movie Tickets";
                        String res = customerRef.bookMovieTickets(userID, bookMovieID, bookMovieName, bookNumberOfTickets);
                        writeToLogFile("bookMovieTickets",userID+" "+bookMovieID+" "+bookMovieName+" "+bookNumberOfTickets,res);
                        System.out.println(res.split(";")[res.split(";").length-1]);
                        break;
                    }
                    case 2:
                    {
                        log="Get Booking Done by user";
                        String res = customerRef.getBookingSchedule(userID);
                        writeToLogFile("getBookingSchedule",userID,res);
                        System.out.println(res);
                        break;
                    }
                    case 3:
                    {
                        System.out.println("Please enter Movie ID");
                        String cancelBookMovieID = sc2.nextLine();
                        System.out.println("Please enter Movie Name");
                        String cancelBookMovieName = sc2.nextLine();
                        System.out.println("Please enter No of Tickets to Cancel");
                        int cancelBookNumberOfTickets = Integer.parseInt(sc2.nextLine());
                        log="Tickets Cancelled.";
                        String res = customerRef.cancelMovieTickets(userID, cancelBookMovieID, cancelBookMovieName, cancelBookNumberOfTickets);
                        writeToLogFile("cancelMovieTickets",userID+" "+cancelBookMovieID+" "+cancelBookMovieName+" "+cancelBookNumberOfTickets,res);
                        System.out.println(res);
                        break;
                    }
                    case 4:{
                        startProg();
                        break;
                    }
                }
            }


        }
    }

    public static boolean checkAdmin(String userID){
        if(userID.charAt(3) == 'A'){
            return true;
        }
        else if(userID.charAt(3) == 'C'){
            return false;
        }
        else {
            return false;
        }
    }

    public static String getServerPort(String userID){
        String serverSubstring = userID.substring(0,3);
        switch (serverSubstring){
            case "ATW":
                return getATWServer();
            case "OUT":
                return getOUTServer();
            case "VER":
                return getVERServer();
        }
        return null;
    }

    public static String showAdminMenu(){
        System.out.println("Please select one of the following options: \n" +
                "1. Add Movie Slots\n" +
                "2. Remove Movie Slots\n" +
                "3. List Movie Shows Availability\n" +
                "4. Book Movie Ticket\n" +
                "5. Get Booking Schedule\n" +
                "6. Cancel Movie Tickets\n" +
                "7. Logout");
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        return input;
    }
    public static String showCustomerMenu(){
        System.out.println("Please select one of the following options: \n" +
                "1. Book Movie Ticket\n" +
                "2. Get Booking Schedule\n" +
                "3. Cancel Movie Tickets\n" +
                "4. Logout");
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        return input;
    }
    public static void writeToLogFile(String operation, String params, String response) {
        try {
            FileWriter myWriter = new FileWriter("C:\\Users\\laksh\\Intellij-workspace\\MovieTicketsBookingSystem\\src\\Logs\\"+currentUser+".txt",true);
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String log = dateFormat.format(LocalDateTime.now()) + " : " + operation + " : " + params + " : "
                    + " : " + response + "\n";
            myWriter.write(log);
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
