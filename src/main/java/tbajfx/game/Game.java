package tbajfx.game;

import java.util.ArrayList;
import java.util.List;

import tbajfx.db.entity.Direction;
import tbajfx.db.entity.Room;
import tbajfx.db.entity.RoomTransition;
import tbajfx.db.repository.RoomTransitionRepository;
import tbajfx.event.IObservable;
import tbajfx.event.IObserver;
import tbajfx.event.RoomChangeSignal;
import tbajfx.event.Signal;
import tbajfx.event.TransitionSignal;

public final class Game implements IObservable, IObserver {
    
    private List<IObserver> observers;
    private GameState state;

    public Game()
    {
        observers = new ArrayList<>();
        state = new GameState(1);
    }

    public void init()
    {
        signal( new RoomChangeSignal(getState().getCurrentRoom()) );
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
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
        if (signal instanceof TransitionSignal) {
            TransitionSignal typedSignal = (TransitionSignal)signal;
            Direction direction = typedSignal.getDirection();

            RoomTransitionRepository repository = new RoomTransitionRepository();
            RoomTransition transition = repository.findByFromRoomAndDirection( getState().getCurrentRoom(), direction);
            if (transition != null) {
                Room newRoom = transition.getToRoom();
                getState().setCurrentRoom( newRoom );
                signal( new RoomChangeSignal(newRoom) );
            }
        }
    }

}
