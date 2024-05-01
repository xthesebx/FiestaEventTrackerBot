package Discord.commands;

import Discord.Main;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import com.hawolt.logger.Logger;

public class UnbindCommand extends BasicCommand{
	public UnbindCommand (SlashCommandInteractionEvent event) {
		super(event);
		Logger.info("unbinding");
		Main.removeBind(gid);
		sendMessage("unbinding");
	}
}
