package communicationmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.audio.Sfx;


public class SfxPatch {
    @SpirePatch(
        clz = Sfx.class,
        method = "play",
        paramtypez={
            float.class
        }
    )
    public static class PlayPatch {
        public static SpireReturn<Long> Prefix() {
            return SpireReturn.Return(0L);
        }
    }

    @SpirePatch(
        clz = Sfx.class,
        method = "play",
        paramtypez={
            float.class,
            float.class,
            float.class
        }
    )
    public static class PlayPatch2 {
        public static SpireReturn<Long> Prefix() {
            return SpireReturn.Return(0L);
        }
    }

    @SpirePatch(
        clz = Sfx.class,
        method = "loop",
        paramtypez={
            float.class
        }
    )
    public static class LoopPatch {
        public static SpireReturn<Long> Prefix() {
            return SpireReturn.Return(0L);
        }
    }

}