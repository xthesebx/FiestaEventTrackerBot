package Discord.commands;

import Discord.Main;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class StartCommand extends AdminCommands {
	public StartCommand (MessageReceivedEvent event) throws NotAdminException {
		super(event);
		Main.running = true;
		if (!Main.debug)
			Main.jda.getPresence().setActivity(Activity.customStatus("Tracking Fiesta Events on Khazul"));
	}
}
