package coolthings.joey.grant.bulldozerxtreme.socket;

public class Room {
    public final String id;


    private int playerNumber;

    public Room(String id) {
        this.id = id;
    }

    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public class JSON {
        public static final String id = "roomId";
        public static final String playerNumber = "playerNumber";
    }
}
