package tbajfx.game;

public final class Game {
    
    private GameState state;

    public Game()
    {
        state = new GameState(1);
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

}
