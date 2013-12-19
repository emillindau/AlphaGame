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

    public boolean delete(ChangeListener toDelete) {
        return listeners.remove(toDelete);
    }


    public void change(char newChar) {
        for (ChangeListener listener : listeners) {
            listener.change(newChar);
        }
    }

    public void gameOver() {
        for (ChangeListener listener : listeners) {
            listener.gameOver();
        }
    }
}
