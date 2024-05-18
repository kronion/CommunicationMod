package communicationmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.rooms.EventRoom;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class EventRoomPatch {
    private static final Logger logger = LogManager.getLogger(EventRoomPatch.class.getName());

    @SpirePatch(
            clz = EventRoom.class,
            method = "onPlayerEntry"
    )
    public static class LogEventNamePatch {
        public static void Postfix(EventRoom _instance) {
            logger.info(_instance.event.getClass().getName());
            AbstractEventPatch.waitingToShow = true;
        }
    }
}