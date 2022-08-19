package card;

import play.CurrentStatus;

import java.util.LinkedList;

/**
 * WildDrawFour whose parent is Wild
 */
public class WildDrawFour extends Wild {
    public WildDrawFour(String color, int number, String function) {
        super(color, number, function);
    }

    public Boolean isValid() {
        /* When previous player's penalty card is wildDrawFour */
        if (CurrentStatus.function.equals("wildDrawFour") && CurrentStatus.penalty != 0) {
            return true;
        }

        /* The following is to make sure wildDrawFour is the only card that can be put out.*/
        LinkedList<Card> playerCardsCollections = CurrentStatus.playerCards.getCards();
        for (int i = 0; i < playerCardsCollections.size(); i++) {
            Card toCheck = playerCardsCollections.get(i);
            if (toCheck != this) {
                /*
                 * This is to avoid stackoverflow happening
                 * when "Two wildDrawFour in your hand but current Status is DrawTwo"
                 */
                if (toCheck.getFunction().equals("wildDrawFour")) {
                    return false;
                }
                if (toCheck.isValid() == true) {
                    return false;
                }
            }
        }
        return true;
    }

    public void activateFunction() {
        makeChoice();
        CurrentStatus.penalty += 4;
    }
}
