package communicationmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;


public class EnergyPanelPatch {
    @SpirePatch(
        clz = EnergyPanel.class,
        method = "updateVfx"
    )
    public static class EnergyPanelVfxPatch {
        public static SpireReturn<Void> Prefix() {
            return SpireReturn.Return();
        }
    }
}