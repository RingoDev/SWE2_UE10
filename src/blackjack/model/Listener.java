package blackjack.model;

import java.util.EventListener;

public interface Listener extends EventListener {

    /**
     * Signals a change in the state of the game.
     */
    void update(Event e);
}
