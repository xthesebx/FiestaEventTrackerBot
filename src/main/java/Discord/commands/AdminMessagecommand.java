package Discord.commands;

import Discord.Main;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class AdminMessagecommand extends AdminCommands {
	public AdminMessagecommand (MessageReceivedEvent event, String s) throws NotAdminException {
		super(event);
		Main.sendToAll(s.substring(s.indexOf(" ") + 1));
	}
}
