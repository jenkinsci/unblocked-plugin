package io.jenkins.plugins.unblocked.providers;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.model.Run;
import io.jenkins.plugins.unblocked.UnblockedJobProperty;
import javax.annotation.Nullable;

public final class BaseUrlProviders {

    @Extension
    public static class JobPropertyBaseUrl implements BaseUrlProvider {
        @Override
        public @Nullable String getBaseUrl(@NonNull Run<?, ?> run) {
            var prop = run.getParent().getProperty(UnblockedJobProperty.class);
            if (prop != null) {
                var value = prop.getBaseUrl();
                if (value != null) {
                    return value;
                }
            }
            return null;
        }
    }
}
