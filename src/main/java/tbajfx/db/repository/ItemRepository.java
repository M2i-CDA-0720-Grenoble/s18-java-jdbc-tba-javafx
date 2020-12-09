package tbajfx.db.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import tbajfx.db.entity.Item;
import tbajfx.db.Repository;

public class ItemRepository extends Repository<Item> {
    
    public String getTableName()
    {
        return "item";
    }

    public Item instantiateFromResultSet(ResultSet set) throws SQLException
    {
        return new Item(
            set.getInt("id"),
            set.getString("name"),
            set.getString("description"),
            set.getInt("room_id")
        );
    }

}
