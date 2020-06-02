package blackjack.model.player;

import blackjack.model.Turn;

public class Dealer extends Player {

    public Dealer() {
        super();
    }

    @Override
    public Turn makeTurn() {
        return getValue() > 16 ? Turn.STAY : Turn.HIT;
    }
}
