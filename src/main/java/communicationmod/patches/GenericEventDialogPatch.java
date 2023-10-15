package communicationmod.patches;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.events.GenericEventDialog;
import communicationmod.GameStateListener;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class GenericEventDialogPatch {
    private static final Logger logger = LogManager.getLogger(GenericEventDialogPatch.class.getName());

    @SpirePatch(
            clz = GenericEventDialog.class,
            method = "update"
    )
    public static class UpdatePatch {

        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(GenericEventDialog _instance) {
            GameStateListener.registerStateChange();
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher matcher = new Matcher.FieldAccessMatcher(GenericEventDialog.class, "selectedOption");
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), matcher);
            }
        }

    }

    @SpirePatch(
            clz = GenericEventDialog.class,
            method = "show",
            paramtypez = {String.class, String.class}
    )
    public static class ShowPatch1 {
        public static void Prefix(GenericEventDialog _instance, String title, String text) {
            logger.info(title);
            logger.info(text);
        }
    }

    @SpirePatch(
            clz = GenericEventDialog.class,
            method = "show",
            paramtypez = {String.class, String.class}
    )
    public static class ShowPatch2 {
        public static void Postfix(GenericEventDialog _instance, String title, String text) {
            float animateTimer = (float) ReflectionHacks.getPrivate(_instance, GenericEventDialog.class, "animateTimer");
            boolean show = (boolean) ReflectionHacks.getPrivate(_instance, GenericEventDialog.class, "show");
            String instanceTitle = (String) ReflectionHacks.getPrivate(_instance, GenericEventDialog.class, "title");
            logger.info(animateTimer);
            logger.info(show);
            logger.info(instanceTitle);
            logger.info(title);
            logger.info(text);
        }
    }
}