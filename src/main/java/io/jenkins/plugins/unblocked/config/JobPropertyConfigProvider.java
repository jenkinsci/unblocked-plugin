package io.jenkins.plugins.unblocked.config;

import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import hudson.Extension;
import hudson.model.Run;
import io.jenkins.plugins.unblocked.UnblockedConfig;
import io.jenkins.plugins.unblocked.properties.UnblockedJobProperty;

@Extension
public class JobPropertyConfigProvider implements UnblockedConfigProvider {
    @Override
    public @Nullable UnblockedConfig getUnblockedConfig(@NonNull Run<?, ?> run) {
        var prop = run.getParent().getProperty(UnblockedJobProperty.class);
        if (prop != null) {
            return prop.getConfig();
        }
        return null;
    }
}
