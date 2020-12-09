package tbajfx.ui;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import tbajfx.db.entity.Direction;
import tbajfx.db.entity.Room;
import tbajfx.db.repository.DirectionRepository;
import tbajfx.event.IObservable;
import tbajfx.event.IObserver;
import tbajfx.event.RoomChangeSignal;
import tbajfx.event.Signal;
import tbajfx.event.TransitionSignal;

public final class GameUI implements IObservable, IObserver {

    private List<IObserver> observers;
    private Label roomName;
    private Label roomDescription;
    private Button[] directionButtons;

    public GameUI()
    {
        observers = new ArrayList<>();
    }

    public VBox render()
    {
        roomName = new Label("<room name>");
        HBox roomNameBlock = new HBox(
            new Label("Current room: "),
            roomName
        );
        roomDescription = new Label("<room description>");
        roomDescription.setWrapText(true);

        DirectionRepository repository = new DirectionRepository();
        List<Direction> directions = repository.findAll();

        directionButtons = new Button[directions.size()];
        for (int i = 0; i < directionButtons.length; i++) {
            Direction direction = directions.get(i);
            Button button = new Button( direction.getName() );
            button.setOnAction(e -> signal( new TransitionSignal(direction) ) );
            directionButtons[i] = button;
        }
        VBox directionContainer = new VBox(directionButtons);

        VBox viewport = new VBox(roomNameBlock, roomDescription, directionContainer);
        viewport.setSpacing(20);
        viewport.setStyle("-fx-font-size: 18px;");
        viewport.setPadding(new Insets(16, 16, 16, 16));
        return viewport;
    }

    public void attach(IObserver observer)
    {
        observers.add(observer);
    }

    public void detach(IObserver observer)
    {
        observers.remove(observer);
    }

    public void signal(Signal signal)
    {
        for (IObserver observer: observers) {
            observer.handleSignal(signal);
        }
    }

    public void handleSignal(Signal signal)
    {
        if (signal instanceof RoomChangeSignal) {
            RoomChangeSignal typedSignal = (RoomChangeSignal)signal;
            Room room = typedSignal.getRoom();
            roomName.setText( room.getName() );
            roomDescription.setText( room.getDescription() );
        }
    }

}
