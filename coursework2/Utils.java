package coursework2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.NoSuchElementException;

public class Utils {
	private static final String fileName = "rooms.txt";


    static void loadFromFileToStorage(Map<Integer, Room> rooms) {
        try(BufferedReader input = Files.newBufferedReader(Paths.get(fileName))) {
            String line;

            while ((line = input.readLine()) != null) {
                Room room = new Room();
                String[] roomComponents = line.split(" ");
                String roomNumber = roomComponents[0];
                String roomType = roomComponents[1];
                String roomPrice = roomComponents[2];
                String hasBalcony = roomComponents[3];
                String hasLounge = roomComponents[4];
                String status = roomComponents[5];
                setRoomVariables(room, roomNumber, roomType, roomPrice, hasBalcony, hasLounge, status);
                rooms.put(room.getRoomNumber(), room);
            }
        } catch (IOException | NoSuchElementException | IllegalStateException e) {
            System.err.println(e.getMessage());
        }
    }

    static void writeRoomDetailsBackToFile(Map<Integer, Room> rooms){
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(Paths.get(fileName))) {
            for (Map.Entry<Integer, Room> room : rooms.entrySet()) {
                Room actualRoom = room.getValue();
                bufferedWriter.write(actualRoom.getRoomNumber()+" "+actualRoom.getRoomType().toString()+" "
                        +actualRoom.getRoomPrice()+" "+actualRoom.isHasBalcony()+" "+actualRoom.isHasLounge()+" "+actualRoom.getStatus());
                bufferedWriter.newLine();
            }
        }
        catch (IOException | IllegalStateException e) {
            System.err.println(e.getMessage());
        }
    }


    private static void setRoomVariables(Room room, String roomNumber, String roomType, String roomPrice, String hasBalcony, String hasLounge, String status) {
        room.setRoomNumber(Integer.parseInt(roomNumber));
        room.setRoomType(RoomType.valueOf(roomType.toUpperCase()));
        room.setRoomPrice(Double.parseDouble(roomPrice));
        room.setHasBalcony(Boolean.parseBoolean(hasBalcony));
        room.setHasLounge(Boolean.parseBoolean(hasLounge));
        room.setStatus(status);
    }
}
