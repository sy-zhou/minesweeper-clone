import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import static javax.swing.GroupLayout.Alignment.*;
import java.util.*;
/**
 * Main is the class to begin the minesweeper game.
 * 
 * @author Sally Zhou
 * @version 1.0 2018-03-16
 */
public class Main
{
    // constants
    private static final int BEGINNER = 0;
    private static final int INTERMEDIATE = 1;
    private static final int ADVANCED = 2;

    // GUI components
    private static JFrame menuFrame;
    private static JRadioButton beginnerButton;
    private static JRadioButton intermediateButton;
    private static JRadioButton advancedButton;
    private static JButton startButton;

    /**
     * Begins the minesweeper game.
     *
     * @param argument not used
     */
    public static void main(String[] argument)
    {
        initComponents();
    } // end of main(String[] argument)

    /*
     * Initializes all GUI components and formats them.
     */
    private static void initComponents()
    {
        // create menu, ask user the difficulty of board they want (radio buttons)
        menuFrame = new JFrame("Minesweeper Menu");
        JPanel contentPane = (JPanel) menuFrame.getContentPane();
        contentPane.setBorder(new EmptyBorder(12, 12, 12, 12));
        GroupLayout layout = new GroupLayout(contentPane);
        contentPane.setLayout(layout);
        
        JLabel welcomeMessageA = new JLabel("-- Welcome to Sally's (very crude) Minesweeper! --");
        JLabel welcomeMessageB = new JLabel("Please select a difficulty and then click 'Start'.");
        
        // setting up difficulty buttons
        beginnerButton = new JRadioButton("Beginner");
        intermediateButton = new JRadioButton("Intermediate");
        advancedButton = new JRadioButton("Advanced");

        ButtonGroup difficultyButtons = new ButtonGroup();
        difficultyButtons.add(beginnerButton);
        difficultyButtons.add(intermediateButton);
        difficultyButtons.add(advancedButton);
        
        JPanel difficultyButtonsPanel = new JPanel();
        difficultyButtonsPanel.setBorder(new EmptyBorder(4, 4, 4, 4));
        difficultyButtonsPanel.setLayout(new BoxLayout(difficultyButtonsPanel, BoxLayout.PAGE_AXIS));
        difficultyButtonsPanel.add(beginnerButton);
        difficultyButtonsPanel.add(intermediateButton);
        difficultyButtonsPanel.add(advancedButton);
        
        startButton = new JButton("START");
        startButton.addActionListener(new ActionListener() 
            {
                @Override
                public void actionPerformed(ActionEvent e) {startGame();}
            });
        
        // formatting using GroupLayout
        layout.setHorizontalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(CENTER)
                .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(LEADING)
                        .addComponent(welcomeMessageA)
                        .addComponent(welcomeMessageB)))
                .addComponent(difficultyButtonsPanel)
                .addComponent(startButton))
        );
        
        layout.setVerticalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(BASELINE)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(welcomeMessageA)
                    .addComponent(welcomeMessageB)
                    .addComponent(difficultyButtonsPanel)
                    .addComponent(startButton)))
        );
        
        beginnerButton.setSelected(true);
        
        menuFrame.pack();
        menuFrame.setVisible(true);
    }
    
    private static void startGame()
    {
        menuFrame.setVisible(false);
        if (beginnerButton.isSelected())
        {
            GUI ms = new GUI(BEGINNER);
        }
        else if (intermediateButton.isSelected())
        {
            GUI ms = new GUI(INTERMEDIATE);
        }
        else if (advancedButton.isSelected())
        {
            GUI ms = new GUI(ADVANCED);
        }
    }
} // end of class Main