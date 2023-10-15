package communicationmod.patches;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.map.MapRoomNode;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class MapRoomNodePatch {
    private static boolean choiceMade = false;

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

//    @SpirePatch(
//            clz= MapRoomNode.class,
//            method="update"
//    )
//    public static class LogPatch {
//        private static final Logger logger = LogManager.getLogger(LogPatch.class.getName());
//
//        @SpireInsertPatch(
//                locator = Locator.class
//        )
//        public static void Insert(MapRoomNode _instance) {
//            float animWaitTimer = (float) ReflectionHacks.getPrivate(_instance, MapRoomNode.class, "animWaitTimer");
//            logger.info(AbstractDungeon.screen);
//            logger.info(AbstractDungeon.dungeonMapScreen.clicked);
//            logger.info(animWaitTimer);
//        }
//
//        private static class Locator extends SpireInsertLocator {
//            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
//                Matcher matcher = new Matcher.FieldAccessMatcher(AbstractDungeon.class, "screen");
//                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), matcher);
//            }
//        }
//    }

    @SpirePatch(
            clz= MapRoomNode.class,
            method="update"
    )
    public static class TrackChoosePatch {
        private static final Logger logger = LogManager.getLogger(TrackChoosePatch.class.getName());

        private static MapRoomNode instance = null;

        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(MapRoomNode _instance) {
            choiceMade = true;
            instance = _instance;
//            logger.info("Choice made");
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher matcher = new Matcher.MethodCallMatcher(MapRoomNode.class, "playNodeSelectedSound");
                int[] lines = LineFinder.findAllInOrder(ctMethodToPatch, new ArrayList<Matcher>(), matcher);
                for (int i = 0; i < lines.length; i++) {
                    lines[i] += 1;
                }

                return lines;
            }
        }

        public static void Prefix(MapRoomNode _instance) {
            if (instance == _instance) {
//                logger.info(choiceMade);
                float animWaitTimer = (float) ReflectionHacks.getPrivate(_instance, MapRoomNode.class, "animWaitTimer");

                if (choiceMade) {
                    if (animWaitTimer == 0.0F) {
                        ReflectionHacks.setPrivate(_instance, MapRoomNode.class, "animWaitTimer", 0.001F);
                    }
                }
            }
        }
    }

    @SpirePatch(
            clz= MapRoomNode.class,
            method="update"
    )
    public static class CompleteChoicePatch {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(MapRoomNode _instance) {
            choiceMade = false;
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher matcher = new Matcher.FieldAccessMatcher(AbstractDungeon.class, "nextRoom");
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), matcher);
            }
        }
    }
}