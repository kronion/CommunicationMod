package communicationmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.monsters.AbstractMonster;


public class AbstractMonsterPatch {

    @SpirePatch(
            clz = AbstractMonster.class,
            method = "updateEscapeAnimation"
    )
    public static class EnsureEventualEscapePatch {
        public static void Prefix(AbstractMonster _instance) {
            if (_instance.flipHorizontal && _instance.escapeTimer == 0.0F) {
                _instance.escapeTimer = -0.001F;
            }
        }
    }
}