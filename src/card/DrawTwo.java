package card;

import play.CurrentStatus;

/**
 * Draw Two
 */
public class DrawTwo extends Card {
    private final String color;
    private final int number;
    private final String function;

    public DrawTwo(String color, int number, String function) {
        this.color = color;
        this.number = number;
        this.function = function;
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

    public String getName() {
        String name = this.color+"DrawTwo";
        return name;
    }

    public Boolean isValid() {
        /* We cannot use drawTwo for wildDrawFour */
        if (CurrentStatus.function.equals("wildDrawFour") && CurrentStatus.penalty != 0) {
            return false;
        }
        return this.color.equals(CurrentStatus.color) || this.function.equals(CurrentStatus.function);
    }

    public void activateFunction() {
        CurrentStatus.penalty += 2;
    }
}
