package Discord.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import com.hawolt.logger.Logger;

public class CreditsCommand extends BasicCommand {
	public CreditsCommand (SlashCommandInteractionEvent event) {
		super(event);
		Logger.info("printing credits");
		sendMessage("credits");
	}
}
