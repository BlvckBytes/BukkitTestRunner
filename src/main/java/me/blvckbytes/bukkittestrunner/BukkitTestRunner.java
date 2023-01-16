package me.blvckbytes.bukkittestrunner;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

@org.bukkit.plugin.java.annotation.plugin.Plugin(name = "BukkitTestRunner", version = "0.1")
public class BukkitTestRunner extends JavaPlugin {

  @Override
  public void onEnable() {
    // When the first tick elapsed, all plugins should be loaded
    Bukkit.getScheduler().runTaskLater(this, this::bootstrapTesting, 1);
  }

  private void bootstrapTesting() {
    try {
      File reportFolder = getReportsFolder();
      int runCounter = 0;

      for (Plugin plugin : getServer().getPluginManager().getPlugins()) {
        if (!(plugin instanceof ITestable))
          continue;

        runTestsForPlugin(plugin.getName(), (ITestable) plugin, reportFolder);
        runCounter++;
      }

      log("Ran " + runCounter + " plugins' tests, shutting down now.");
      Bukkit.getServer().shutdown();
    } catch (Exception e) {
      e.printStackTrace();
      Bukkit.getPluginManager().disablePlugin(this);
    }
  }

  private File getReportsFolder() {
    File dataFolder = getDataFolder();

    if (!dataFolder.exists()) {
      if (!dataFolder.mkdirs())
        throw new IllegalStateException("Could not create the data folder");
    }

    File reportsFolder = new File(dataFolder, "reports");

    if (!reportsFolder.exists()) {
      if (!reportsFolder.mkdirs())
        throw new IllegalStateException("Could not create the reports folder");
    }

    return reportsFolder;
  }

  private void runTestsForPlugin(String name, ITestable testable, File reportsFolder) throws Exception {
    File reportsFile = new File(reportsFolder, name + ".txt");

    log("Running tests for plugin '" + name + "', saving reports to '" + reportsFile + "'");

    try (
      FileOutputStream fos = new FileOutputStream(reportsFile);
      PrintStream ps = new PrintStream(fos);
    ) {
      try {
        JUnitCore engine = new JUnitCore();
        engine.addListener(new TextListener(ps));
        engine.run(testable.getTestClasses());
      } catch (Exception e) {
        ps.println("An error occurred while trying to run the tests:");
        e.printStackTrace(ps);
      }
    }
  }

  private void log(String message) {
    Bukkit.getConsoleSender().sendMessage("§8[§5BukkitTestRunner§8] §d" + message);
  }
}
