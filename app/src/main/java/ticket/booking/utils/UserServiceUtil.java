package ticket.booking.utils;

public class UserServiceUtil {

    public static String generateTicketId() {
        return "" + System.currentTimeMillis();
    }
}
