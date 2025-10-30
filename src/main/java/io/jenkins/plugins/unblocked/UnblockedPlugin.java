package io.jenkins.plugins.unblocked;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.PluginWrapper;
import jenkins.model.Jenkins;

public class UnblockedPlugin {

    private final PluginWrapper plugin;

    public UnblockedPlugin(PluginWrapper plugin) {
        this.plugin = plugin;
    }

    @NonNull
    public static UnblockedPlugin get() {
        var manager = Jenkins.get().getPluginManager();
        if (manager == null) {
            throw new IllegalStateException("Plugin manager not available");
        }
        var plugin = manager.getPlugin("unblocked");
        if (plugin == null) {
            throw new IllegalStateException("Unblocked plugin not found");
        }
        return new UnblockedPlugin(plugin);
    }

    public String getVersion() {
        return plugin.getVersion();
    }
}
