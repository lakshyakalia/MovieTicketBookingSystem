package Client;

import Interface.AdminInterface;

import java.rmi.Naming;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws Exception {

        System.out.println("Please enter user id: ");
        Scanner sc = new Scanner(System.in);
        String userID = sc.nextLine();

        boolean isAdmin = checkAdmin(userID);
        if(isAdmin){
//            AdminInterface adminObj =
        }
        else {
            AdminInterface adminObj = (AdminInterface) Naming.lookup("hi");
            adminObj.test();
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
}
