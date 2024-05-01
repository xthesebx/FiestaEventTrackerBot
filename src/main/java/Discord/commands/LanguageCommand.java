package Discord.commands;

import Discord.Main;
import com.hawolt.logger.Logger;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.json.JSONObject;


public class LanguageCommand extends BasicCommand{
	public LanguageCommand (SlashCommandInteractionEvent event) {
		super(event);
		OptionMapping options = event.getOption("language");
		assert options != null;
		String languageString = options.getAsString();
		Logger.error(languageString);
		if (!languageString.equals("deutsch") && !languageString.equals("english") && !languageString.equals("debug")) {
			sendMessage("unknownLang");
			return;
		}
		if (!Main.bindsObject.has(gid)) Main.bindsObject.put(gid, new JSONObject());
		Main.bindsObject.getJSONObject(gid).put("lang", languageString);
		Main.writeBinds();
		guildLang = Main.bindsObject.getJSONObject(gid).getString("lang");
		guildLangObject = Main.lang.getJSONObject(guildLang);
		sendMessage("langchange");
	}
}
