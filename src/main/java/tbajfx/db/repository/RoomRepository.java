package tbajfx.db.repository;

import java.sql.*;

import tbajfx.db.entity.Room;
import tbajfx.db.Repository;

public final class RoomRepository extends Repository<Room> {

    public String getTableName()
    {
        return "room";
    }

    public Room instantiateFromResultSet(ResultSet set) throws SQLException
    {
        return new Room(
            set.getInt("id"),
            set.getString("name"),
            set.getString("description")
        );
    }

}
