package tbajfx.db.entity;

import java.util.HashMap;


public class Direction extends Entity {
    
    private String name;
    private String command;

    public Direction()
    {
        id = 0;
        name = "";
        command = "";
    }

    public Direction(int id, String name, String command)
    {
        this.id = id;
        this.name = name;
        this.command = command;
    }

    public HashMap<String, String> getProperties()
    {
        return new HashMap<String, String>() {{
            put("name", name);
            put("command", command);
        }};
    }

    @Override
    public String toString()
    {
        return "[Direction #" + id + "] { name: " + name + ", command: " + command + " }";
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

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

}
