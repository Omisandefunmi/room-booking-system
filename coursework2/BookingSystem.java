package coursework2;

import java.util.*;

public class BookingSystem {
    private static final Map<Integer, Room> rooms = new HashMap<>();
    private static final Map<Guest, Room> reservedRooms = new HashMap<>();
    private static final ArrayList<Room> singleRooms = new ArrayList<>();
    private static final ArrayList<Room> doubleRooms = new ArrayList<>();
    private static final ArrayList<Room> suiteRooms = new ArrayList<>();
    private static final Scanner input = new Scanner(System.in);
    private static String roomChoice;
    private static String balconyChoice;
    private static String loungeChoice;
    private static double budgetChoice;
    private static Room nextBestMatch;
    private static double lowestRoomPrice;
    private static String roomType;

    public static void runApp() {
        /*
         This method runs all the methods of the application when called.
         */
        Utils.loadFromFileToStorage(rooms);
        sortRoomsByType();
        menuHandler();
    }

    private static void displayMenu() {
        /*
        This method displays the major functions of the application.
        */
        System.out.println("""
                - Room Booking System - -
                 
                - - MAIN MENU - -
                                
                1 - Reserve Room
                2 - Cancel Room
                3 - View Room Reservations
                Q - Quit
                Pick:
                """);
    }

    private static void menuHandler(){
        /*
        This method displays menu and processes user's input
        */
        displayMenu();
        analyzeMenuResponse();
    }
    private static void sortRoomsByType(){
         /*
        This method sorts the rooms into respective maps according their room types.
        */
        for (Map.Entry<Integer, Room> room : rooms.entrySet()) {
            Room actualRoom = room.getValue();
            if(actualRoom.getRoomType() == RoomType.SINGLE){
                singleRooms.add(actualRoom);
            }
            else if(actualRoom.getRoomType() == RoomType.DOUBLE){
                doubleRooms.add(actualRoom);
            }
            else {
                suiteRooms.add(actualRoom);
            }
        }
    }
    private static void analyzeMenuResponse() {
         /*
        This method processes user's input and displays methods that correspond to  user's choice
        */
        String choice = input.next();
        switch (choice.toUpperCase()) {
            case "1": reserveRoom();
                        break;
            case "2": cancel();
                        break;
            case "3": viewRoomReservations();
                        break;
            case "Q": exitApp();
                        break;
            default: System.out.println("Invalid input, please try again");
                        menuHandler();
        }
    }

    private static void cancel() {
         /*
        This method prompts users for reservation cancelling details and cancels if details are correct.
        */
        System.out.println("Enter the room number or press 0 to return to main menu");
        int roomNumber = Integer.parseInt(input.next());
        if(roomNumber == 0){
            menuHandler();
        }

        System.out.println("Enter the email used to book the room");
        String email = input.next();

        Room room = rooms.get(roomNumber);
        if(room == null){
            System.out.println("No such room!! \nKindly enter a correct number");
            cancel();
        }
        else if(email == null){
            System.out.println("Email field cannot be empty");
            cancel();
        }

        else if(!room.getStatus().equalsIgnoreCase(email)){
            System.out.printf("Cancelling failed for room %d as either email or room number does not match yours. %n Try again later!!!%n%n", roomNumber);
            menuHandler();
        }
        else{
            room.setStatus("free");
            rooms.put(roomNumber, room);
            removeFromReservedRoom(roomNumber);
            System.out.printf("Your reservation for room %d has been cancelled successfully!!!%n%n", roomNumber);
            menuHandler();
        }
    }

    private static void removeFromReservedRoom(int roomNumber) {
         /*
        This method removes a room from the database of reserved rooms when the guest cancel reservation.
        */
        for(Map.Entry< Guest, Room> entry : reservedRooms.entrySet()){
           Guest guest = entry.getKey();
            if(guest.getRoomNumber() == roomNumber){
                reservedRooms.remove(guest);
            }
        }
    }

    private static void effectBooking(Room room){
         /*
        This method books rooms.
        */
        System.out.println("Enter your email to complete booking");
        String email = input.next();

        Guest guest = new Guest(room.getRoomNumber(),email);
        room.setStatus(email);
        rooms.put(room.getRoomNumber(), room);
        reservedRooms.put(guest, room);
        System.out.println("Room booking successful with room number "+room.getRoomNumber());
        if(nextBestMatch != null){
            removeNextBestMatchFromUnoccupiedRoomsList();
        }
        lowestRoomPrice = 0.0;
    }

