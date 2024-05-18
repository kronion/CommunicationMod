package communicationmod.patches;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.potions.SmokeBomb;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import communicationmod.GameStateListener;
import javassist.CannotCompileException;
import javassist.CtBehavior;

import java.util.ArrayList;

public class SmokeBombPatch {
    public static boolean smoked = false;

    @SpirePatch(
        clz = SmokeBomb.class,
        method = "use"
    )
    public static class BlockOnUse {
        public static void Prefix() {
            smoked = true;
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
            float timer = ReflectionHacks.getPrivate(_instance, AbstractRoom.class, "endBattleTimer");
            if (timer < 0.0f) {
                smoked = false;
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