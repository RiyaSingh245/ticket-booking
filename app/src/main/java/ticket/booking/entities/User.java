package ticket.booking.entities;

import java.util.List;

public class User {
    private String userName;
    private String userID;
    private String password;
    private String hashedPassword;
    private List<Ticket> bookedTickets;

    public User() {}

    public User(String userName, String userID, String password, String hashedPassword, List<Ticket> bookedTickets) {
        this.userName = userName;
        this.userID = userID;
        this.password = password;
        this.hashedPassword = hashedPassword;
        this.bookedTickets = bookedTickets;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public List<Ticket> getBookedTickets() {
        return bookedTickets;
    }

    public void setBookedTickets(List<Ticket> bookedTickets) {
        this.bookedTickets = bookedTickets;
    }

    public void printBookedTickets() {
        if(bookedTickets.isEmpty()) {
            System.out.println("No tickets booked");
        } else {
            for(Ticket ticket: bookedTickets) {
                System.out.println(ticket.getTicketInfo());
            }
        }
    }
}
