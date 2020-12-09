package tbajfx.ui;

import tbajfx.event.IObserver;
import tbajfx.event.Signal;

public final class Console implements IObserver {
    
    public void log(String message)
    {
        System.out.println(message);
    }

    public void handleSignal(Signal signal)
    {
        log("Fired signal: " + signal.toString());
    }

}
