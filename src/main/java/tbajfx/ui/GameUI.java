package tbajfx.ui;

import java.util.List;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import tbajfx.db.entity.Direction;
import tbajfx.db.repository.DirectionRepository;

public final class GameUI {

    private Label roomName;
    private Label roomDescription;
    private Button[] directionButtons;

    public VBox render()
    {
        roomName = new Label("Current Room: <room name>");
        roomDescription = new Label("<room description>");

        DirectionRepository repository = new DirectionRepository();
        List<Direction> directions = repository.findAll();

        directionButtons = new Button[directions.size()];
        for (int i = 0; i < directionButtons.length; i++) {
            Direction direction = directions.get(i);
            directionButtons[i] = new Button( direction.getName() );
        }
        VBox directionContainer = new VBox(directionButtons);

        VBox viewport = new VBox(roomName, roomDescription, directionContainer);
        viewport.setSpacing(20);
        viewport.setStyle("-fx-font-size: 18px;");
        viewport.setPadding(new Insets(16, 16, 16, 16));
        return viewport;
    }

}
