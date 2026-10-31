package p02.pres.events;

import java.util.EventObject;

public class StartEvent extends EventObject {
    public StartEvent(Object source) {
        super(source);
    }
}