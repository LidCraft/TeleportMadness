package com.liddev.teleportmadness;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
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
class Configurator {
    
    private File configFile;
    private YamlConfiguration config;
    private TeleportMadness mad;
    private boolean configLoadError;
    
    public Configurator(TeleportMadness mad){
        this.mad = mad;
    }
    
    public void loadConfig(){
        configFile = new File(mad.getDataFolder(), "config.yml");
        mad.saveDefaultConfig();
        load();
    }
    
    public void load(){
        config = new YamlConfiguration();
        config.options().pathSeparator('/');
        try {
            config.load(configFile);
        } catch (InvalidConfigurationException ex) {
            configLoadError = true;

            // extract line numbers from the exception if we can
            ArrayList<String> lines = new ArrayList<String>();
            Pattern pattern = Pattern.compile("line (\\d+), column");
            Matcher matcher = pattern.matcher(ex.getMessage());
            while (matcher.find()) {
                String lineNo = matcher.group(1);
                if (!lines.contains(lineNo)) {
                    lines.add(lineNo);
                }
            }

            // make a nice message
            String msg = "Your configuration is invalid! ";
            if (lines.size() == 0) {
                msg += "Unable to find any line numbers.";
            } else {
                msg += "Take a look at line(s): " + lines.get(0);
                for (String lineNo : lines.subList(1, lines.size())) {
                    msg += ", " + lineNo;
                }
            }
            mad.getLogger().severe(msg);

            // save the whole error to config_error.txt
            try {
                File outFile = new File(mad.getDataFolder(), "config_error.txt");
                PrintStream out = new PrintStream(new FileOutputStream(outFile));
                out.println("Use the following website to help you find and fix configuration errors:");
                out.println("https://yaml-online-parser.appspot.com/");
                out.println();
                out.println(ex.toString());
                out.close();
                mad.getLogger().info("Saved the full error message to " + outFile);
            } catch (IOException ex2) {
                mad.getLogger().severe("Failed to save the full error message!");
            }

            // save a backup
            File backupFile = new File(mad.getDataFolder(), "config_backup.yml");
            File sourceFile = new File(mad.getDataFolder(), "config.yml");
            if (FileUtil.copy(sourceFile, backupFile)) {
                mad.getLogger().info("Saved a backup of your configuration to " + backupFile);
            } else {
                mad.getLogger().severe("Failed to save a configuration backup!");
            }
        } catch (Exception ex) {
            mad.getLogger().log(Level.SEVERE, "Failed to load configuration", ex);
        }
    }
}
