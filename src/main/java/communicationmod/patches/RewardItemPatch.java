package communicationmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.rewards.RewardItem;
import communicationmod.GameStateListener;


public class RewardItemPatch {
    @SpirePatch(
            clz = RewardItem.class,
            method = "claimReward"
    )
    public static class RewardItemClaimPatch {
        public static void Postfix(RewardItem __instance) {
            // Without this there's no state change if potion slots are full.
            // For rewards other than potions, we expect there will always be
            // a state change as usual.
            if (__instance.type == RewardItem.RewardType.POTION) {
                GameStateListener.registerStateChange();
            }
        }
    }
}
