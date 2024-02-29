package Discord;

import com.hawolt.logger.Logger;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.utils.FileUpload;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.time.LocalDateTime;
import java.util.Random;

public class CheckEvent extends Thread {
	public EventType currentEvent;
	JDA jda;
	JSONObject binds, pics, lang;
	Main main;
	String currentMessage = "";
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
		while (true) {
			if (Main.running) checkForEvent();
			try {
				Thread.sleep(60000);
			} catch (InterruptedException ignored){
			}
		}
	}
	
	void checkForEvent() {
		String name = comparePics();
		if (name.isEmpty()) return;
		if (pics.has(name)) {
			if (name.contains("Disconnect")) {
				jda.getTextChannelById("1178712493934252157").sendMessage("<@277064996264083456> " + pics.getString(name)).queue();
				return;
			}
			EventType temp = currentEvent;
			getEventType(name);
			String newMessage = pics.getString(name);
			if (debug) {
				Random rng = new Random();
				switch(rng.nextInt(0, 3)) {
					case 0: {
						currentEvent = EventType.APO;
						break;
					}
					case 1: {
						currentEvent = EventType.CYPIAN;
						break;
					}
					case 2: {
						currentEvent = EventType.TEVA;
						break;
					}
					case 3: {
						currentEvent = EventType.NONE;
						break;
					}
				}
			}
			if (temp != currentEvent) {
			Logger.info(currentEvent);
			if (!debug && currentEvent == EventType.NONE && !name.equals("NoEvent.png")) {
				jda.getTextChannelById("1178712493934252157").sendMessage(newMessage).queue();
			} else {
					if (!init) {
						if (currentEvent.equals(EventType.NONE)) Main.sendToAllLangDependend("restartnone");
						else Main.sendToAllLangDependend("restart", currentEvent.toString());
						init = true;
					} else {
						Main.sendToAllLangDependend(currentEvent.toString());
					}
				}
				currentMessage = newMessage;
			}
			return;
		}
	}

	public String comparePics() {
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
					return f.getName();
				} else{
					continue;
				}
			}
			File unknownF = new File(System.getProperty("user.home") + "\\Pictures\\" + LocalDateTime.now().getHour() + LocalDateTime.now().getMinute() + LocalDateTime.now().getSecond() + "Unknown_image.png");
			ImageIO.write(biA, "png", unknownF);
			Logger.error("unkown type");
			jda.getTextChannelById("1178712493934252157").sendMessage("<@277064996264083456> " + "IDK PLS HELP").addFiles(FileUpload.fromData(unknownF)).queue();
		} catch (IOException | AWTException e) {
		
		}
		return "";
	}

	private void getEventType(String name) {
		switch (name) {
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
			default: {
				currentEvent = EventType.NONE;
				break;
			}
		}
	}
}
