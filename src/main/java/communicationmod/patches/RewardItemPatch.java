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
        public static void Postfix() {
            // Without this there's no state change if potion slots are full.
            // Note that this patch triggers for all rewards, not just potions,
            // but it's ok because taking other rewards should trigger a state
            // change anyway.
            GameStateListener.registerStateChange();
        }
    }
}
