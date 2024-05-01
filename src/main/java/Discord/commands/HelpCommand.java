package Discord.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import com.hawolt.logger.Logger;

public class HelpCommand extends BasicCommand{
	public HelpCommand (SlashCommandInteractionEvent event) {
		super(event);
		Logger.info("command list");
		sendMessage("commands");
	}
}
