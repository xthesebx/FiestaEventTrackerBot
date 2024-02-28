package Discord.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import com.hawolt.logger.Logger;

public class HelpCommand extends BasicCommand{
	public HelpCommand (MessageReceivedEvent event) {
		super(event);
		Logger.info("command list");
		sendMessage("commands");
	}
}
