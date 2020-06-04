package blackjack.ui;

import blackjack.model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ViewPanel extends JPanel {

    /**
     * The reference to the model.
     */
    private final Game model;

    private ViewPanel.MouseListener mouseListener;

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
    private JLabel chipsText;
    private JTextField startingChips;
    private JPanel dealerScoreWrapper;

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

                // makes all buttons invisible
                buttonPanel.setVisible(false);
            }
        });

        // setting view layout
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(new EmptyBorder(20, 20, 20, 20));


        this.mouseListener = new MouseListener();

        dealerScoreWrapper = new JPanel();
        dealerScoreWrapper.setLayout(new FlowLayout(FlowLayout.LEFT));
        dealerScore = new JLabel();
        dealerScoreWrapper.add(dealerScore);
        dealerScoreWrapper.setMaximumSize(new Dimension(2000, 20));

        dealerCardPanel = new CardPanel(this.model, this.model.getDealer());

        JPanel humanScoreWrapper = new JPanel();
        humanScoreWrapper.setLayout(new FlowLayout(FlowLayout.LEFT));
        humanScore = new JLabel();
        humanScoreWrapper.add(humanScore);
        humanScoreWrapper.setMaximumSize(new Dimension(10000, 20));

        humanCardPanel = new CardPanel(this.model, this.model.getHuman());


        //adding all the Buttons and Components to the view
        this.add(dealerScoreWrapper);
        this.add(dealerCardPanel);
        this.add(humanScoreWrapper);
        this.add(humanCardPanel);
        this.add(createButtonPanel());
        this.add(creatPlayPanel());

        // giving some color
        this.setBackground(new Color(53, 101, 77));
        playPanel.setBackground(new Color(53, 101, 77));
        startingChips.setBackground(new Color(53, 70, 77));
        startingChips.setForeground(new Color(255, 255, 255));
        dealerScoreWrapper.setBackground(new Color(53, 101, 77));
        humanScoreWrapper.setBackground(new Color(53, 101, 77));
        buttonPanel.setBackground(new Color(53, 101, 77));

    }

    /**
     * creates the Hit, Stay and DoubleDown Buttons with ActionListeners
     *
     * @return a {@link JPanel} with a {@link JLabel}, a {@link JTextField} and the Play Button
     */
    private JPanel creatPlayPanel() {

        playPanel = new JPanel();
        playPanel.setLayout(new BoxLayout(playPanel, BoxLayout.PAGE_AXIS));

        chipsText = new JLabel("How many chips do you want to play with?");
        chipsText.setAlignmentX(Component.CENTER_ALIGNMENT);

        startingChips = new JTextField("10", 15);
        startingChips.setAlignmentX(Component.CENTER_ALIGNMENT);
        startingChips.setMaximumSize(new Dimension(50, 25));
        startingChips.setHorizontalAlignment(JTextField.CENTER);
        startingChips.setBorder(new LineBorder(new Color(0, 0, 0)));

        play = new JButton("Play");
        play.setAlignmentX(Component.CENTER_ALIGNMENT);

        //adding to playpanel
        playPanel.add(chipsText);
        playPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        playPanel.add(startingChips);
        playPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        playPanel.add(play);

        // adding button listener
        play.addActionListener(e -> {
            try {
                int chips = Integer.parseInt(startingChips.getText());
                if (chips > 0) model.startGame(chips);
                else
                    JOptionPane.showMessageDialog(ViewPanel.this, "You need more chips to sit down at this table", "Invalid", JOptionPane.ERROR_MESSAGE);

            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(ViewPanel.this, "Invalid amount of chips!", "Invalid", JOptionPane.ERROR_MESSAGE);
            }
        });

        return playPanel;
    }

    /**
     * creates the Hit, Stay and DoubleDown Buttons with ActionListeners
     *
     * @return a {@link JPanel} with the 3 buttons
     */
    private JPanel createButtonPanel() {

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
        buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Hit Stay and DoubleDown buttons not visible at start
        buttonPanel.setVisible(false);

        hit = new JButton("Hit");
        stay = new JButton("Stay");
        doubleDown = new JButton("Doubledown");

        //adding to buttonPanel
        buttonPanel.add(hit);
        buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        buttonPanel.add(stay);
        buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        buttonPanel.add(doubleDown);

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

        return buttonPanel;
    }

    /**
     * Implementation of the mouse controller.
     * Will fire if the mouse is clicked.
     */
    class MouseListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            try {
                model.dealStarterCards();
            } catch (OutOfMoneyException e1) {
                JOptionPane.showMessageDialog(ViewPanel.this, e1.getMessage(), "Invalid", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * updates the Scores and makes them invisible if the {@link GameState} is NOTSTARTED or visible if the game is RUNNING or FINISHED
     */
    public void updateScore() {
        dealerScore.setText("Dealer (" + model.getDealer().getValue() + ")");
        humanScore.setText("Player (" + model.getHuman().getValue() + ")");
        dealerScore.setVisible(model.getGameState() != GameState.NOTSTARTED);
        humanScore.setVisible(model.getGameState() != GameState.NOTSTARTED);
    }
}
