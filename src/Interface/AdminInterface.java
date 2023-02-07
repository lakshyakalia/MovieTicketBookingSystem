package Interface;
import java.io.IOException;
import java.rmi.*;
public interface AdminInterface extends Remote{

    /**
     * Adding Movie Slots
     * @param movieID
     * @param movieName
     * @param bookingCapacity
     * @return
     */
    public int addMovieSlots(String movieID, String movieName, int bookingCapacity) throws RemoteException;

    /**
     * Removing Movie Slots
     * @param movieID
     * @param movieName
     * @return
     */
    public int removeMovieSlots(String movieID, String movieName) throws RemoteException;

    /**
     * List all the no of tickets
     * Available for a particular
     * movie in all locations
     * @param movieName
     * @return
     */
    public String listMovieShowAvailability(String movieName) throws IOException;

    /**
     * Book Movie Tickets
     * @param customerID
     * @param movieID
     * @param movieName
     * @param noOfTickets
     * @return
     */
    public String bookMovieTickets(String customerID, String movieID, String movieName, int noOfTickets) throws IOException;

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
    public String cancelMovieTickets(String customerID, String movieID, String movieName, int noOfTickets) throws IOException;
    public void test() throws RemoteException;
}
