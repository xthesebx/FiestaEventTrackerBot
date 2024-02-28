package Discord.commands;

import Discord.EventType;
import Discord.Main;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import com.hawolt.logger.Logger;

public class EventCommand extends BasicCommand{
	public EventCommand (MessageReceivedEvent event) {
		super(event);
		Logger.info("printing current event");
		if (Main.eventCheck.currentEvent.equals(EventType.NONE)) sendMessage("noeventrn");
		else sendMessage("eventrn", Main.eventCheck.currentEvent.toString());
	}
}
