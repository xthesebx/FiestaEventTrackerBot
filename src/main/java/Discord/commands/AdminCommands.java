package Discord.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class AdminCommands extends BasicCommand {
	public AdminCommands (MessageReceivedEvent event) throws NotAdminException {
		super(event);
		if (!event.getAuthor().getId().equals("277064996264083456")) throw new NotAdminException();
	}
}
