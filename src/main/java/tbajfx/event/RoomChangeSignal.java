package tbajfx.event;

import tbajfx.db.entity.Room;

public final class RoomChangeSignal extends Signal {

    private Room room;

    public RoomChangeSignal(Room room)
    {
        this.room = room;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    @Override
    public String toString()
    {
        return this.getClass().getSimpleName() + " { room: '" + room.getName() + "' }";
    }


}
