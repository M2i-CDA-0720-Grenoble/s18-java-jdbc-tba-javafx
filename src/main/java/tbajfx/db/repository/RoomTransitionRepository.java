package tbajfx.db.repository;

import java.sql.*;
import java.util.HashMap;
import java.util.List;

import tbajfx.db.entity.Direction;
import tbajfx.db.entity.Room;
import tbajfx.db.entity.RoomTransition;
import tbajfx.db.Repository;

public final class RoomTransitionRepository extends Repository<RoomTransition> {
    
    @Override
    public String getTableName()
    {
        return "room_transition";
    }

    @Override
    public RoomTransition instantiateFromResultSet(ResultSet set) throws SQLException
    {
        return new RoomTransition(
            set.getInt("id"),
            set.getInt("from_room_id"),
            set.getInt("to_room_id"),
            set.getInt("direction_id")
        );
    }

    public List<RoomTransition> findAllFromRoom(Room room)
    {
        return findByCriteria( new HashMap<String, String>(){{
            put("from_room_id", Integer.toString(room.getId()));
        }});
    }

    public RoomTransition findByFromRoomAndDirection(Room room, Direction direction)
    {
        List<RoomTransition> transitions = findByCriteria( new HashMap<String, String>(){{
            put("from_room_id", Integer.toString(room.getId()));
            put("direction_id", Integer.toString(direction.getId()));
        }});

        if (transitions.isEmpty()) {
            return null;
        }

        return transitions.get(0);
    }

}
