package tbajfx.game;

import java.io.*;
import java.time.LocalDateTime;

import tbajfx.db.entity.Room;
import tbajfx.db.repository.RoomRepository;

public class GameState implements Serializable {

    public static final long serialVersionUID = 1L;
    public static final String folderName = "savegame";
    
    private String name;
    private LocalDateTime createdAt;

    private int currentRoomId;

    public GameState(int startRoomId)
    {
        currentRoomId = startRoomId;
    }

    public static GameState load(String filename)
    {
        try {
            FileInputStream file = new FileInputStream(folderName + "\\" + filename);
            ObjectInputStream objectInputStream = new ObjectInputStream(file);
            GameState state = (GameState)objectInputStream.readObject();
            objectInputStream.close();
            return state;
        }
        catch (FileNotFoundException exception) {
            System.out.println("Save file not found.");
            return null;
        }
        catch (IOException exception) {
            System.out.println("Error while reading save file.");
            exception.printStackTrace();
            return null;
        }
        catch (ClassNotFoundException exception) {
            System.out.println("GameState class deleted since save.");
            return null;
        }
    }

    public void save(String filename)
    {
        try {
            FileOutputStream file = new FileOutputStream(folderName + "\\" + filename);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(file);
            objectOutputStream.writeObject(this);
            objectOutputStream.flush();
            objectOutputStream.close();
        }
        catch (FileNotFoundException exception) {
            System.out.println("Save file not found.");
            return;
        }
        catch (IOException exception) {
            System.out.println("Error while writing save file.");
            return;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Room getCurrentRoom()
    {
        RoomRepository repository = new RoomRepository();
        return repository.findById(currentRoomId);
    }

    public GameState setCurrentRoom(Room room)
    {
        currentRoomId = room.getId();
        return this;
    }

}
