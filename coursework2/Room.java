package coursework2;

public class Room {private int roomNumber;
private RoomType roomType;
private boolean hasBalcony;
private boolean hasLounge;
private String status;
private double roomPrice;


public Room(){

}
public Room(int roomNumber, RoomType roomType, boolean hasBalcony, boolean hasLounge, String status, double roomPrice) {
    this.roomNumber = roomNumber;
    this.roomType = roomType;
    this.hasBalcony = hasBalcony;
    this.hasLounge = hasLounge;
    this.status = status;
    this.roomPrice = roomPrice;
}

public Room(int roomNumber, RoomType roomType, boolean hasBalcony, boolean hasLounge, double roomPrice) {
    this.roomNumber = roomNumber;
    this.roomType = roomType;
    this.hasBalcony = hasBalcony;
    this.hasLounge = hasLounge;
    this.status = "free";
    this.roomPrice = roomPrice;

}

public int getRoomNumber() {
    return roomNumber;
}

public void setRoomNumber(int roomNumber) {
    this.roomNumber = roomNumber;
}

public RoomType getRoomType() {
    return roomType;
}

public void setRoomType(RoomType roomType) {
    this.roomType = roomType;
}

public boolean isHasBalcony() {
    return hasBalcony;
}

public void setHasBalcony(boolean hasBalcony) {
    this.hasBalcony = hasBalcony;
}

public boolean isHasLounge() {
    return hasLounge;
}

public void setHasLounge(boolean hasLounge) {
    this.hasLounge = hasLounge;
}

public String getStatus() {
    return status;
}

public void setStatus(String status) {
    this.status = status;
}

public double getRoomPrice() {
    return roomPrice;
}

public void setRoomPrice(double roomPrice) {
    this.roomPrice = roomPrice;
}

@Override
public String toString() {
    return String.format("""
           [ roomNumber : %d
             roomType : %s
             hasBalcony : %s
             hasLounge : %s
             status : %s
             roomPrice : %.2f ]%n""",roomNumber,roomType,hasBalcony, hasLounge, status, roomPrice);
}

}
