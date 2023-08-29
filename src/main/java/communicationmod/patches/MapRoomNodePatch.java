package communicationmod.patches;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.map.MapRoomNode;

import java.util.ArrayList;

public class MapRoomNodePatch {
    @SpirePatch(
        clz=MapRoomNode.class,
        method="wingedIsConnectedTo"
    )
    public static class WingedConnectedPatch {
        public static boolean Replace(MapRoomNode __instance, MapRoomNode node) {
            boolean jumpPossible = ModHelper.isModEnabled("Flight") || (AbstractDungeon.player.hasRelic("WingedGreaves") &&
        AbstractDungeon.player.getRelic("WingedGreaves").counter > 0);

            if (jumpPossible) {
                ArrayList<MapEdge> edges = ReflectionHacks.getPrivate(__instance, MapRoomNode.class, "edges");

                for (MapEdge edge : edges) {
                    if (edge.dstY == node.y) {
                        return true;
                    }
                    // If the first edge doesn't match, none will match.
                    return false;
                }
            }

            return false;
        }
    }
}