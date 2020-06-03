package blackjack.ui;

import blackjack.model.*;
import blackjack.model.Event;

import javax.swing.*;
import java.awt.*;

/**
 * Application class for the graphical Blackjack game.
 * Sets up the frame and starts the application.
 */
public class App {
    private final Game model;
    private final ViewPanel view;
    private final JLabel stateLbl;


    /**
     * Main method starting the application.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        new App(new Blackjack());
    }

    /**
     * Constructor initializing the model and setting up the application.
     *
     * @param model holds all the game data while the application is running.
     */
    public App(Game model) {
        this.model = model;
        Listener gameListener = e -> {
            System.out.println("updated");
            setLabelText(e);
        };

        model.addGameListener(gameListener);

        JFrame frame = new JFrame("Blackjack");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setJMenuBar(createMenu());

        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new BorderLayout(10, 10));

        this.view = new ViewPanel(model);
        contentPane.add(view, BorderLayout.CENTER);

        JPanel statePanel = new JPanel();
        statePanel.setLayout(new BorderLayout());
        statePanel.setBorder(BorderFactory.createTitledBorder("Message"));
        this.stateLbl = new JLabel(preGameMsg());
        statePanel.setLayout(new FlowLayout());
        statePanel.add(stateLbl,BorderLayout.CENTER);
        contentPane.add(statePanel, BorderLayout.SOUTH);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Resets the game
     */
    public void newGame() {
        model.FullReset();
    }

    /**
     * Creates the menu for this application
     *
     * @return the menu bar
     */
    private JMenuBar createMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        JMenuItem newItem = new JMenuItem("Reset", 'R');
        newItem.addActionListener(e -> newGame());
        JMenuItem exitItem = new JMenuItem("Exit", 'X');
        exitItem.addActionListener(e -> System.exit(0));
        menu.add(newItem);
        menu.add(exitItem);
        menuBar.add(menu);
        return menuBar;
    }


    /**
     * sets the correct Text of the bottom label
     */
    void setLabelText(Event e) {
        switch (e.getState()) {
            case NOTSTARTED -> stateLbl.setText(preGameMsg());
            case RUNNING -> stateLbl.setText(inGameMsg());
            case FINISHED -> stateLbl.setText(postGameMsg());
        }
    }

    /**
     * constructs the after game message with result of the last game
     *
     * @return the after game message
     */
    private String postGameMsg() {
        StringBuilder sb = new StringBuilder();
        switch (this.model.evaluateCards()) {
            case DEALER_WINS:
                sb.append("You just lost ").append(+this.model.getHuman().getLastWager()).append(this.model.getHuman().getLastWager() > 1 ? " Chips. " : " Chip. ");
                break;
            case PLAYER_WINS:
                if (this.model.getHuman().hasBlackJack())
                    sb.append("You made a Blackjack and won ").append(this.model.getHuman().getLastWager() * 2).append(this.model.getHuman().getLastWager() * 2 > 1 ? " Chips. " : " Chip. ");
                else
                    sb.append("You just won ").append(this.model.getHuman().getLastWager()).append(this.model.getHuman().getLastWager() > 1 ? " Chips. " : " Chip. ");
                break;
            case DRAW:
                sb.append("This one was a draw. ");
                break;
        }
        return sb.append("Click anywhere to try again!").toString();
    }

    /**
     * @return the in game message
     */
    private String inGameMsg() {
        return "Chips:" + model.getHuman().getChips() + "        Current Wager: " + model.getHuman().getWager();
    }

    /**
     * @return the pre game message
     */
    private String preGameMsg() {
        return "Press Play to start the Game";
    }
}
