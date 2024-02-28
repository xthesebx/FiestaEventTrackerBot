package Discord.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import static java.lang.System.exit;

public class KillCommand extends AdminCommands {
	public KillCommand (MessageReceivedEvent event) throws NotAdminException {
		super(event);
		exit(0);
	}
}
