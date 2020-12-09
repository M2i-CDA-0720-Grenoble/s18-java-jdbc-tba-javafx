package tbajfx.db.repository;

import java.sql.*;

import tbajfx.db.entity.Direction;
import tbajfx.db.Repository;

public final class DirectionRepository extends Repository<Direction> {
    
    public String getTableName()
    {
        return "direction";
    }

    public Direction instantiateFromResultSet(ResultSet set) throws SQLException
    {
        return new Direction(
            set.getInt("id"),
            set.getString("name"),
            set.getString("command")
        );
    }

}
