package io.jenkins.plugins.unblocked.config;

import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import hudson.ExtensionPoint;
import hudson.model.Run;
import io.jenkins.plugins.unblocked.UnblockedConfig;

public interface UnblockedConfigProvider extends ExtensionPoint {
    @Nullable
    UnblockedConfig getUnblockedConfig(@NonNull Run<?, ?> run);
}
