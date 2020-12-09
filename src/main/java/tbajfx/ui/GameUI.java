package tbajfx.ui;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import tbajfx.db.entity.Direction;
import tbajfx.db.entity.Room;
import tbajfx.db.entity.RoomTransition;
import tbajfx.db.repository.DirectionRepository;
import tbajfx.event.IObservable;
import tbajfx.event.IObserver;
import tbajfx.event.RoomChangeSignal;
import tbajfx.event.Signal;
import tbajfx.event.TransitionSignal;

public final class GameUI implements IObservable, IObserver {

    private List<Direction> directions;
    private List<IObserver> observers;
    private Label roomName;
    private Label roomDescription;
    private Button[] directionButtons;
    private VBox availableDirectionsContainer;

    public GameUI()
    {
        DirectionRepository repository = new DirectionRepository();
        directions = repository.findAll();
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

        availableDirectionsContainer = new VBox();

        directionButtons = new Button[directions.size()];
        for (int i = 0; i < directionButtons.length; i++) {
            Direction direction = directions.get(i);
            Button button = new Button( direction.getName() );
            button.setOnAction(e -> signal( new TransitionSignal(direction) ) );
            directionButtons[i] = button;
        }
        HBox directionContainer = new HBox(directionButtons);

        VBox viewport = new VBox(roomNameBlock, roomDescription, availableDirectionsContainer, directionContainer, dataButtonsContainer);
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
            ObservableList<Node> availableDirections = availableDirectionsContainer.getChildren();
            availableDirections.removeAll(availableDirections);

            for (int i = 0; i < directionButtons.length; i++) {
                Button button = directionButtons[i];
                button.setDisable(true);
            }

            for (RoomTransition transition: room.getTransitions()) {
                int index = directions.indexOf( transition.getDirection() );
                directionButtons[index].setDisable(false);
                availableDirections.add(
                    new Label(transition.getDirection().getName() + " is the " + transition.getToRoom().getName() + ".")
                );
            }
        }
    }

}
