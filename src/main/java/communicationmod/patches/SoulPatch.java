package communicationmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.Soul;


public class SoulPatch {
    @SpirePatch(
        clz = Soul.class,
        method = "updateMovement"
    )
    public static class SoulMovementPatch {
        public static SpireReturn<Void> Prefix() {
            return SpireReturn.Return();
        }
    }
}