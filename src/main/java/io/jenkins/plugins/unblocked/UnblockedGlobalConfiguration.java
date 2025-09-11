package io.jenkins.plugins.unblocked;

import hudson.Extension;
import jenkins.model.GlobalConfiguration;
import org.kohsuke.stapler.DataBoundSetter;

@Extension
public class UnblockedGlobalConfiguration extends GlobalConfiguration {

    private UnblockedConfig config = new UnblockedConfig();

    public UnblockedGlobalConfiguration() {
        load();
    }

    public static UnblockedConfig get() {
        var globalConfig = GlobalConfiguration.all().get(UnblockedGlobalConfiguration.class);
        if (globalConfig == null) {
            return new UnblockedConfig();
        }
        return globalConfig.getConfig();
    }

    public UnblockedConfig getConfig() {
        return config;
    }

    @DataBoundSetter
    public void setConfig(UnblockedConfig config) {
        this.config = config;
        save();
    }
}
