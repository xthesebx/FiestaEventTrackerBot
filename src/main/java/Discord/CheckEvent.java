package Discord;

import com.hawolt.logger.Logger;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.utils.FileUpload;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.time.LocalDateTime;

public class CheckEvent extends Thread {
	EventType currentEvent;
	JDA jda;
	JSONObject binds, pics, lang;
	Main main;
	String currentMessage = "";
	boolean running = true;
	boolean debug, init;
	public CheckEvent(JDA jda, JSONObject binds, JSONObject pics, Main main, JSONObject lang, boolean debug) {
		this.jda = jda;
		this.binds = binds;
		this.pics = pics;
		this.main = main;
		this.lang = lang;
		this.debug = debug;
	}
	@Override
	public void run() {
		super.run();
		Logger.error("run Checkevent");
		while (true) {
			if (running) checkForEvent();
			try {
				Thread.sleep(60000);
			} catch (InterruptedException ignored){
			}
		}
	}
	
	void checkForEvent() {
		try {
			main.readPics();
			Robot robot = new Robot();
			Rectangle rect = new Rectangle();
			rect.setBounds(10, 10, 930, 748);
			File file = new File("image.png");
			ImageIO.write(robot.createScreenCapture(rect), "png", file);
			BufferedImage biA = ImageIO.read(file);
			File dir = new File("eventimages");
			FileFor:
			for (File f : dir.listFiles()) {
				BufferedImage biB = ImageIO.read(f);
				if (biA.getWidth() == biB.getWidth() && biA.getHeight() == biB.getHeight()) {
					for (int i = 0; i < biA.getWidth(); i++) {
						for (int j = 0; j < biA.getHeight(); j++) {
							if (biA.getRGB(i, j) != biB.getRGB(i, j)) {
								if (!debug)
									continue FileFor;
							}
						}
					}
					Logger.info("gleiche bilder");
					Logger.info(f.getName());
					if (pics.has(f.getName())) {
						if (f.getName().contains("Disconnect")) {
							jda.getTextChannelById("1178712493934252157").sendMessage("<@277064996264083456> " + pics.getString(f.getName())).queue();
							return;
						}
						EventType temp = currentEvent;
						switch (f.getName()) {
							case "ApoEvent.png": {
								currentEvent = EventType.APO;
								break;
							}
							case "TevaEvent.png": {
								currentEvent = EventType.TEVA;
								break;
							}
							case "CypianEvent.png": {
								currentEvent = EventType.CYPIAN;
								break;
							}
							case "ElgaEvent.png": {
								currentEvent = EventType.ELGA;
								break;
							}
							default: {
								currentEvent = EventType.NONE;
								break;
							}
						}
						String newMessage = pics.getString(f.getName());
						if (debug) currentEvent = EventType.APO;
						if (currentEvent == EventType.NONE && !f.getName().equals("NoEvent.png")) {
							jda.getTextChannelById("1178712493934252157").sendMessage(newMessage).queue();
						} else {
							if (temp != currentEvent) {
								for (String j : binds.keySet()) {
									if (binds.getJSONObject(j).has("bind")) {
										String s = binds.getJSONObject(j).getString("bind");
										if (debug) s = "1178712493934252157";
										TextChannel channel = jda.getTextChannelById(s);
										try {
											String message = "";
											String language = "englisch";
											if (binds.getJSONObject(j).has("lang")) language = binds.getJSONObject(j).getString("lang");
											message = lang.getJSONObject(language).getString(currentEvent.toString());
											
											if (!init) {
												String latestMessage = channel.getLatestMessageId();
												MessageHistory history = channel.getHistoryAround(latestMessage, 1).complete();
												if (message.equals(history.getMessageById(latestMessage).getContentRaw())) return;
												if (currentEvent.equals(EventType.NONE)) message = lang.getJSONObject(binds.getJSONObject(j).getString("lang")).getString("restartnone").replace("{}", currentEvent.toString());
												else message = lang.getJSONObject(binds.getJSONObject(j).getString("lang")).getString("restart").replace("{}", currentEvent.toString());
												init = true;
											}
											channel.sendMessage(message).queue();
										} catch (NullPointerException e) {
											Logger.info("unbound guild" + e);
										}
									}
								}
							}
							currentMessage = newMessage;
						}
						return;
					}
					break;
				} else{
					continue;
				}
			}
			File unknownF = new File(System.getProperty("user.home") + "\\Pictures\\" + LocalDateTime.now().getHour() + LocalDateTime.now().getMinute() + LocalDateTime.now().getSecond() + "Unknown_image.png");
			ImageIO.write(biA, "png", unknownF);
			Logger.error("unkown type");
			jda.getTextChannelById("1178712493934252157").sendMessage("<@277064996264083456> " + "IDK PLS HELP").addFiles(FileUpload.fromData(unknownF)).queue();
		} catch (Exception e) {
			Logger.error(e);
		}
	}
}
