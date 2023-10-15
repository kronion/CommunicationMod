package communicationmod.patches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.math.Interpolation;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.events.shrines.GremlinWheelGame;
import javassist.CannotCompileException;
import javassist.CtBehavior;

import java.util.ArrayList;

public class GremlinWheelGamePatch {

    @SpirePatch(
            clz= GremlinWheelGame.class,
            method = "update"
    )
    public static class EnsureGameEndPatch {

        @SpireInsertPatch(
                locator=Locator.class
        )
        public static void Insert(GremlinWheelGame _instance) {
            float timer = ReflectionHacks.getPrivate(_instance, GremlinWheelGame.class, "animTimer");

            if (timer == 0.0F) {
                ReflectionHacks.setPrivate(_instance, GremlinWheelGame.class, "animTimer", 0.001F);
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher matcher = new Matcher.MethodCallMatcher(Interpolation.ElasticIn.class, "apply");
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), matcher);
            }
        }

    }
}