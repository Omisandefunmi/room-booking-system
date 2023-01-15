package coursework2;

public class Guest {
	private Integer roomNumber;
    private String email;

    public Guest(Integer roomNumber, String email) {
        this.email = email;
        this.roomNumber = roomNumber;
    }

    public Integer getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(Integer roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
