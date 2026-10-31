package p02.pres.events;

import java.util.EventListener;

public interface PlusEventListener extends EventListener {
    void onPlusOne(PlusEvent event); 
}