package blackjack.model;

import blackjack.model.player.Dealer;
import blackjack.model.player.HumanPlayer;

public interface Game {

    /**
     * evaluate which player won this round
     *
     * @return the result of this round
     */
    GameResult evaluateCards();

    /**
     * handles a player turn
     *
     * @param turn the {@link Turn} of the player
     * @throws OutOfMoneyException if the player doesn't have enough chips to do a DoubleDown
     */
    void humanTurns(Turn turn);

    /**
     * @return the {@link GameState} of the current game
     */
    GameState getGameState();

    /**
     * @return the human of the current game
     */
    HumanPlayer getHuman();

    /**
     * @return the dealer of the current game
     */
    Dealer getDealer();

    /**
     * resets the the whole game including the chips of the human
     */
    void FullReset();

    /**
     * resets the human and dealer hands and wagers
     */
    void PlayerReset();

    /**
     * Adds a game listener to this model.
     *
     * @param l the game listener to be added
     */
    void addGameListener(Listener l);

    /**
     * deals the first 2 human cards and 1 dealer card and changes GameState to finished if human made a Blackjack
     *
     * @throws OutOfMoneyException if the player has < 1 chips
     */
    void dealStarterCards() throws OutOfMoneyException;

    /**
     * starts a new game with a certain amount of chips
     */
    void startGame(int chips);

}