    private static void removeNextBestMatchFromUnoccupiedRoomsList() {
         /*
        This method returns the next best match variable to null when called.
        */
        if(roomChoice.equals("A")){
            singleRooms.remove(nextBestMatch);
            nextBestMatch = null;
        }
        else if(roomChoice.equals("B")){
            doubleRooms.remove(nextBestMatch);
            nextBestMatch = null;
        }
        else {
            suiteRooms.remove(nextBestMatch);
            nextBestMatch = null;
        }
    }

    private static void reserveRoom() {
         /*
        This method uses all methods pertaining to room reservation to reserve rooms.
        */
        promptForUserPreferences();

        Room roomMatch = matchRoom();
        if(roomMatch == null && nextBestMatch != null){
            System.out.println("""
                    Sorry, we could not find a room that matches your specifications.
                    
                    However, we found a next best match.
                    
                    """);
            displayNextBestMatchDetails();
            promptForUserDecision();
        }
        else if(roomMatch == null){
            System.out.println("""
                    Sorry, there are no rooms available to fit your preference and we could not find a close match.
                    """);
            menuHandler();
        }
        else{
            effectBooking(roomMatch);
            menuHandler();
        }
        System.out.println(roomMatch);

    }

    private static void promptForUserDecision() {
         /*
        This method provides options to users to either book a next best match room or not.
        */
        System.out.println("""
                Choose an option
                
                A. Book next best match
                B. Return to main menu
                """);
        String response = input.next();
        switch (response.toUpperCase()){
            case "A": effectBooking(nextBestMatch); menuHandler(); break;
            case "B": menuHandler(); break;
            default:  {System.out.println("Invalid response!!! You only type either letter A or B");
                        promptForUserDecision();}


        }
    }

    private static void displayNextBestMatchDetails() {
         /*
        This method displays details of next best match room.
        */
        System.out.printf("""
                
                Room number: %s
                Room type: %s
                Room price: %5.2f
                Has balcony: %s
                Has lounge: %s
                
                """, nextBestMatch.getRoomNumber(),String.valueOf(nextBestMatch.getRoomType()).toLowerCase(),
                nextBestMatch.getRoomPrice(), nextBestMatch.isHasBalcony(), nextBestMatch.isHasLounge());

    }

    private static Room matchRoom() {
         /*
        This method finds room match according to the user's room type preference
        and also assign a next best match if no match is found
        */
        if(roomChoice.equals("A")){
            Room room = findRoomMatch(singleRooms);
            if(room != null){
            singleRooms.remove(room);
            }
            else{
                if(!singleRooms.isEmpty()){
                nextBestMatch = singleRooms.get(0);}
            }
            return room;
        }
        else if(roomChoice.equals("B")){
            Room room = findRoomMatch(doubleRooms);
            if(room != null){
                doubleRooms.remove(room);
            }
            else{
                if(!doubleRooms.isEmpty()){
                nextBestMatch = doubleRooms.get(0);}
            }
            return room;
        }
        else {
            Room room = findRoomMatch(suiteRooms);
            if(room != null){
                suiteRooms.remove(room);}
            else{
                if(!suiteRooms.isEmpty()){
                nextBestMatch = suiteRooms.get(0);}
                }
            return findRoomMatch(suiteRooms);
        }
    }

    private static Room findRoomMatch(ArrayList<Room> roomList) {
         /*
        This method finds actual room match
        */
        if(roomList.size() == 0){
            return null;
        }
        if(balconyChoice.equals("A")){
            for (int i = 0; i < roomList.size(); i++) {
                Room room = roomList.get(i);
                if(room.isHasBalcony()){
                    if(loungeChoice.equals("A")) {
                        if (room.isHasLounge()) {
                            return room;
                        }
                    }
                    else{
                        if(!room.isHasLounge()){
                            return room;
                        }
                    }
                }
            }
        }
        else{
            for (int i = 0; i < roomList.size(); i++) {
                Room room = roomList.get(i);
                if(!room.isHasBalcony()){
                    if(loungeChoice.equals("A")) {
                        if (room.isHasLounge()) {
                            return room;
                        }
                    }
                    else{
                        if(!room.isHasLounge()){
                            return room;
                        }
                    }
                }
            }
        }
        nextBestMatch = roomList.get(0);
        return null;
    }

    private static void promptForUserPreferences() {
        /*
        This method runs all method that prompt user for preferences.
        */
        roomChoice = promptForRoomChoice();
        validateRoomPreference(roomChoice);
        assignLowestPrice();
        balconyChoice = promptForBalconyPreference();

        loungeChoice = promptForLoungePreference();

        budgetChoice = promptForUserBudget();
    }

    private static void validateRoomPreference(String choice) {
         /*
        This method checks that user's input is correct
        */
        if (!(choice.equals("A") || choice.equals("B") || choice.equals("C"))) {
            System.out.println("Invalid response!!! You only type either letter A or B or C");
            System.out.println("Try Again!");
            promptForRoomChoice();
        }
    }

