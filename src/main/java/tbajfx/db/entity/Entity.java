package tbajfx.db.entity;

import java.util.HashMap;

abstract public class Entity {

    protected int id;


    public int getId()
    {
        return id;
    }

    public Entity setId(int id)
    {
        this.id = id;

        return this;
    }

    /**
     * Pack object properties as a collection of { columnName => value }
     */
    abstract public HashMap<String, String> getProperties();

}
