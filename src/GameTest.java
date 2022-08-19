import card.*;
import collections.*;
import org.junit.Assert;
import org.junit.Test;

import play.CurrentStatus;
import play.GUI;
import play.Game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

import static org.junit.Assert.*;

public class GameTest implements DS {

    /**
     * Test initializing the draw pile with the 108 cards
     */
    @Test
    public void testInitialize108() {
        Game testGame = new Game();
        testGame.initialize_cards();

        int nCurrentCards = CurrentStatus.currentCards.size();
        assertEquals(108, nCurrentCards);

        LinkedList<Card> current = CurrentStatus.currentCards.getCards();
        HashMap<String, Integer> notZeroOrWild = new HashMap<>();
        HashMap<String, Integer> zero = new HashMap<>();
        HashMap<String, Integer> wild = new HashMap<>();
        for (int i = 0; i < current.size(); i++) {
            Card card = current.get(i);
            String color = card.getColor();
            int number = card.getNumber();
            String name = card.getName();
            if (Arrays.asList(COLOR_LIST).contains(color)) {
                if (number != 0) {
                    int count = notZeroOrWild.getOrDefault(name, 0);
                    notZeroOrWild.put(name, count+1);
                } else {
                    int count = zero.getOrDefault(name, 0);
                    zero.put(name, count+1);
                }
            } else {
                int count = wild.getOrDefault(name, 0);
                wild.put(name, count+1);
            }
        }
        for (int value: notZeroOrWild.values()) {
            assertEquals(2, value);
        }
        for (int value: zero.values()) {
            assertEquals(1, value);
        }
        for (int value: wild.values()) {
            assertEquals(4, value);
        }
    }

    /**
     * Test given a card, initializing the initial state properly
     */
    @Test
    public void testSingleCard() {
        Wild wild = new Wild("wild", -1, "wild");
        assertEquals("wild", wild.getName());

        CurrentStatus.penalty = 0;
        WildDrawFour wildDrawFour = new WildDrawFour("wild", -1, "wildDrawFour");
        assertEquals("wildDrawFour", wildDrawFour.getName());

        Normal normal = new Normal("blue", 1, "nf");
        assertEquals("blue1", normal.getName());

        Skip skip = new Skip("red", -1, "skip");
        assertEquals("redSkip", skip.getName());

        Reverse reverse = new Reverse("yellow", -1, "reverse");
        assertEquals("yellowReverse", reverse.getName());

        DrawTwo drawTwo = new DrawTwo("green", -1, "drawTwo");
        assertEquals("greenDrawTwo", drawTwo.getName());
    }

    /**
     * Test assigning cards to each player at the beginning
     */
    @Test
    public void testInitializePlayerCards() {
        Game testGame = new Game();
        initialize(testGame, -1);
        int nPlayerCards = 0;
        int count = 0;
        for (PlayerCards playerCards: CurrentStatus.players.values()) {
            nPlayerCards = playerCards.size();
            assertEquals(7, nPlayerCards);
            count += 1;
        }
        assertEquals(3, count);
    }

    /**
     * Test updating the draw pile when the cards are drawn, and reusing discard pile when no card is left in the draw pile
     */
    @Test
    public void testDrawCard() {
        Game testGame = new Game();
        testGame.initialize_cards();
        UsedCards used = CurrentStatus.usedCards;
        CurrentCards current = CurrentStatus.currentCards;
        assertEquals(108, current.size());
        assertEquals(108, current.size());
        assertEquals(0, used.size());

        PlayerCards player = new PlayerCards();
        player.drawCard();
        assertEquals(107, current.size() );
        assertEquals(0, used.size());
    }

    /**
     * test Updating the draw pile when the cards are drawn,
     * and reusing discard pile when no card is left in the draw pile
     */
    @Test
    public void testReuseCards() {
        CurrentStatus.currentCards = new CurrentCards();
        CurrentStatus.usedCards = new UsedCards(CurrentStatus.currentCards);
        UsedCards used = CurrentStatus.usedCards;
        CurrentCards current = CurrentStatus.currentCards;
        current.setEmpty();
        assertEquals(0, current.size());
        assertEquals(108, used.size());
        PlayerCards player = new PlayerCards();
        player.drawCard();
        assertEquals(107, current.size());
        assertEquals(0, used.size());
    }

    /**
     * test when a normal or wild card is attempted to play, its validity needs to be checked
     */
    @Test
    public void testValidityForNormalOrSimpleWild() {
        CurrentStatus.color = "red";
        CurrentStatus.number = 1;
        CurrentStatus.function = "nf";

        Normal normal = new Normal("blue", 2, "nf");
        assertFalse(normal.isValid());

        normal = new Normal("red", 2, "nf");
        assertTrue(normal.isValid());

        normal = new Normal("blue", 1, "nf");
        assertTrue(normal.isValid());

        DrawTwo drawTwo = new DrawTwo("blue", -1, "drawTwo");
        assertFalse(drawTwo.isValid());

        drawTwo = new DrawTwo("red", -1, "drawTwo");
        assertTrue(drawTwo.isValid());

        wildDrawFourPuttingOutForNonWildDrawFour();
    }

