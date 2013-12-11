package event;

import java.util.ArrayList;

/**
 * Created by Emil on 2013-12-12.
 */
public class ChangeObserver {
    ArrayList<ChangeListener> listeners = new ArrayList<>();

    public void add(ChangeListener toAdd) {
        listeners.add(toAdd);
    }

    public void change() {
        for (ChangeListener listener : listeners) {
            listener.change();
        }
    }
}
