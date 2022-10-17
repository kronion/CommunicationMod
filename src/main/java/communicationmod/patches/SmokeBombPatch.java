package communicationmod.patches;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.potions.SmokeBomb;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import communicationmod.GameStateListener;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class SmokeBombPatch {
    static boolean waiting = false;

    @SpirePatch(
        clz = SmokeBomb.class,
        method = "use"
    )
    public static class BlockOnUse {
        public static void Prefix() {
            waiting = true;
            GameStateListener.blockStateUpdate();
        }
    }

    @SpirePatch(
        clz = AbstractRoom.class,
        method = "update"
    )
    public static class Unblock {
        @SpireInsertPatch(
        locator=LocatorAfter.class
        )
        public static void Insert(AbstractRoom _instance) {
            Logger logger = LogManager.getLogger(SmokeBombPatch.class.getName());
            logger.info("HERE");
            float timer = ReflectionHacks.getPrivate(_instance, AbstractRoom.class, "endBattleTimer");
            logger.info(timer);
            if (timer < 0.0f) {
                GameStateListener.resumeStateUpdate();
            }
        }

        private static class LocatorAfter extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher matcher = new Matcher.FieldAccessMatcher(AbstractRoom.class, "endBattleTimer");
                int[] lines = LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), matcher);
                lines[0] += 1;  // Insert our code right after the matched line
                return lines;
            }
        }
    }
}