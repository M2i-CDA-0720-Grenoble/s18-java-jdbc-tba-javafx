package tbajfx.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tbajfx.db.entity.Entity;

public final class EntityStorage {

    Map<Integer, Entity> objectsById;
    Map<String, List<Entity>> queryResults;

    public EntityStorage()
    {
        objectsById = new HashMap<>();
        queryResults = new HashMap<>();
    }

    public Entity getById(int id)
    {
        return objectsById.get(id);
    }

    public EntityStorage put(Entity object)
    {
        objectsById.put(object.getId(), object);

        return this;
    }

    public List<Entity> getQueryResults(String query)
    {
        return queryResults.get(query);
    }

    public EntityStorage storeQueryResults(String query, ArrayList<Entity> results)
    {
        queryResults.put(query, results);

        return this;
    }

    public void resetQueryResults()
    {
        queryResults = new HashMap<>();
    }

}
