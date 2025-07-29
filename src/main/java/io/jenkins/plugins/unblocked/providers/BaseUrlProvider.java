package io.jenkins.plugins.unblocked.providers;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.ExtensionPoint;
import hudson.model.Run;
import javax.annotation.Nullable;

public interface BaseUrlProvider extends ExtensionPoint {
    @Nullable
    String getBaseUrl(@NonNull Run<?, ?> run);
}
