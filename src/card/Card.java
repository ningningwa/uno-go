package card;

/**
 *  This class is for card generation.
 *  This is just a template for many kinds of cards.
 */
public abstract class Card {
    /*
     * type of card
     * @return
     * "nf" if it has no function,
     * "wild" if it has function wild
     * "wildDrawFour" if it has function wildDrawFour
     * "skip" if has function skip
     * "reverse" if has function reverse
     * "drawTwo" if has function drawTwo
     */
    public abstract String getFunction();

    /*
     * number on the card
     * @return 0-9 for regular card, -1 for card with function
     */
    public abstract int getNumber();

    /*
     * color on the card
     * @return
     * "red" for red card, "blue" for blue card
     * "yellow" for yellow card, "green" for green card, "wild" for wild function card
     */
    public abstract String getColor();

    public abstract String getName();

    /*
     * activate card function
     */
    public abstract void activateFunction();

    /*
     * check whether this card can be put out.
     */
    public abstract Boolean isValid();
}
