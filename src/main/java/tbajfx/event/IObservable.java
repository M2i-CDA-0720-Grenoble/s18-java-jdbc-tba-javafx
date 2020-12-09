package tbajfx.event;

import tbajfx.event.Signal;

public interface IObservable {
    
    /**
     * Binds an observer to this observable
     * @param observer The observer to attach
     */
    public void attach(IObserver observer);

    /**
     * Removes the bound between an observer and this observable
     * @param observer The observer to detach
     */
    public void detach(IObserver observer);

    /**
     * Send a signal to all observers
     * @param signal The signal to be sent to all observers
     */
    public void signal(Signal signal);

}
