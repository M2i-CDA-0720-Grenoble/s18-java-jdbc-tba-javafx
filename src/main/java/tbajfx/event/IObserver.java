package tbajfx.event;

import tbajfx.event.Signal;

public interface IObserver {
    
    /**
     * Handles a signal sent by the observable
     * @param signal The signal to be handled
     */
    public void handleSignal(Signal signal);

}
