package Discord.commands;

import Discord.EventType;
import Discord.Main;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import com.hawolt.logger.Logger;

public class EventCommand extends BasicCommand{
	public EventCommand (SlashCommandInteractionEvent event) {
		super(event);
		Logger.info("printing current event");
		if (Main.eventCheck.currentEvent == null || Main.eventCheck.currentEvent.equals(EventType.NONE)) sendMessage("noeventrn");
		else sendMessage("eventrn", Main.eventCheck.currentEvent.toString());
	}
}
