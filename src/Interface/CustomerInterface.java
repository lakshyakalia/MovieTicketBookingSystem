package Interface;

public interface CustomerInterface {

    /**
     * Book Movie Tickets
     * @param customerID
     * @param movieID
     * @param movieName
     * @param noOfTickets
     * @return
     */
    public String bookMovieTickets(String customerID, String movieID, String movieName, int noOfTickets);

    /**
     * Get list of all bookings
     * by the user
     * @param customerID
     * @return
     */
    public String getBookingSchedule(String customerID);

    /**
     * Cancel any tickets booked
     * by the user
     * @param customerID
     * @param movieID
     * @param movieName
     * @param noOfTickets
     * @return
     */
    public String cancelMovieTickets(String customerID, String movieID, String movieName, int noOfTickets);
}
