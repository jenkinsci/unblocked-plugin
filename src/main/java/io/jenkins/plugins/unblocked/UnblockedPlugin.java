package io.jenkins.plugins.unblocked;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.PluginWrapper;
import java.util.Objects;
import jenkins.model.Jenkins;

public record UnblockedPlugin(PluginWrapper plugin) {

    @NonNull
    public static UnblockedPlugin get() {
        var plugin = Jenkins.get().getPluginManager().getPlugin("unblocked");
        Objects.requireNonNull(plugin, "Unblocked plugin not found");

        return new UnblockedPlugin(plugin);
    }

    public String getVersion() {
        return plugin.getVersion();
    }

    public boolean isSnapshot() {
        return getVersion().endsWith("-SNAPSHOT");
    }
}
