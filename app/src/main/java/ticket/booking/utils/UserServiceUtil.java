package ticket.booking.utils;

public class UserServiceUtil {

    public static String hashPassword(String plainPassword) {
        return "test";
    }

    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return true;
    }

    public static String generateTicketId() {
        return "" + System.currentTimeMillis();
    }
}
