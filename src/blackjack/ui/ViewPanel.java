package blackjack.ui;

import blackjack.model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ViewPanel extends JPanel {

    /**
     * The reference to the model.
     */
    private final Game model;

    private Controller mouseListener;

    private JPanel buttonPanel;
    private JComponent dealerCardPanel;
    private JComponent humanCardPanel;
    private JPanel playPanel;
    private JLabel dealerScore;
    private JLabel humanScore;

    private JButton doubleDown;
    private JButton hit;
    private JButton stay;
    private JButton play;

    /**
     * Constructor initializing the model and setting up this view.
     *
     * @param model the model of the blackjack game.
     */
    public ViewPanel(Game model) {

        super();
        this.model = model;
        this.setPreferredSize(new Dimension(600, 600));

        this.model.addGameListener(e -> {
            repaint();
            updateScore();
            if (e.getState() == GameState.NOTSTARTED) {

                //exchanges playButton visibility with TurnButtons visibility
                playPanel.setVisible(true);
                buttonPanel.setVisible(false);
            }

            if (e.getState() == GameState.RUNNING) {

                // removes the mouseListener from the view
                removeMouseListener(mouseListener);

                //exchanges playButton visibility with TurnButtons visibility
                playPanel.setVisible(false);
                buttonPanel.setVisible(true);

                // makes sure the DoubleDown button is visible
                doubleDown.setVisible(true);
            }

            if (e.getState() == GameState.FINISHED) {

                // adds the mouseListener to the view
                addMouseListener(mouseListener);

                // makes hit stay and doubleDown buttons invisible
                buttonPanel.setVisible(false);
            }
        });

        // setting view layout
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(new EmptyBorder(20, 20, 20, 20));

        mouseListener = new Controller();

        dealerScore = new JLabel();

        dealerCardPanel = new CardPanel(this.model, this.model.getDealer());

        humanScore = new JLabel();

        humanCardPanel = new CardPanel(this.model, this.model.getHuman());

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
        buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Hit Stay and DoubleDown buttons not visible at start
        buttonPanel.setVisible(false);


        hit = new JButton("Hit");
        stay = new JButton("Stay");
        doubleDown = new JButton("Doubledown");
        buttonPanel.add(hit);
        buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        buttonPanel.add(stay);
        buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        buttonPanel.add(doubleDown);

        playPanel = new JPanel();
        playPanel.setLayout(new BoxLayout(playPanel, BoxLayout.LINE_AXIS));


        play = new JButton("Play");
        playPanel.add(play);


        //adding all the Buttons and Components to the view
        this.add(dealerScore);
        this.add(dealerCardPanel);
        this.add(humanScore);
        this.add(humanCardPanel);
        this.add(buttonPanel);
        this.add(playPanel);


        // adding button listeners
        play.addActionListener(e -> model.startGame());

        hit.addActionListener(e -> {
            model.humanTurns(Turn.HIT);
            doubleDown.setVisible(false);
        });

        stay.addActionListener(e -> {
            model.humanTurns(Turn.STAY);
            doubleDown.setVisible(false);
        });

        doubleDown.addActionListener((e -> {
            try {
                model.humanTurns(Turn.DOUBLE_DOWN);
            } catch (Exception exc) {
                JOptionPane.showMessageDialog(ViewPanel.this, exc.getMessage(), "Invalid", JOptionPane.ERROR_MESSAGE);
            }
        }));
    }

    /**
     * Implementation of the mouse controller.
     * Will select a field and set a stone on a mouse click event.
     */
    class Controller extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            try {
                model.dealStarterCards();
            } catch (OutOfMoneyException e1) {
                JOptionPane.showMessageDialog(ViewPanel.this, e1.getMessage(), "Invalid", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void updateScore() {
        dealerScore.setText("Dealer (" + model.getDealer().getValue() + ")");
        humanScore.setText("Player (" + model.getHuman().getValue() + ")");
        dealerScore.setVisible(model.getGameState() != GameState.NOTSTARTED);
        humanScore.setVisible(model.getGameState() != GameState.NOTSTARTED);
    }
}
