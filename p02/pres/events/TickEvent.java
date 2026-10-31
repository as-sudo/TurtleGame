package p02.pres.events;

import java.util.EventObject;

public class TickEvent extends EventObject {
    public TickEvent(Object source) {
        super(source);
    }
}