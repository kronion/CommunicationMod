package communicationmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import javassist.CannotCompileException;
import javassist.CtBehavior;

import java.util.ArrayList;

@SpirePatch(
        clz= AbstractDungeon.class,
        method="closeCurrentScreen"
)
public class AbstractDungeonPatch {
    @SpireInsertPatch(
            locator=Locator.class
    )
    public static void Insert() {
        // Avoid reopening closed "combat" rewards in a shop after doing a card removal.
        // Without this patch, combat rewards due to relics like Cauldron and Orrery can
        // be reopened after a card removal, which causes CommunicationMod to hang forever.
        //
        // Technically we're changing the actual game behavior here, but it seems unlikely
        // that it would be intended to reopen these reward screens once the player has
        // closed them.
        if (!AbstractDungeon.combatRewardScreen.rewards.isEmpty()) {
            if (AbstractDungeon.previousScreen == AbstractDungeon.CurrentScreen.SHOP) {
                AbstractDungeon.combatRewardScreen.clear();
            }
        }
    }

    private static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
            Matcher matcher = new Matcher.FieldAccessMatcher(AbstractDungeon.class, "combatRewardScreen");
            return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), matcher);
        }
    }
}