package collections;
import card.*;

import java.util.ArrayList;
import java.util.Collections;

/**
 *  This class is for currentCard that can be drawn.
 */
public class CurrentCards extends Cards implements DS {
    /*
    initialize cards stack when the game begins
     */
    public CurrentCards() {
        for (int i = 0; i < COLOR_LIST.length; i++) {
            for (int j = 0; j < NUMBER_LIST.length; j++) {
                Normal toAdd = new Normal(COLOR_LIST[i], NUMBER_LIST[j], "nf");
                this.cards.add(toAdd);
                if (NUMBER_LIST[j] != 0) {
                    Normal twiceAdd = new Normal(COLOR_LIST[i], NUMBER_LIST[j], "nf");
                    this.cards.add(twiceAdd);
                }
            }
            for (int j = 0; j < 2; j++) {
                Skip skip = new Skip(COLOR_LIST[i], -1, "skip");
                this.cards.add(skip);
                Reverse reverse = new Reverse(COLOR_LIST[i], -1, "reverse");
                this.cards.add(reverse);
                DrawTwo drawTwo = new DrawTwo(COLOR_LIST[i], -1, "drawTwo");
                this.cards.add(drawTwo);
            }
            Wild wild = new Wild(null, -1, "wild");
            this.cards.add(wild);
            WildDrawFour wildDrawFour = new WildDrawFour(null, -1, "wildDrawFour");
            this.cards.add(wildDrawFour);
        }
        Collections.shuffle(this.cards);
    }

    public void fillCards(UsedCards toFill) {
        if (this.isEmpty()) {
            this.cards = toFill.getCards();
            Collections.shuffle(this.cards);
            toFill.setEmpty();
        }
    }
}
