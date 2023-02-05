package Interface;

import java.io.IOException;
import java.rmi.RemoteException;

public interface CustomerInterface {

    /**
     * Book Movie Tickets
     * @param customerID
     * @param movieID
     * @param movieName
     * @param noOfTickets
     * @return
     */
    public String bookMovieTickets(String customerID, String movieID, String movieName, int noOfTickets) throws RemoteException;

    /**
     * Get list of all bookings
     * by the user
     * @param customerID
     * @return
     */
    public String getBookingSchedule(String customerID) throws IOException;

    /**
     * Cancel any tickets booked
     * by the user
     * @param customerID
     * @param movieID
     * @param movieName
     * @param noOfTickets
     * @return
     */
    public String cancelMovieTickets(String customerID, String movieID, String movieName, int noOfTickets) throws RemoteException;
}