    /**
     * test when a drawTwo card is attempted to play, its validity needs to be checked
     */
    @Test
    public void testValidityForDrawTwo() {
        CurrentStatus.currentCard = new DrawTwo("red", -1, "drawTwo");
        CurrentStatus.color = "red";
        CurrentStatus.number = 1;
        CurrentStatus.function = "drawTwo";
        CurrentStatus.penalty = 4;

        DrawTwo drawTwo = new DrawTwo("blue", -1, "drawTwo");
        assertTrue(drawTwo.isValid());

        wildDrawFourPuttingOutForNonWildDrawFour();
    }

    /**
     * test when a drawFour card is attempted to play, its validity needs to be checked
     */
    @Test
    public void testValidityForWildDrawFour() {
        CurrentStatus.currentCard = new WildDrawFour("wild", -1, "wildDrawFour");
        CurrentStatus.color = "red";
        CurrentStatus.number = 1;
        CurrentStatus.function = "wildDrawFour";
        CurrentStatus.penalty = 4;
        DrawTwo drawTwo = new DrawTwo("blue", -1, "drawTwo");
        assertFalse(drawTwo.isValid());
        WildDrawFour wildDrawFour = new WildDrawFour("wild", -1, "wildDrawFour");
        assertTrue(wildDrawFour.isValid());
    }

    private void wildDrawFourPuttingOutForNonWildDrawFour() {
        CurrentStatus.playerCards = new PlayerCards(new CurrentCards());
        WildDrawFour wildDrawFour = new WildDrawFour("wild", -1, "wildDrawFour");
        assertFalse(wildDrawFour.isValid());
        CurrentStatus.playerCards.setEmpty();
        assertTrue(wildDrawFour.isValid());
    }

    /**
     * test updating the game state when a card is played
     */
    @Test
    public void testUpdatedGameState() {
        Game testGame = new Game();
        initialize(testGame, -1);

        Normal normal = new Normal("blue", 2, "nf");
        CurrentStatus.updateCurrentStatus(normal);
        assertEquals(normal, CurrentStatus.currentCard);
        assertEquals("blue", CurrentStatus.color);
        assertEquals(2, CurrentStatus.number);
        assertEquals("nf", CurrentStatus.function);

        DrawTwo drawTwo = new DrawTwo("red", -1, "drawTwo");
        CurrentStatus.updateCurrentStatus(drawTwo);
        assertEquals(drawTwo, CurrentStatus.currentCard);
        assertEquals("red", CurrentStatus.color);
        assertEquals(-1, CurrentStatus.number);
        assertEquals("drawTwo", CurrentStatus.function);

        Skip skip = new Skip("green", -1, "skip");
        int previousPlayerIndex = CurrentStatus.playerIndex;
        int playerIndex = Math.floorMod(CurrentStatus.direction*2 + previousPlayerIndex, CurrentStatus.numberOfPlayers);
        CurrentStatus.updateCurrentStatus(skip);
        assertEquals(playerIndex, CurrentStatus.playerIndex);
        assertEquals(skip, CurrentStatus.currentCard);
        assertEquals("green", CurrentStatus.color);
        assertEquals(-1, CurrentStatus.number);
        assertEquals("skip", CurrentStatus.function);

        Reverse reverse = new Reverse("blue", -1, "reverse");
        int previousDirection = CurrentStatus.direction;
        CurrentStatus.updateCurrentStatus(reverse);
        assertNotEquals(previousDirection, CurrentStatus.direction);
        assertEquals(reverse, CurrentStatus.currentCard);
        assertEquals("blue", CurrentStatus.color);
        assertEquals(-1, CurrentStatus.number);
        assertEquals("reverse", CurrentStatus.function);
    }

    /**
     * test If a player draws a card from the draw stack,
     * they should play it if it is valid.
     * Otherwise, they should skip this turn
     */
    @Test
    public void testDrewCardValidity() {
        Game testGame = new Game();
        initialize(testGame, -1);

        Wild wild = new Wild("wild", -1, "wild");
        CurrentStatus.currentCard = wild;
        CurrentStatus.color = "blue";
        CurrentStatus.number = -1;
        CurrentStatus.function = "wild";
        CurrentStatus.penalty = 0;

        int previousUsedCardsSize = CurrentStatus.usedCards.size();
        Card previousCard = CurrentStatus.currentCard;
        CurrentStatus.drawEffect();
        if (previousCard == CurrentStatus.currentCard) {
            assert(CurrentStatus.usedCards.size() == previousUsedCardsSize || previousUsedCardsSize == CurrentStatus.currentCards.size() + 1);
        } else {
            assert(CurrentStatus.usedCards.size() - previousUsedCardsSize == 1 || previousUsedCardsSize == CurrentStatus.currentCards.size() + 1);
        }
    }

