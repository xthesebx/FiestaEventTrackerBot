package Discord.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import com.hawolt.logger.Logger;

public class CreditsCommand extends BasicCommand {
	public CreditsCommand (MessageReceivedEvent event) {
		super(event);
		Logger.info("printing credits");
		sendMessage("credits");
	}
}
