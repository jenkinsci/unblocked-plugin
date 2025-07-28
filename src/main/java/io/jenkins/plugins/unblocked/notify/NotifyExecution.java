package io.jenkins.plugins.unblocked.notify;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.model.Run;
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
            Notifier.submit(baseUrl, run);
        }
        return null;
    }
}
