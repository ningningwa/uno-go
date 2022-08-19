package play;
import card.Card;
import collections.CurrentCards;
import collections.UsedCards;
import collections.PlayerCards;

import java.util.*;

public class CurrentStatus {
    public static String input = "";
    public static Boolean inputDone = false;
    public static Boolean over = false; // whether game over
    public static Boolean skip = false; // whether skip has been put out
    public static int direction = 1; // current direction (when reverse is put out)
    public static int numberOfPlayers;
    public static ArrayList<String> playerSequence;
    public static HashMap<String, PlayerCards> players;
    public static int playerIndex; // currentPlayerIndex
    public static int penalty; // for accumulated penalty calculation
    public static PlayerCards playerCards; // currentPlayerCards
    public static String playerName; // currentPlayerName
    public static String function; // currentCardFunction
    public static String color; // currentCardColor
    public static int number; //currentCardNumber
    public static Card currentCard;
    public static CurrentCards currentCards;
    public static UsedCards usedCards;

    /*
     * move on to the next player
     */
    public static void nextPlayer() {
        playerIndex = Math.floorMod(direction + playerIndex, numberOfPlayers);
        playerCards = getPlayerCards(playerIndex);
        playerName = getPlayerName(playerIndex);
        try {
            Thread.sleep(3000);
        } catch(Exception e) {
        }
        try {
            GUI.textArea.setText("");
        } catch (Exception e) {
        }
    }

    /*
     * currentPlayer draws card
     */
    public static void playerCardsDraw(int number) {
        playerCards.drawCards(number);
    }

    public static String getPlayerName(int index) {
        return playerSequence.get(index);
    }

    public static PlayerCards getPlayerCards(int number) {
        return players.getOrDefault(getPlayerName(number), null);
    }

    /*
     * check game over
     */
    public static void checkOver(int checkIndex) {
        if (getPlayerCards(checkIndex).isEmpty()) {
            String win = getPlayerName(checkIndex)+" wins";
            System.out.println(win);
            over = true;
        }
    }

    /*
     * When card is drawn, if it is valid, it must be put out.
     * Otherwise, it keeps in hand.
     * Move on to the next player.
     */
    public static Card drawEffect() {
        Card drawnCard = playerCards.drawCard();
        System.out.println("Your drawn card is " + drawnCard.getName());
        int length = CurrentStatus.playerCards.size();
        if (CurrentStatus.putOutCard(length) == false) {
            System.out.println("It's not a valid card, move on to the next player");
            CurrentStatus.nextPlayer();
        } else {
            System.out.println("It's a valid card, move on to the next player");
        }
        return drawnCard;
    }

    public static Boolean putOutCard(int number) {
        int checkIndex = playerIndex;
        Boolean isValid = playerCards.playCard(number);
        checkOver(checkIndex);
        return isValid;
    }

    /*
     * when card has been put out, the current gameStatus is updated
     */
    public static void updateCurrentStatus(Card card) {
        usedCards.addOneCard(card);
        currentCard = card;
        color = card.getColor();
        number = card.getNumber();
        function = card.getFunction();
        card.activateFunction();
        nextPlayer();
        if (skip == true) {
            nextPlayer();
            skip = false;
        }
    }
}
