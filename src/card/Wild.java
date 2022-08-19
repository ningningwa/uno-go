package card;

import collections.AIPlayerCards;
import play.CurrentStatus;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

/**
 * Wild Family
 */
public class Wild extends Card implements DS {
    protected String color;
    protected final int number;
    protected final String function;

    public Wild(String color, int number, String function) {
        this.color = color;
        this.number = number;
        this.function = function;
    }

    public String getName() {
        String name = this.function;
        return name;
    }

    public Boolean isValid() {
        return CurrentStatus.penalty == 0;
    }

    public String getFunction() {
        return this.function;
    }

    public String getColor() {
        return this.color;
    }

    public int getNumber() {
        return this.number;
    }

    public void makeDirectionChoice() {
        System.out.println("Do you want to change playing direction?");
        String directionChoice = CurrentStatus.input;
        while (!CurrentStatus.inputDone) {
            directionChoice = CurrentStatus.input;
            directionChoice = directionChoice.toLowerCase();
        }
        if (directionChoice.equals("y") || directionChoice.equals("yes")) {
            CurrentStatus.direction = -CurrentStatus.direction;
        }
        CurrentStatus.inputDone = false;
    }
    /*
     * When wild is put out, you need to choose the color
     */
    protected String makeColorChoice() {
        System.out.println("Please make your color choice: ");
        String colorChoice = CurrentStatus.input;
        while (!CurrentStatus.inputDone) {
            colorChoice = CurrentStatus.input;
            colorChoice = colorChoice.toLowerCase();
        }
        CurrentStatus.inputDone = false;
        if (!Arrays.asList(COLOR_LIST).contains(colorChoice)) {
            System.out.println("It's not a valid color choice");
            return makeColorChoice();
        }
        return colorChoice;
    }

    public void makeNumberOrColorChoice() {
        System.out.println("Please make your choice (change number or color): ");
        String choice = CurrentStatus.input;
        while (!CurrentStatus.inputDone) {
            choice = CurrentStatus.input;
            choice = choice.toLowerCase();
        }
        CurrentStatus.inputDone = false;
        int number;
        try {
            number = Integer.parseInt(choice);
            if (number >= 0 && number <=9 ) {
                CurrentStatus.color = "wild";
                CurrentStatus.number = number;
            }
        } catch (Exception e) {
            color = makeColorChoice();
            CurrentStatus.color = color;
        }
    }

    protected void makeChoice() {
        if (CurrentStatus.playerCards instanceof AIPlayerCards) {
            Random r = new Random();
            int random = r.nextInt(100-1) + 1;
            if (random <= 50) {
                CurrentStatus.direction = - CurrentStatus.direction;
                System.out.println(CurrentStatus.playerName+" chose to reverse the current direction.");
            } else {
                System.out.println(CurrentStatus.playerName+" chose to keep the current direction.");
            }
            random = r.nextInt(100-1) + 1;
            if (random <= 20) {
                System.out.println(CurrentStatus.playerName+" chose to change number.");
                CurrentStatus.direction = - CurrentStatus.direction;
                random = r.nextInt(9-0) + 0;
                CurrentStatus.number = random;
            } else {
                System.out.println(CurrentStatus.playerName+" chose to change color.");
                ((AIPlayerCards) CurrentStatus.playerCards).chooseColorSmartly();
            }
            return;
        }
        makeDirectionChoice();
        makeNumberOrColorChoice();
    }

    public void activateFunction() {
        makeChoice();
    }
}
