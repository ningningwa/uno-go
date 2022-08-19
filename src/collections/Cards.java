package collections;
import card.Card;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 *  This class is for cardStack and is parent for other cardCollection Class.
 */
public class Cards {
    protected LinkedList<Card> cards; // stored cards

    public Cards(Cards cards) {
        this.cards = new LinkedList<>(cards.getCards());
    }

    /*
    card size
     */
    public int size() {
        return this.cards.size();
    }

    public Cards() {
        this.cards = new LinkedList<>();
    }

    public boolean isEmpty() {
        return this.cards.size() == 0;
    }

    public LinkedList<Card> getCards() {
        return this.cards;
    }

    public void addOneCard(Card toAdd) {
        this.cards.add(toAdd);
    }

    public Card popOneCard() {
        return this.cards.poll();
    }

    public void setEmpty() {
        this.cards = new LinkedList<>();
    }
}
