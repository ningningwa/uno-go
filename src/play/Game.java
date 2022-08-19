package play;
import card.Card;
import collections.AIPlayerCards;
import collections.CurrentCards;
import collections.PlayerCards;
import collections.UsedCards;

import java.util.*;
import java.util.HashMap;

public class Game {
    public void start(String[] humans, String[] AIs) {
        CurrentStatus.playerSequence = new ArrayList();
        CurrentStatus.penalty = 0;
        initialize_cards();
        initialize_players(humans, AIs);
        play();
        makeCardChoice();
    }

    public void initialize_cards() {
        CurrentStatus.currentCards = new CurrentCards();
        CurrentStatus.usedCards = new UsedCards();
    }

    /**
     * initialize players based on whether they are humans or not.
     */
    public void initialize_players(String[] humans, String[] AIs) {
        CurrentStatus.players = new HashMap<>();
        createIdentity(humans, false);
        if (AIs != null) {
            createIdentity(AIs, true);
            CurrentStatus.numberOfPlayers = humans.length + AIs.length;
        } else {
            CurrentStatus.numberOfPlayers = humans.length;
        }
    }

    private void createIdentity(String[] identities, Boolean AI) {
        if (identities == null || identities.length == 0) {
            return;
        }
        for (int i = 0; i < identities.length; i++) {
            String playerName = identities[i];
            PlayerCards playerCards;
            if (AI) {
                playerCards = new AIPlayerCards(7);
            } else {
                playerCards = new PlayerCards(7);
            }
            CurrentStatus.players.put(playerName, playerCards);
            CurrentStatus.playerSequence.add(playerName);
        }
    }

    public void play() {
        CurrentStatus.playerIndex = -1;
        Card card = CurrentStatus.currentCards.popOneCard();
        while (card.getColor() == null) {
            CurrentStatus.usedCards.addOneCard(card);
            card = CurrentStatus.currentCards.popOneCard();
        }
        CurrentStatus.updateCurrentStatus(card);
    }

    /**
     * make penalty choice
     * detailed penalty accumulation functions are in Card classes
     */
    private void makePenaltyChoice() {
        System.out.println("Do you accept the penalty (if not, please put out your card with draw attribute):");
        String choice = CurrentStatus.input;
        while (!CurrentStatus.inputDone) {
            choice = CurrentStatus.input;
            choice = choice.toLowerCase();
        }
        CurrentStatus.inputDone = false;
        /*let the player choose whether to accept penalty for human right. */
        if (choice.equals("y") || choice.equals("yes")) {
            CurrentStatus.playerCardsDraw(CurrentStatus.penalty);
            CurrentStatus.penalty = 0;
            CurrentStatus.nextPlayer();
            return;
        }
        int number;
        try {
            number = Integer.parseInt(choice);
        } catch (Exception e) {
            System.out.println("It's not a valid card");
            makePenaltyChoice();
            return;
        }
        if (CurrentStatus.putOutCard(number) == false) {
            System.out.println("It's not a valid card");
            makePenaltyChoice();
            return;
        }
    }

    public void makeCardChoice() {
        if (CurrentStatus.over == true) {
            try {
                Thread.sleep(5000);
            } catch(Exception e) {
            }
            System.exit(0);
        }
        String currentPlayerName = CurrentStatus.playerName;
        ArrayList<String> currentPlayerCards = CurrentStatus.playerCards.showCards();
        System.out.println(currentPlayerName+"\n"+currentPlayerCards);
        System.out.println("currentCard: "+CurrentStatus.currentCard.getName());
        System.out.println("currentColor: "+CurrentStatus.color);
        System.out.println("currentNumber: "+CurrentStatus.number);
        System.out.println("currentPenalty: "+CurrentStatus.penalty);
        System.out.println("currentCardsStack: "+CurrentStatus.currentCards.size());
        System.out.println("UsedCardsStack: "+CurrentStatus.usedCards.size());
        if (CurrentStatus.playerCards instanceof AIPlayerCards) {
            try {
                Thread.sleep(2000);
            } catch(Exception e) {
            }
            ((AIPlayerCards) CurrentStatus.playerCards).play();
            makeCardChoice();
            return;
        }
        if (CurrentStatus.penalty != 0) {
            makePenaltyChoice();
            makeCardChoice();
            return;
        }
        System.out.println("Please make your choice: ");
        String choice = CurrentStatus.input;
        while (!CurrentStatus.inputDone) {
            choice = CurrentStatus.input;
            choice = choice.toLowerCase();
        }
        CurrentStatus.inputDone = false;
        if (choice.equals("draw")) {
            CurrentStatus.drawEffect();
            makeCardChoice();
            return;
        } else {
            int number;
            try {
                number = Integer.parseInt(choice);
            } catch (Exception e) {
                System.out.println("It's not a valid card");
                makeCardChoice();
                return;
            }
            if (CurrentStatus.putOutCard(number) == false) {
                System.out.println("It's not a valid card");
            } else {
            }
            makeCardChoice();
            return;
        }
    }
}
