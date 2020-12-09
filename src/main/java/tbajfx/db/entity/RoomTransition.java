package tbajfx.db.entity;

import java.util.HashMap;

import tbajfx.db.repository.DirectionRepository;
import tbajfx.db.repository.RoomRepository;

public class RoomTransition extends Entity {
    
    private int fromRoomId;
    private int toRoomId;
    private int directionId;

    public RoomTransition()
    {
        id = 0;
        fromRoomId = 0;
        toRoomId = 0;
        directionId = 0;
    }

    public RoomTransition(int id, int fromRoomId, int toRoomId, int directionId) {
        this.id = id;
        this.fromRoomId = fromRoomId;
        this.toRoomId = toRoomId;
        this.directionId = directionId;
    }

    @Override
    public HashMap<String, String> getProperties() {
        return new HashMap<String, String>() {{
            put("from_room_id", Integer.toString(fromRoomId));
            put("to_room_id", Integer.toString(toRoomId));
            put("direction_id", Integer.toString(directionId));
        }};
    }

    @Override
    public String toString()
    {
        return "[RoomTransition #" + id + "] {\n fromRoom: " + getFromRoom() + ",\n toRoom: " + getToRoom() + ",\n direction: " + getDirection() + "\n}";
    }

    public int getId() {
        return id;
    }

    public Room getFromRoom()
    {
        RoomRepository repository = new RoomRepository();
        return repository.findById(fromRoomId);
    }

    public Room getToRoom()
    {
        RoomRepository repository = new RoomRepository();
        return repository.findById(toRoomId);
    }

    public RoomTransition setFromRoom(Room room)
    {
        fromRoomId = room.getId();
        return this;
    }

    public RoomTransition setToRoom(Room room)
    {
        toRoomId = room.getId();
        return this;
    }

    public Direction getDirection()
    {
        DirectionRepository repository = new DirectionRepository();
        return repository.findById(directionId);
    }

    public RoomTransition setDirection(Direction direction)
    {
        directionId = direction.getId();
        return this;
    }

}
