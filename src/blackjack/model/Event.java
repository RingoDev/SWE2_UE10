package blackjack.model;

import java.util.EventObject;

@SuppressWarnings("serial")
public class Event extends EventObject {

    private final GameState state;

    public Event(Object source, GameState state) {
        super(source);
        this.state = state;
    }

    public Game getModel() {
        return (Game) super.source;
    }

    public GameState getState(){
        return state;
    }

}


