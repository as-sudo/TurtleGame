package p02.pres.events;
import java.util.EventListener;


   public interface TickListener extends EventListener {
    void onTick(TickEvent event);
    
        
    }