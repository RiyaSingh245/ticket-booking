package ticket.booking.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import ticket.booking.entities.Ticket;
import ticket.booking.entities.Train;
import ticket.booking.entities.User;
import ticket.booking.utils.UserServiceUtil;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class UserBookingService {
    private User user;
    private List<User> userList;
    private final ObjectMapper objectMapper;
    private final String USER_PATH = "app/src/main/java/ticket/booking/localDb/users.json";

    public UserBookingService() throws IOException {
        objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        loadUsers();
    }

    private void loadUsers() throws IOException {
        userList = objectMapper.readValue(new File(USER_PATH), new TypeReference<List<User>>() {});
    }

    public boolean signUp(User user) throws IOException {
        try {
            Optional<User> foundUser = userList.stream().filter(user1 -> {
                return user1.getUserName().equals(user.getUserName());
            }).findFirst();

            if(foundUser.isPresent()) {
                System.out.println("Username already taken");
                return false;
            }

            userList.add(user);
            saveUserListToFile();
        } catch (Exception ex) {
            System.out.println("saving user list to file failed " + ex.getMessage());
            return false;
        }
        return true;
    }

    private void saveUserListToFile() throws IOException {
        File usersFile = new File(USER_PATH);
        objectMapper.writeValue(usersFile, userList);
    }

    public void fetchBookings() {
        System.out.println("Fetching your bookings");
        user.printBookedTickets();
    }

    public Optional<User> getUserByUserName(String username) {
        return userList.stream().filter(user -> user.getUserName().equals(username)).findFirst();
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean cancelBooking(String ticketID) throws IOException {
        if(ticketID == null || ticketID.isEmpty()) {
            System.out.println("Ticket ID cannot be null or empty");
            return Boolean.FALSE;
        }

        boolean isRemoved = user.getBookedTickets().removeIf(ticket -> ticket.getTicketID().equals(ticketID));
        if(isRemoved) {
            saveUserListToFile();
            System.out.println("Ticket with ID " + ticketID + " has been cancelled.");
            return true;
        } else {
            System.out.println("No ticket found with ID " + ticketID);
            return false;
        }
    }

    public List<Train> getTrains (String source, String destination) throws IOException {
        try {
            TrainService trainService = new TrainService();
            return trainService.searchTrain(source, destination);
        } catch (IOException ex) {
            System.out.println("There is something wrong!");
            return Collections.emptyList();
        }
    }

    public List<List<Integer>> fetchSeats(Train train) {
        return train.getSeats();
    }

    public Boolean bookTrainSeat(Train train, int row, int seat) {
        try {
            TrainService trainService = new TrainService();
            List<List<Integer>> seats = train.getSeats();
            if(row >= 0 && row < seats.size() && seat >= 0 && seat < seats.get(row).size()) {
                if(seats.get(row).get(seat) == 0) {
                    seats.get(row).set(seat, 1);

                    train.setSeats(seats);
                    trainService.addTrain(train);

                    Ticket ticket = new Ticket();

                    ticket.setSource(train.getStations().getFirst());
                    ticket.setDestination(train.getStations().getLast());
                    ticket.setTrain(train);
                    ticket.setUserID(user.getUserID());
                    ticket.setDateOfTravel("2026-09-16");
                    ticket.setTicketID(UserServiceUtil.generateTicketId());

                    user.getBookedTickets().add(ticket);

                    System.out.println("Seat booked successfully  !  ");

                    System.out.println(ticket.getTicketInfo());

                    saveUserListToFile();
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }
}
