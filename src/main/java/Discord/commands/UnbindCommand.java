package Discord.commands;

import Discord.Main;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import com.hawolt.logger.Logger;

public class UnbindCommand extends BasicCommand{
	public UnbindCommand (MessageReceivedEvent event) {
		super(event);
		Logger.info("unbinding");
		Main.removeBind(event.getGuild().getId());
		sendMessage("unbinding");
	}
}
