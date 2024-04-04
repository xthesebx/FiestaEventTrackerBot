package Discord.commands;

import Discord.Main;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class StopCommand extends AdminCommands{
	public StopCommand (MessageReceivedEvent event) throws NotAdminException {
		super(event);
		Main.running = false;
		Main.debug = true;
		Main.jda.getPresence().setActivity(Activity.customStatus("Maintenance"));
	}
}
