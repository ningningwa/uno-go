package play;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;

import javax.swing.*;
import java.awt.*;

//Below is referenced from the template in CS 242 materials.

public class GUI extends JFrame {
    public static JTextArea textArea;
    private JTextField textField;
    private JButton button;
    private Boolean humans = false;
    private Boolean AI = false;
    private String humanPlayers = "";
    public GUI() {
        setTitle("UNO");
        setLayout(new FlowLayout());
        initializeBar();
        initializeTextArea();
        setSize(10000, 10000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        System.out.println("Players you want to initialize (Please type in names with space separated)");
    }
    private void initializeTextArea() {
        textArea = new JTextArea(50, 100);
        textArea.setEditable(false);
        Font font = textArea.getFont();
        float size = font.getSize() + 10.0f;
        textArea.setFont( font.deriveFont(size) );
        add(textArea);
        PrintStream printStream = new PrintStream(new CustomOutputStream(textArea));
        System.setOut(printStream);
        System.setErr(printStream);
    }
    private void initializeBar() {
        textField = new JTextField(20);
        Font font = textField.getFont();
        textField.setFont(font.deriveFont(font.getSize()+20.0f));
        button = new JButton("action");
        font = button.getFont();
        button.setFont(font.deriveFont(font.getSize()+20.0f));
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if (!humans) {
                    while (textField.getText() == null || textField.getText().equals("")) {
                        System.out.println("Empty names");
                        return;
                    }
                    humanPlayers = textField.getText();
                    textField.setText("");
                    humans = true;
                    System.out.println("How many AIs do you want to initialize?");
                    return;
                }
                if (!AI) {
                    Boolean inputStatus = false;
                    int AInumber=0;
                    String[] humanPlayersList = humanPlayers.split("\\s+");
                    while (!inputStatus) {
                        if (textField.getText() == null || textField.getText().equals("")){
                            return;
                        }
                        try {
                            int number = Integer.parseInt(textField.getText());
                            if (number + humanPlayersList.length > 10 || number + humanPlayersList.length < 2 || number < 0) {
                                System.out.println("The total number of players is not correct");
                                textField.setText("");
                                return;
                            } else {
                                inputStatus = true;
                                AInumber = number;
                                break;
                            }
                        } catch (Exception e) {
                            System.out.println("Input is not a number");
                            textField.setText("");
                            return;
                        }
                    }
                    String[] AIPlayersList = new String[AInumber];
                    for (int i = 1; i <= AInumber; i++) {
                        AIPlayersList[i-1] = "AI No. " + i;
                    }
                    start(humanPlayersList, AIPlayersList);
                    AI = true;
                    textField.setText("");
                    return;
                }
                if (textField.getText() == null || !textField.getText().equals("") && !CurrentStatus.inputDone) {
                    CurrentStatus.input = textField.getText();
                    textField.setText("");
                    CurrentStatus.inputDone = true;
                }
            }
        });
        add(textField);
        add(button);
    }
    public void start(String[] players, String[] AIs) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Game game = new Game();
                game.start(players, AIs);
            }
        });
        thread.start();
    }
    public static void main(String[] args) {
        new GUI();
    }
}
