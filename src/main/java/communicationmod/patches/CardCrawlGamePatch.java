package communicationmod.patches;

import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.ExceptionHandler;
import com.megacrit.cardcrawl.daily.TimeHelper;
import communicationmod.CommunicationMod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
            clz = CardCrawlGame.class,
            method = "dispose"
    )
    public static class DisposePatch {
        public static void Prefix(CardCrawlGame _instance) {
            CommunicationMod.dispose();
        }
    }
}