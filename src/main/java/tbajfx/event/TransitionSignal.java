package tbajfx.event;

import tbajfx.db.entity.Direction;

public class TransitionSignal extends Signal {
    
    private Direction direction;

    public TransitionSignal(Direction direction)
    {
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    public String toString()
    {
        return this.getClass().getSimpleName() + " { direction: '" + direction.getName() + "' }";
    }

}
