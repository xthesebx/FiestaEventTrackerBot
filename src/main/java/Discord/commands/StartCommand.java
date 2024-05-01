package Discord.commands;

import Discord.Main;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class StartCommand extends BasicCommand {
	public StartCommand (SlashCommandInteractionEvent event) {
		super(event);
		Main.running = true;
		Main.debug = false;
		Main.jda.getPresence().setActivity(Activity.customStatus("Tracking Fiesta Events on Khazul"));
		event.reply("ok").queue();
	}
}
