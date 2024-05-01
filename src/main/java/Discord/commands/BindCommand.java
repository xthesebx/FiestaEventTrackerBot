package Discord.commands;

import Discord.Main;
import com.hawolt.logger.Logger;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.json.JSONObject;

public class BindCommand extends BasicCommand{
	public BindCommand (SlashCommandInteractionEvent event) {
		super(event);
		Logger.info("binding");
		if (!Main.bindsObject.has(gid)) Main.bindsObject.put(gid, new JSONObject());
		Main.bindsObject.getJSONObject(gid).put("bind", cid);
		Main.writeBinds();
		sendMessage("binding");
	}
}
