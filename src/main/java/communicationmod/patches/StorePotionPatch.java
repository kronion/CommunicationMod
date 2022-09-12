package communicationmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.shop.StorePotion;
import communicationmod.CommunicationMod;


public class StorePotionPatch {
    @SpirePatch(
            clz = StorePotion.class,
            method = "purchasePotion"
    )
    public static class StorePotionPurchasePatch {

        public static void Postfix() {
            // Without this there's no state change if potion slots are full,
            // or if the player has the Sozu relic.
            CommunicationMod.mustSendGameState = true;
        }

    }
}
