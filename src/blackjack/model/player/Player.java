package blackjack.model.player;

import blackjack.model.Card;
import blackjack.model.Turn;

import java.util.ArrayList;
import java.util.List;

public abstract class Player {

    List<Card> cards;
    int wager;
    int lastWager;

    public int getWager() {
        return wager;
    }

    public void setWager(int wager) {
        this.wager = wager;
    }

    Player() {
        cards = new ArrayList<>();
    }

    public boolean isBusted() {
        return getValue() > 21;
    }

    public void addCard(Card c) {
        cards.add(c);
    }


    public void reset() {
        cards = new ArrayList<>();
        wager = 0;
    }

    public List<Card> getCards() {
        return cards;
    }

    public int aceCount() {
        return (int) cards.stream().filter(c -> c.getIndex() == 1).count();
    }

    public int getStandardValue() {
        return cards.stream().mapToInt(c -> {
            if (c.getIndex() == 1) return 11;
            return Math.min(c.getIndex(), 10);
        }).sum();
    }

    public int getValue() {
        int value = getStandardValue();
/*
   Subtracts 10 from Value as often as the amount of aces.
   As long as Value is above 21.
*/
        for (int i = 0; i < aceCount(); i++) {
            if (value > 21) value -= 10;
            else return value;
        }
        return value;
    }

    public boolean hasBlackJack() {
        if (cards.size() == 2) {
            for (int i = 0; i < 2; i++) {
                if (cards.get(i).getIndex() == 1) {
                    if (cards.get((i + 1) % 2).getIndex() > 9) return true;
                }
            }
        }
        return false;
    }
    public void setLastWager(int lastWager){
        this.lastWager = lastWager;
    }
    public int getLastWager(){
        return lastWager;
    }

    public abstract Turn makeTurn();
}
