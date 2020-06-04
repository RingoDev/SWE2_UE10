package blackjack.model;

import blackjack.model.player.Dealer;
import blackjack.model.player.HumanPlayer;


import javax.swing.event.EventListenerList;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Blackjack implements Game {

    public static final int TOTAL_NUM_CARDS = 52;
    private final List<Integer> deck;
    private final HumanPlayer human;
    private final Dealer dealer;

    private final EventListenerList eventListeners;

    public GameState state;

    public Blackjack() {
        this.state = GameState.NOTSTARTED;
        eventListeners = new EventListenerList();
        deck = new ArrayList<>();
        human = new HumanPlayer();
        dealer = new Dealer();
    }

    public void shuffle() {
        if (deck.size() == 0) for (int i = 0; i < TOTAL_NUM_CARDS; i++) deck.add(i);
    }

    /**
     * @return the human of the current game
     */
    public HumanPlayer getHuman() {
        return human;
    }

    /**
     * @return the dealer of the current game
     */
    public Dealer getDealer() {
        return dealer;
    }

    /**
     * evaluate which player won this round
     *
     * @return the result of this round
     */
    public GameResult evaluateCards() {
        if (human.isBusted()) return GameResult.DEALER_WINS;
        if (human.hasBlackJack() && dealer.hasBlackJack()) return GameResult.DRAW;
        if (dealer.isBusted()) return GameResult.PLAYER_WINS;
        if (human.hasBlackJack()) return GameResult.PLAYER_WINS;
        if (dealer.hasBlackJack()) return GameResult.DEALER_WINS;
        if (human.getValue() > dealer.getValue()) return GameResult.PLAYER_WINS;
        if (dealer.getValue() > human.getValue()) return GameResult.DEALER_WINS;
        return GameResult.DRAW;
    }

    /**
     * resets the the full game including the chips of the human
     */
    public void FullReset() {
        PlayerReset();
        state = GameState.NOTSTARTED;
        fireEvent();
    }

    /**
     * @return the {@link GameState} of the current game
     */
    public GameState getGameState() {
        return this.state;
    }

    /**
     * resets the human and dealer hands and wagers
     */
    @Override
    public void PlayerReset() {
        human.reset();
        dealer.reset();
    }

    @Override
    public void addGameListener(Listener l) {
        this.eventListeners.add(Listener.class, l);

    }

    /**
     * draw a random card that is still in the deck
     * shuffles the Cards if the deck is empty
     *
     * @return the selected card
     */
    public Card drawCard() {
        if (deck.size() == 0) shuffle();
        return new Card(deck.remove(new Random().nextInt(deck.size())));
    }


    /**
     * draws cards for the dealer until he chooses to stay
     * sets GameState to finish because this is always the last action of a game
     */
    void dealerTurns() {
        while (true) {
            if (dealer.makeTurn() == Turn.STAY) {
                break;
            } else {
                dealer.addCard(drawCard());
            }
        }
        this.state = GameState.FINISHED;
    }

    /**
     * sets the wager for this round
     */
    public void setWager() {
        int wager = 1; // can be modified to take different wagers as input
        human.setChips(human.getChips() - wager);
        human.setWager(wager);
    }

    /**
     * sets the chips for this game
     */
    public void setStartingChips(int chips) {
        human.setChips(chips);
    }

    /**
     * distributes the winnings according to game result.
     */
    public void distributeWinnings() {
        human.setLastWager(human.getWager());

        int winnings;

        if (evaluateCards() == GameResult.PLAYER_WINS) {
            if (human.hasBlackJack()) winnings = human.getWager() * 3;
            else winnings = human.getWager() * 2;
        } else if (evaluateCards() == GameResult.DEALER_WINS) {
            winnings = 0;
        } else winnings = human.getWager();
        human.setChips(human.getChips() + winnings);
    }

    /**
     * starts a new game
     */
    public void startGame(int chips) {

        this.state = GameState.RUNNING;

        setStartingChips(chips);

        shuffle();

        dealStarterCards();

        fireEvent();

    }

    /**
     * deals the first 2 human cards and 1 dealer card and changes GameState to finished if human made a Blackjack
     *
     * @throws OutOfMoneyException if the player has < 1 chips
     */
    public void dealStarterCards() throws OutOfMoneyException {

        if (human.getChips() < 1) throw new OutOfMoneyException();

        this.state = GameState.RUNNING;

        PlayerReset();
        setWager();

        dealer.addCard(drawCard());
        human.addCard(drawCard());
        human.addCard(drawCard());

        // starts dealerTurns and sets GameState to finished
        if (human.hasBlackJack()) {
            dealerTurns();
            distributeWinnings();
        }

        fireEvent();

    }

    /**
     * updates all Listeners in eventListeners with a new {@link Event}
     */
    private void fireEvent() {
        Event evt = new Event(this, state);
        for (Listener l : eventListeners.getListeners(Listener.class)) {
            l.update(evt);
        }
    }

    /**
     * handles a player turn
     *
     * @param turn the {@link Turn} of the player
     * @throws OutOfMoneyException if the player doesn't have enough chips to do a DoubleDown
     */
    @Override
    public void humanTurns(Turn turn) throws OutOfMoneyException {

        switch (turn) {
            case STAY:
                dealerTurns();
                break;
            case HIT:
                human.addCard(drawCard());
                if (human.isBusted()) this.state = GameState.FINISHED;
                if (human.hasBlackJack()) dealerTurns();
                break;
            case DOUBLE_DOWN:
                if (human.getChips() < human.getWager()) {
                    throw new OutOfMoneyException("You dont have enough money to double down!");
                } else {
                    human.addCard(drawCard());
                    human.setChips(human.getChips() - human.getWager());
                    human.setWager(human.getWager() * 2);
                    dealerTurns();
                }
                break;
        }
        if (this.state == GameState.FINISHED) {
            distributeWinnings();
        }
        fireEvent();
    }
}
