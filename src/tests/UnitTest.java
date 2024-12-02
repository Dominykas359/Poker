package tests;

import helpers.GameFlowHelpers;
import helpers.WinnerHelpers;
import misc.CalculateHand;
import misc.Table;
import models.Card;
import models.Hand;
import models.Player;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UnitTest {

    @Test
    public void testEvaluateRoyalFlush() {
        List<Card> royalFlush = Arrays.asList(
                new Card(1, 10),  // 10 of Hearts
                new Card(1, 11),  // Jack of Hearts
                new Card(1, 12),  // Queen of Hearts
                new Card(1, 13),  // King of Hearts
                new Card(1, 1)    // Ace of Hearts
        );

        Hand result = CalculateHand.evaluateHand(royalFlush);
        assertEquals(10, result.getRankValue());
        assertEquals(royalFlush, result.getCards());
    }

    @Test
    public void testAwardPrizeMoney(){
        Player player = new Player("Test", 100.0, true, false, 0, false, false);
        Table table = Table.getInstance();

        table.setPrizeMoney(50.0);

        WinnerHelpers.awardPrizeMoney(player, table);

        assertEquals(150, player.getMoney(), 0.01);
        assertEquals(0, table.getPrizeMoney(), 0.01);
        String lastAction = table.getActionHistory().get(table.getActionHistory().size() - 1);
        assertEquals("Test wins the round with $50.0", lastAction);
    }

    @Test
    public void testAllPlayersMatchedBet(){
        List<Player> players = new ArrayList<>();

        Player player = new Player("Test", 100.0, true, false, 0, false, false);
        Player player2 = new Player("Test2", 100.0, true, false, 0, false, false);

        player.setBet(100);
        player.setMoved(true);
        players.add(player);

        player2.setBet(100);
        player2.setMoved(true);
        players.add(player2);

        double currentBet = 100;

        boolean result = GameFlowHelpers.allPlayersHaveMatchedBet(players, currentBet);

        assertTrue(result);
    }
}
