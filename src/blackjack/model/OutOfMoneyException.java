package blackjack.model;

public class OutOfMoneyException extends RuntimeException {

    public OutOfMoneyException() {
        super("You have no more money left!");
    }

    public OutOfMoneyException(String str) {
        super(str);
    }

}
