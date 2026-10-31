package p02.pres.events;
import java.util.EventListener;
public interface StartListener extends EventListener {
    void onStart(StartEvent event); 
}