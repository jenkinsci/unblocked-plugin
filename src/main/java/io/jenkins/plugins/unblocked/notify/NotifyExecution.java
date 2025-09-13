package io.jenkins.plugins.unblocked.notify;

import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import hudson.ExtensionList;
import hudson.model.Run;
import hudson.util.Secret;
import io.jenkins.plugins.unblocked.UnblockedGlobalConfiguration;
import io.jenkins.plugins.unblocked.config.UnblockedConfigProvider;
import io.jenkins.plugins.unblocked.utils.Urls;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.SynchronousNonBlockingStepExecution;

public class NotifyExecution extends SynchronousNonBlockingStepExecution<Void> {

    @Nullable
    private final String baseUrl;

    @Nullable
    private final Secret signature;

    public NotifyExecution(@NonNull StepContext context, @Nullable String baseUrl, @Nullable Secret signature) {
        super(context);
        this.baseUrl = baseUrl;
        this.signature = signature;
    }

    @Override
    protected Void run() throws Exception {
        final var run = getContext().get(Run.class);
        if (run != null) {
            final var baseUrl = getBaseUrl(run);
            final var signature = getSignature(run);
            Notifier.submit(baseUrl, signature, run);
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

    private Secret getSignature(Run<?, ?> run) {
        if (signature != null) {
            return signature;
        }
        for (final var provider : ExtensionList.lookup(UnblockedConfigProvider.class)) {
            var config = provider.getUnblockedConfig(run);
            if (config != null) {
                var signature = config.getSignature();
                if (signature != null) {
                    return signature;
                }
            }
        }
        return UnblockedGlobalConfiguration.get().getSignature();
    }
}