    private static String validateUserInput(String choice) {
        /*
        This method checks that user's input is correct
        */
        if (!(choice.equals("A") || choice.equals("B"))) {
            return """
                        Invalid response!!! You only type either letter A or B
                        Try Again!
                        """;
        }
        return null;
    }

    private static String promptForLoungePreference() {
        /*
        This method prompts for user's lounge preference
        */
        System.out.println("""
                Do you want a room that has a lounge?
                
                A. Yes
                B. No
                
                """);
       String choice = input.next().toUpperCase();
       String errorMessage = validateUserInput(choice);
        if(errorMessage != null) {
            System.out.println(errorMessage);
            promptForLoungePreference();
        }
       return choice;
    }

    private static String promptForBalconyPreference() {
         /*
        This method prompts for user's balcony preference
        */
        System.out.println("""
                Do you want a room that has a balcony?
                
                A. Yes
                B. No
                
                """);
        String choice = input.next().toUpperCase();
        String errorMessage = validateUserInput(choice);
        if(errorMessage != null) {
            System.out.println(errorMessage);
            promptForBalconyPreference();
        }

        return choice;
    }

    private static String promptForRoomChoice() {
         /*
        This method prompts for user's room type preference
        */
        System.out.println("""
                What type of room do you wish to book?
                
                A. Single
                B. Double
                C. Suite
                """);

        return input.next().toUpperCase();
    }
    private static void assignLowestPrice(){
         /*
        This method gets the lowest price possible for a room type and assigns it to the lowestRoomPrice variable
        */
        if (roomChoice.equals("A")){
            if(!singleRooms.isEmpty()) {
                lowestRoomPrice = singleRooms.get(0).getRoomPrice();
                roomType = String.valueOf(singleRooms.get(0).getRoomType());
            }
        }
        if (roomChoice.equals("B")){
            if(!doubleRooms.isEmpty()) {
                lowestRoomPrice = doubleRooms.get(0).getRoomPrice();
                roomType = String.valueOf(doubleRooms.get(0).getRoomType());
            }
        }
        if(roomChoice.equals("C")){
            if(!suiteRooms.isEmpty()) {
                lowestRoomPrice = suiteRooms.get(0).getRoomPrice();
                roomType = String.valueOf(suiteRooms.get(0).getRoomType());
            }
        }
    }

    private static double promptForUserBudget() {
         /*
        This method prompts for user's budget preference
        */
        double budget = 0;
        try{
        System.out.println("""
                What is your budget?
                      
                """);
        budget =  input.nextDouble();
        if(budget < 0){
            System.out.println("Enter positive numbers");
            budget = promptForUserBudget();
        }

            if(budget < lowestRoomPrice){
            System.out.printf("""
                Budget price too low. Kindly upgrade budget.
                The lowest price for a %s room type is %5.2f
                """,roomType.toLowerCase(), lowestRoomPrice );
            budget = promptForUserBudget();
        }
        }
        catch (InputMismatchException e){
            System.out.println("Enter digits only: ");
            input.next();
            promptForUserBudget();
        }
        return budget;
    }



    private static void viewRoomReservations(){
         /*
        This method displays all room reservations.
        */
        if(reservedRooms.size() == 0){
            System.out.println("There are no reserved rooms at the moment \nCheck back later!!!\n");
        }
        else {
            for (Map.Entry<Guest, Room> entry : reservedRooms.entrySet()) {
                Room room = entry.getValue();
                Guest guest = entry.getKey();
                System.out.printf("""
                                                
                                Room number: %s
                                Room type: %s
                                Room price: %5.2f
                                Has balcony: %s
                                Has lounge: %s
                                Reserved by: %s
                                                
                                """, room.getRoomNumber(), String.valueOf(room.getRoomType()).toLowerCase(),
                        room.getRoomPrice(), room.isHasBalcony(), room.isHasLounge(), guest.getEmail());
            }

        }
        returnToMainMenu();
    }

    private static void returnToMainMenu() {
         /*
        This method returns user to the application's main menu.
        */
        System.out.println("To return to main menu, press 1");
        String choice = input.next();
        if(choice.equals("1")){
            menuHandler();
        }
        else{
            System.out.println("You have entered an invalid input. \nPlease try again");
            returnToMainMenu();
        }
    }

    private static void exitApp() {
         /*
        This method ends the application.
        */
        Utils.writeRoomDetailsBackToFile(rooms);
        System.out.println("""
                Thank you for your patronage!!!
                """);
        System.exit(0);
    }
}
