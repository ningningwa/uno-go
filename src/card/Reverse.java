package card;

import play.CurrentStatus;

/**
 * Reverse
 */
public class Reverse extends Card {

    private final String color;
    private final int number;
    private final String function;

    public Reverse(String color, int number, String function) {
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

    public Boolean isValid() {
        if (CurrentStatus.penalty != 0) {
            return false;
        }
        return this.color.equals(CurrentStatus.color) || this.function.equals(CurrentStatus.function);
    }

    public String getName() {
        String name = this.color + "Reverse";
        return name;
    }

    public void activateFunction() {
        CurrentStatus.direction *= -1;
    }
}
