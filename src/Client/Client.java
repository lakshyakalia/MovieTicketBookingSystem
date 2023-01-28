package Client;

import Constants.Constant;
import Interface.AdminInterface;
import Interface.CustomerInterface;

import java.rmi.Naming;
import java.util.Scanner;

public class Client extends Constant {
    public static void main(String[] args) throws Exception {

        System.out.println("Please enter user id: ");
        Scanner sc = new Scanner(System.in);
        String userID = sc.nextLine();

        boolean isAdmin = checkAdmin(userID);
        String serverPort = getServerPort(userID);

        if(isAdmin){
            AdminInterface adminRef = (AdminInterface) Naming.lookup(serverPort);
//            adminRef.test();
            int option = Integer.parseInt(showAdminMenu());
            Scanner sc2 = new Scanner(System.in);
            switch (option){
                case 1:
                    System.out.println("Please enter Movie ID");
                    String movieID = sc2.nextLine();
                    System.out.println("Please enter Movie Name");
                    String movieName = sc2.nextLine();
                    System.out.println("Please enter Booking Capacity");
                    int bookingCapacity = Integer.parseInt(sc2.nextLine());

                    adminRef.addMovieSlots(movieID, movieName, bookingCapacity);
                    break;
//                case 2:

            }
        }
        else {
            CustomerInterface customerRef = (CustomerInterface) Naming.lookup(serverPort);
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
        String serverSubstring = userID.substring(0,2);
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
                "6. Cancel Movie Tickets\n");
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        return input;
    }
}
