package Discord.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class RestartCommand extends BasicCommand {
    public RestartCommand(SlashCommandInteractionEvent event) throws AWTException, IOException, InterruptedException{
        super(event);
        event.reply("ok").queue();
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("cmd.exe", "/c", "taskkill /f /im fiesta.bin");
        processBuilder.start();
        Thread.sleep(10000);
        processBuilder.command("cmd.exe", "/c", "taskkill /f /im glyphclientapp.exe");
        processBuilder.start();
        Thread.sleep(10000);
        processBuilder.command("cmd.exe", "/c", "start C:\\Users\\Administrator\\Desktop\\Glyph.lnk");
        processBuilder.start();
        File file = new File("image.png");
        Robot robot = new Robot();
        do {
            Thread.sleep(30000);
            robot.mouseMove(900, 115);
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            Thread.sleep(60000);
            Rectangle rect = new Rectangle();
            rect.setBounds(875, 75, 150, 45);
            ImageIO.write(robot.createScreenCapture(rect), "png", file);
        } while (compare(file) != null);
        BufferedReader reader = new BufferedReader(new FileReader("login.env"));
        String username = reader.readLine();
        String password = reader.readLine();
        reader.close();
        StringSelection stringSelection = new StringSelection(username);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, stringSelection);
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        Thread.sleep(5000);
        robot.keyPress(KeyEvent.VK_TAB);
        robot.keyRelease(KeyEvent.VK_TAB);
        Thread.sleep(5000);
        StringSelection passwordSelection = new StringSelection(password);
        clipboard.setContents(passwordSelection, passwordSelection);
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        Thread.sleep(5000);
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
        Thread.sleep(10000);
        robot.mouseMove(280, 350);
        Thread.sleep(2000);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        Thread.sleep(2000);
        robot.mouseMove(700, 600);
        Thread.sleep(2000);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        Thread.sleep(10000);
        robot.mouseMove(85, 405);
        Thread.sleep(2000);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        Thread.sleep(1000);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        Thread.sleep(2000);
        robot.mouseMove(715, 725);
        Thread.sleep(2000);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        Thread.sleep(1000);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        Thread.sleep(60000);
        robot.keyPress(KeyEvent.VK_F);
        Thread.sleep(1000);
        robot.keyRelease(KeyEvent.VK_F);
        Thread.sleep(5000);
        robot.mouseMove(115, 630);
        Thread.sleep(2000);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        Thread.sleep(1000);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        Thread.sleep(2000);
        robot.keyPress(KeyEvent.VK_F);
        Thread.sleep(1000);
        robot.keyRelease(KeyEvent.VK_F);
        Thread.sleep(5000);
        robot.keyPress(KeyEvent.VK_U);
        Thread.sleep(1000);
        robot.keyRelease(KeyEvent.VK_U);
        Thread.sleep(5000);
        robot.mouseMove(155, 640);
        Thread.sleep(1000);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        Thread.sleep(1000);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        Thread.sleep(5000);
        robot.mouseMove(1024, 768);
    }

    private String compare(File file) throws IOException {
        BufferedImage biA = ImageIO.read(file);
        File[] dir = new File[2];
        dir[0] = new File("C:\\Users\\Administrator\\Downloads\\eventimages\\update.png");
        dir[1] = new File("C:\\Users\\Administrator\\Downloads\\eventimages\\play.png");
        FileFor:
        for (File f : dir) {
            BufferedImage biB = ImageIO.read(f);
            if (biA.getWidth() == biB.getWidth() && biA.getHeight() == biB.getHeight()) {
                for (int i = 0; i < biA.getWidth(); i++) {
                    for (int j = 0; j < biA.getHeight(); j++) {
                        if (biA.getRGB(i, j) != biB.getRGB(i, j)) {
                            continue FileFor;
                        }
                    }
                }
                return f.getName();
            }
        }
        return null;
    }
}
