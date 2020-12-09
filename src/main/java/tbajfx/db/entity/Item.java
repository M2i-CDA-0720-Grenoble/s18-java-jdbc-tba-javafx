package tbajfx.db.entity;

import java.util.HashMap;

import tbajfx.db.repository.RoomRepository;

public final class Item extends Entity {
    
    protected String name;
    protected String description;
    protected int roomId;

    public Item() {
        id = 0;
        name = "";
        description = "";
        roomId = 0;
    }

    public Item(int id, String name, String description, int roomId)
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.roomId = roomId;
    }

    public HashMap<String, String> getProperties()
    {
        return new HashMap<String, String>() {{
            put("id", Integer.toString(id));
            put("name", name);
            put("description", description);
            put("room_id", Integer.toString(roomId));
        }};
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Room getRoom() {
        RoomRepository repository = new RoomRepository();
        return repository.findById( roomId );
    }

    public void setRoom(Room room) {
        this.roomId = room.getId();
    }

}
