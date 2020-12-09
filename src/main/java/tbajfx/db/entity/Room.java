package tbajfx.db.entity;

import java.util.HashMap;
import java.util.List;

import tbajfx.db.repository.ItemRepository;
import tbajfx.db.repository.RoomTransitionRepository;


public class Room extends Entity
{

    private String name;
    private String description;

    public Room()
    {
        id = 0;
        name = "";
        description = "";
    }

    public Room(int id, String name, String description)
    {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public HashMap<String, String> getProperties()
    {
        return new HashMap<String, String>() {{
            put("name", name);
            put("description", description);
        }};
    }

    @Override
    public String toString()
    {
        return "[Room #" + id + "] { name: " + name + ", description: " + description +" }";
    }

    public int getId() {
        return id;
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

    public List<RoomTransition> getTransitions()
    {
        RoomTransitionRepository transitionRepository = new RoomTransitionRepository();
        return transitionRepository.findAllFromRoom(this);
    }

    public List<Item> getItems()
    {
        ItemRepository itemRepository = new ItemRepository();
        return itemRepository.findByCriteria(new HashMap<String, String>() {{
            put("room_id", Integer.toString(id) );
        }});
    }

}