    /**
     * test when a player doesn't have a card to play or choose to skip this round on the penalty,
     * the penalty should be applied to the player, and the player should skip this turn
     */
    @Test
    public void testPenaltySkip() {
        Game testGame = new Game();
        initialize(testGame, -1);

        DrawTwo drawTwo = new DrawTwo("blue", -1, "drawTwo");
        CurrentStatus.currentCard = drawTwo;
        CurrentStatus.color = "blue";
        CurrentStatus.number = -1;
        CurrentStatus.function = "drawTwo";
        CurrentStatus.penalty = 0;
        CurrentStatus.updateCurrentStatus(drawTwo);
        /*This can be tested in local game because I let players have there own rights to choose whether to put out
         *cards with draw attributes when there is penalty accumulated.
         */
    }

    /**
     * test game ending conditions (detecting when a player has played out all the cards)
     */
    @Test
    public void testGameOver() {
        Game testGame = new Game();
        initialize(testGame, -1);

        Normal normal = new Normal("blue", 2, "nf");
        CurrentStatus.updateCurrentStatus(normal);

        CurrentStatus.playerCards = new PlayerCards();
        CurrentStatus.players.put(CurrentStatus.playerName, CurrentStatus.playerCards);
        CurrentStatus.penalty = 0;
        CurrentStatus.playerCards.addOneCard(new Normal("blue", 1, "nf"));
        CurrentStatus.putOutCard(1);
        assertTrue(CurrentStatus.over);
    }

    /**
     * test consecutive stacking
     */
    @Test
    public void testConsecutiveStacking() {
        Game testGame = new Game();
        initialize(testGame, -1);

        DrawTwo drawTwo = new DrawTwo("blue", -1, "drawTwo");
        CurrentStatus.currentCard = drawTwo;
        CurrentStatus.color = "blue";
        CurrentStatus.number = -1;
        CurrentStatus.function = "drawTwo";
        CurrentStatus.penalty = 0;
        CurrentStatus.updateCurrentStatus(drawTwo);
        /*This can be also tested in local game because I let players have there own rights to choose whether to put out
         *cards with draw attributes when there is penalty accumulated.
         */
    }

    @Test
    public void testReverseOnBlack() {
        Wild wild = new Wild("wild", -1, "wild");
        int direction = CurrentStatus.direction;
        CurrentStatus.input = "y";
        CurrentStatus.inputDone = true;
        wild.makeDirectionChoice();
        assertEquals(0, direction+CurrentStatus.direction);

        direction = CurrentStatus.direction;
        CurrentStatus.input = "aaa";
        CurrentStatus.inputDone = true;
        wild.makeDirectionChoice();
        assertNotEquals(0, direction+CurrentStatus.direction);
        return;
    }

    @Test
    public void testNumberOnBlack() {
        Wild wild = new Wild("wild", -1, "wild");
        CurrentStatus.number = 7;
        CurrentStatus.input = "8";
        CurrentStatus.inputDone = true;
        wild.makeNumberOrColorChoice();
        assertEquals(8, CurrentStatus.number);

        CurrentStatus.input = "4";
        CurrentStatus.inputDone = true;
        wild.makeNumberOrColorChoice();
        assertEquals(4, CurrentStatus.number);
    }

    /**
     * This function is to test whether AI players are correctly initialized
     */
    @Test
    public void testAIPlayersInitialization() {
        Game testGame = new Game();
        initialize(testGame, 3);
        int count = 0;
        for (PlayerCards value:CurrentStatus.players.values()) {
            if (value instanceof AIPlayerCards) {
                count++;
            }
        }
        assertEquals(3, count);
    }

    /**
     * This function is to test whether AI get the most frequent color in his hand.
     */
    @Test
    public void testAIchooseColor() {
        CurrentStatus.color = "yellow";
        AIPlayerCards aiPlayerCards = new AIPlayerCards();
        aiPlayerCards.addOneCard(new Normal("blue", 1, "nf"));
        aiPlayerCards.addOneCard(new Normal("yellow", 1, "nf"));
        aiPlayerCards.addOneCard(new Normal("blue", 2, "nf"));
        aiPlayerCards.addOneCard(new Normal("red", 2, "nf"));
        aiPlayerCards.chooseColorSmartly();
        assertEquals("blue", CurrentStatus.color);

        aiPlayerCards.setEmpty();
        aiPlayerCards.addOneCard(new Normal("yellow", 1, "nf"));
        aiPlayerCards.addOneCard(new Normal("yellow", 1, "nf"));
        aiPlayerCards.addOneCard(new Normal("blue", 2, "nf"));
        aiPlayerCards.addOneCard(new Normal("red", 2, "nf"));
        aiPlayerCards.addOneCard(new Normal("wild", -1, "wild"));
        aiPlayerCards.chooseColorSmartly();
        assertEquals("yellow", CurrentStatus.color);
    }

    private void initialize(Game testGame, int nums) {
        String[] humanPlayerList = {"abc", "def", "ghi"};
        CurrentStatus.playerSequence = new ArrayList();
        CurrentStatus.penalty = 0;
        testGame.initialize_cards();
        if (nums < 0) {
            testGame.initialize_players(humanPlayerList,null);
        } else {
            String[] AIPlayersList = new String[nums];
            for (int i = 1; i <= nums; i++) {
                AIPlayersList[i-1] = "AI No. " + i;
            }
            testGame.initialize_players(humanPlayerList, AIPlayersList);
        }
        testGame.play();
    }

}
