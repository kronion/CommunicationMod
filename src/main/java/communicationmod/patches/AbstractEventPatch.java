package communicationmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.events.GenericEventDialog;
import com.megacrit.cardcrawl.events.RoomEventDialog;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;


public class AbstractEventPatch {
    private static final Logger logger = LogManager.getLogger(AbstractEventPatch.class.getName());
    public static boolean waitingToShow = false;

    @SpirePatch(
            clz = AbstractEvent.class,
            method = "update"
    )
    public static class EnsureEventShowPatch {
        public static void Prefix(AbstractEvent __instance) {
            if (__instance.waitTimer == 0.0F && waitingToShow) {
                __instance.waitTimer = 0.001F;
            }
        }
    }

    @SpirePatch(
            clz= AbstractEvent.class,
            method="update"
    )
    public static class ConfirmEventShownPatch {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert() {
            waitingToShow = false;
            logger.info("REACHED");
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher matcher = new Matcher.MethodCallMatcher(RoomEventDialog.class, "show");
                int[] result = LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), matcher);
                result[0] += 1;
                return result;
            }
        }
    }

    @SpirePatch(
            clz = AbstractImageEvent.class,
            method = "update"
    )
    public static class EnsureImageEventShowPatch {
        public static void Prefix(AbstractImageEvent __instance) {
            if (!__instance.combatTime && __instance.waitTimer == 0.0F && waitingToShow) {
                __instance.waitTimer = 0.001F;
            }
        }
    }

    @SpirePatch(
            clz= AbstractImageEvent.class,
            method="update"
    )
    public static class ConfirmImageEventShownPatch {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert() {
            waitingToShow = false;
            logger.info("REACHED");
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher matcher = new Matcher.MethodCallMatcher(GenericEventDialog.class, "show");
                int[] result = LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), matcher);
                result[0] += 1;
                return result;
            }
        }
    }
}