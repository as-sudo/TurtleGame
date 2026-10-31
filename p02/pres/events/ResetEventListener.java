package p02.pres.events;

import java.util.EventListener;

public interface ResetEventListener extends EventListener {
    void onReset(ResetEvent event);  
}