package tbajfx.game;

import tbajfx.db.entity.Room;
import tbajfx.db.repository.RoomRepository;

public final class GameState {
    
    private int currentRoomId;

    public GameState(int roomId)
    {
        currentRoomId = roomId;
    }

    public Room getCurrentRoom() {
        RoomRepository repository = new RoomRepository();
        return repository.findById( currentRoomId );
    }

    public void setCurrentRoom(Room room) {
        currentRoomId = room.getId();
    }

}
