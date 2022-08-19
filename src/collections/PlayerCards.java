package collections;

import card.Card;
import play.CurrentStatus;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 *  This class is for playerCards that can be put out.
 */
public class PlayerCards extends Cards {
    public PlayerCards() {
        super();
    }

    public PlayerCards(Cards cards) {
        this.cards = new LinkedList<>(cards.getCards());
    }

    public PlayerCards(int nums) {
        drawCards(nums);
    }

    public void drawCards(int nums) {
        while (nums != 0) {
            drawCard();
            nums--;
        }
    }

    public Card drawCard() {
        Card toDraw;
        CurrentCards current = CurrentStatus.currentCards;
        UsedCards used = CurrentStatus.usedCards;
        /* When currentCardStack is empty, we need to let used cards fill in it. */
        if (current.isEmpty()) {
            current.fillCards(used);
        }
        toDraw = current.popOneCard();
        this.addOneCard(toDraw);
        return toDraw;
    }

    /*
     * show player cards to the terminal
     */
    public ArrayList<String> showCards() {
        ArrayList<String> output = new ArrayList<>();
        for (int i = 0; i < this.cards.size(); i++) {
            String toAdd = "("+(i+1)+") "+this.cards.get(i).getName();
            output.add(toAdd);
        }
        return output;
    }

    public Boolean playCard(int number) {
        //index outOf bound
        if (number < 1 || number > this.cards.size()) {
            return false;
        }
        Card card = this.cards.get(number-1);
        if (!card.isValid()) {
            return false;
        }
        CurrentStatus.updateCurrentStatus(card);
        this.cards.remove(number-1);
        return true;
    }
}
