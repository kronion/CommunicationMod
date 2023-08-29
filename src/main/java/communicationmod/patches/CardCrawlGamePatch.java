package communicationmod.patches;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.ExceptionHandler;
import com.megacrit.cardcrawl.daily.TimeHelper;
import com.megacrit.cardcrawl.desktop.DesktopLauncher;
import communicationmod.CommunicationMod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

public class CardCrawlGamePatch {
    private static final Logger logger = LogManager.getLogger(CardCrawlGamePatch.class.getName());

    public static class RenderState {
        public static boolean render = false;
    }

    @SpirePatch(
        clz = CardCrawlGame.class,
        method = "render"
    )
    public static class RenderPatch {
        public static SpireReturn<Void> Prefix(CardCrawlGame __instance) {
            if (!RenderState.render) {
                try {
                    TimeHelper.update();
                    if (Gdx.graphics.getRawDeltaTime() > 0.1F)
                        return SpireReturn.Return();
                    __instance.update();
                } catch (Exception e) {
                    logger.info("Exception occurred in CardCrawlGame render method!");
                    ExceptionHandler.handleException(e, logger);
                    Gdx.app.exit();
                }

                return SpireReturn.Return();
            }

            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
        clz = Display.class,
        method = "update",
        paramtypez={
            boolean.class
        }
    )
    public static class DisplayPatch {
        public static SpireReturn<Void> Prefix() {
            // By profiling, we determined lots of time is spent by the GDX game library updating
            // the screen, and it happens regardless of whether or not the STS application "listener"
            // actually does anything in `render()`. Here, we disable the display update entirely.
            if (!RenderState.render) {
                return SpireReturn.Return();
            }

            return SpireReturn.Continue();
        }
    }

     @SpirePatch(
        clz = Display.class,
        method = "processMessages"
    )
    public static class DisplayPatch2 {
        public static SpireReturn<Void> Prefix() {
            // By profiling, we determined lots of time is spent by the GDX game library updating
            // the screen, and it happens regardless of whether or not the STS application "listener"
            // actually does anything in `render()`. Here, we disable the display update entirely.
            if (!RenderState.render) {
                return SpireReturn.Return();
            }

            return SpireReturn.Continue();
        }
    }

     @SpirePatch(
        clz = DesktopLauncher.class,
        method = "loadSettings"
    )
    public static class AudioPatch {
        public static void Prefix(LwjglApplicationConfiguration config) {
            // By profiling, we determined lots of time is spent by the GDX game library updating
            // the audio`. Here, we disable the audio update entirely.
            config.disableAudio = true;
        }
    }

    @SpirePatch(
        clz = CardCrawlGame.class,
        method = "dispose"
    )
    public static class DisposePatch {
        public static void Prefix(CardCrawlGame _instance) {
            CommunicationMod.dispose();
        }
    }
}