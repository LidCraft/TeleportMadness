package com.liddev.teleportmadness;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.FileUtil;

/**
 *
 * @author Renlar <liddev.com>
 */
public class FileManager {

    private final String configFileName = "config.yml";
    private final String commandFileName = "commands.yml";
    private final File configFile;
    private final File commandFile;
    private YamlConfiguration config;
    private YamlConfiguration commands;
    private final TeleportMadness mad;

    public FileManager(TeleportMadness mad) {
        this.mad = mad;
        if (!mad.getDataFolder().exists()) {
            mad.getDataFolder().mkdir();
        }
        configFile = new File(mad.getDataFolder(), configFileName);
        commandFile = new File(mad.getDataFolder(), commandFileName);
    }

    public void loadConfig() {
        if (!configFile.exists()) {
            copy(mad.getResource(configFileName), configFile);
        }
        config = new YamlConfiguration();
        config.options().pathSeparator('/');
        try {
            config.load(configFile);
        } catch (IOException ex) {
            IOExceptionHandler(ex, configFileName);
        } catch (InvalidConfigurationException ex) {
            InvalidConfigExceptionHandler(ex, configFileName);
        }
    }

    public YamlConfiguration getConfig() {
        if (config == null) {
            loadConfig();
        }
        return config;
    }

    public void saveConfig() {
        if (config == null) {
            mad.getLogger().log(Level.SEVERE, "Config save error: Config file is not loaded, cannot save null file.");
        }
        try {
            config.save(configFile);
        } catch (IOException ex) {
            mad.getLogger().log(Level.SEVERE, "Config save error: Unable to save config file.", ex);
        }
    }

    public void loadCommands() {
        if (!commandFile.exists()) {
            copy(mad.getResource(commandFileName), commandFile);
        }
        commands = new YamlConfiguration();
        commands.options().pathSeparator('/');
        try {
            commands.load(commandFile);
        } catch (IOException ex) {
            IOExceptionHandler(ex, commandFileName);
        } catch (InvalidConfigurationException ex) {
            InvalidConfigExceptionHandler(ex, commandFileName);
        }
    }

    public YamlConfiguration getCommands() {
        if (commands == null) {
            loadCommands();
        }
        return commands;
    }

    private void copy(InputStream in, File file) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
        } catch (IOException ex) {
            mad.getLogger().log(Level.SEVERE, "Failed to copy {0} to plugin directory.\nPlease ensure bukkit has permission to read and write to all files in the plugin directory.\nError Message:\n{1}", new Object[]{file, ex});
        }
    }

    private void IOExceptionHandler(IOException ex, String file) {
        mad.getLogger().log(Level.SEVERE, "Failed to load configuration. Does bukkit have permission to read and write {0}?\nError Message:\n{1}", new Object[]{file, ex});
    }

    private void InvalidConfigExceptionHandler(InvalidConfigurationException ex, String file) {

        //Provide as clear an error message as possible.
        //get line numbers from the error message.
        ArrayList<String> lines = new ArrayList<String>();
        Pattern pattern = Pattern.compile("line (\\d+), column");
        Matcher matcher = pattern.matcher(ex.getMessage());
        while (matcher.find()) {
            String lineNo = matcher.group(1);
            if (!lines.contains(lineNo)) {
                lines.add(lineNo);
            }
        }

        StringBuilder msg = new StringBuilder(file + ", is invalid! ");
        //TODO: attempt to repair the config files and reload, Fix: using tabs instead of spaces, invaid spacing such as "mad:job" instead of the proper "mad: job"
        if (lines.isEmpty()) {
            msg.append("\nUnable to find any line numbers.");
        } else {
            msg.append("\nThe following lines have errors: ").append(lines.get(0));
            for (String lineNum : lines.subList(1, lines.size())) {
                msg.append(", ").append(lineNum);
            }
        }
        mad.getLogger().severe(msg.toString());

        // save the full error in "file"_error.txt
        try {
            File outFile = new File(mad.getDataFolder(), file + "_error.txt");
            PrintStream out = new PrintStream(new FileOutputStream(outFile));
            out.println(ex.toString());
            out.close();
            mad.getLogger().log(Level.INFO, "Saved the full error message in {0}", outFile);
        } catch (FileNotFoundException exfne) {
            mad.getLogger().severe("Failed to save the error message!");
        }

        // save a backup
        File backupFile = new File(mad.getDataFolder(), file + "_backup.yml");
        File sourceFile = new File(mad.getDataFolder(), file);
        if (FileUtil.copy(sourceFile, backupFile)) {
            mad.getLogger().log(Level.INFO, "Saved a backup of " + file + " to {0}", backupFile);
        } else {
            mad.getLogger().severe("Failed to save a configuration backup!");
        }
    }
}
