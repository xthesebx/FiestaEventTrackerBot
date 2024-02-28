package Discord.commands;

import Discord.Main;
import com.hawolt.logger.Logger;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.json.JSONObject;


public class LanguageCommand extends BasicCommand{
	public LanguageCommand (MessageReceivedEvent event, String s) {
		super(event);
		Logger.error(s);
		String languageString = s.substring(s.indexOf(" ") + 1).toLowerCase();
		Logger.error(languageString);
		if (!languageString.equals("deutsch") && !languageString.equals("englisch") && !languageString.equals("debug")) {
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
