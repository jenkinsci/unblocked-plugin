package io.jenkins.plugins.unblocked.notify;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.ExtensionList;
import hudson.model.Run;
import io.jenkins.plugins.unblocked.UnblockedGlobalConfiguration;
import io.jenkins.plugins.unblocked.config.UnblockedConfigProvider;
import io.jenkins.plugins.unblocked.utils.Urls;
import javax.annotation.Nullable;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.SynchronousNonBlockingStepExecution;

public class NotifyExecution extends SynchronousNonBlockingStepExecution<Void> {

    @Nullable
    private final String baseUrl;

    public NotifyExecution(@NonNull StepContext context, @Nullable String baseUrl) {
        super(context);
        this.baseUrl = baseUrl;
    }

    @Override
    protected Void run() throws Exception {
        final var run = getContext().get(Run.class);
        if (run != null) {
            final var baseUrl = getBaseUrl(run);
            Notifier.submit(baseUrl, run);
        }
        return null;
    }

    private String getBaseUrl(Run<?, ?> run) {
        if (baseUrl != null) {
            if (Urls.isValid(baseUrl)) {
                return baseUrl;
            }
            throw new IllegalArgumentException("Invalid URL: " + baseUrl);
        }

        for (final var provider : ExtensionList.lookup(UnblockedConfigProvider.class)) {
            var config = provider.getUnblockedConfig(run);
            if (config != null) {
                var baseUrl = config.getBaseUrl();
                if (baseUrl != null) {
                    return baseUrl;
                }
            }
        }
        return UnblockedGlobalConfiguration.get().getBaseUrl();
    }
}
