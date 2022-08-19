package collections;

import card.Card;
import card.DS;
import play.CurrentStatus;

import java.awt.*;
import java.util.*;

/**
 * This class is for AI players.
 */
public class AIPlayerCards extends PlayerCards implements DS {
    public AIPlayerCards() {
        super();
    }
    public AIPlayerCards(int nums) {
        super(nums);
    }

    /*
     * This function is for the AI's strategy to play cards.
     */
    public void play() {
        int count = 1;
        for (int i = 0; i < this.cards.size(); i++) {
            Card checkingCard = this.cards.get(i);
            if (checkingCard.isValid()) {
                int playerIndex = CurrentStatus.playerIndex;
                System.out.println(CurrentStatus.playerName+" chose to put out "+checkingCard.getName());
                this.cards.remove(count-1);
                CurrentStatus.updateCurrentStatus(checkingCard);
                CurrentStatus.checkOver(playerIndex);
                return;
            }
            count++;
        }
        if (CurrentStatus.penalty != 0) {
            CurrentStatus.playerCardsDraw(CurrentStatus.penalty);
            CurrentStatus.penalty = 0;
            CurrentStatus.nextPlayer();
            return;
        }
        Card drawnCard = drawCard();
        System.out.println("Your drawn card is " + drawnCard.getName());
        if (drawnCard.isValid()) {
            cards.remove(cards.size()-1);
            int playerIndex = CurrentStatus.playerIndex;
            System.out.println("It's a valid card, move on to the next player");
            CurrentStatus.updateCurrentStatus(drawnCard);
            CurrentStatus.checkOver(playerIndex);
        } else {
            System.out.println("It's not a valid card, move on to the next player");
            CurrentStatus.nextPlayer();
        }
    }

    /*
     * This function can smartly choose which color when AI put out wild card.
     * The logic is that AI selects the color that it has most.
     */
    public void chooseColorSmartly() {
        HashMap<String, Integer> colors = new HashMap<>();
        for (Card card: cards) {
            String cardColor = card.getColor();
            if (Arrays.asList(COLOR_LIST).contains(cardColor)) {
                colors.put(cardColor, colors.getOrDefault(cardColor, 0)+1);
            }
        }
        String maxColor = "red";
        int maxValue = colors.getOrDefault("red", 0);
        for (String color:COLOR_LIST) {
            int value = colors.getOrDefault(color, 0);
            if (value > maxValue) {
                maxColor = color;
                maxValue = value;
            }
        }
        CurrentStatus.color = maxColor;
    }
}
