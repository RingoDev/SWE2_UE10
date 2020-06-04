package blackjack.ui;

import blackjack.model.Card;
import blackjack.model.Game;
import blackjack.model.player.Player;

import javax.swing.*;
import java.awt.*;
import java.util.List;


public class CardPanel extends JComponent {

    Game model;
    Player player;
    Graphics2D g2D;

    CardPanel(Game model, Player player) {
        super();
        this.model = model;
        this.player = player;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g2D = (Graphics2D) g;
        drawHand();
    }

    /**
     * draws the hand of the respective player
     */
    public void drawHand() {

        List<Card> cards = player.getCards();
        int h = getHeight();
        int cardHeight = (int) (h / 1.5);
        int cardWidth = (int) (cardHeight * 0.7);
        for (int i = 0; i < cards.size(); i++) {
            drawCard(i, cardWidth, cardHeight, cards);
        }
    }

    /**
     * draws a card
     *
     * @param i          the index of the card in the hand
     * @param cardWidth  the width of the card
     * @param cardHeight the height of the card
     * @param cards      the cards of the player
     */
    public void drawCard(int i, int cardWidth, int cardHeight, List<Card> cards) {
        g2D.setColor(Color.white);
        g2D.fillRect(i * cardWidth/2, 10, cardWidth, cardHeight);
        g2D.setColor(cards.get(i).getColor() == Card.Color.Clubs || cards.get(i).getColor() == Card.Color.Spades ? Color.BLACK : Color.RED);
        g2D.setFont(new Font("TimesRoman", Font.PLAIN, cardHeight / 10));
        System.out.println(cardWidth);
        g2D.drawString(cards.get(i).toString(), i * cardWidth/2 + cardWidth/10, 10 + cardHeight / 6);

        paintBorder(g2D, i * cardWidth/2, 10, cardWidth, cardHeight);
    }

    /**
     * draws a black border around the card
     *
     * @param g      the 2D Graphics
     * @param x      x-Coordinate of the card
     * @param y      y-Coordinate of the card
     * @param width  the width of the card
     * @param height the height of the card
     */
    public void paintBorder(Graphics g, int x, int y,
                            int width, int height) {
        g.setColor(Color.black);
        g.fillRect(x, y, width, 2);
        g.fillRect(x, y, 2, height);
        g.fillRect(x + width - 2, y, 2, height);
        g.fillRect(x, y + height - 2, width, 2);
    }
}