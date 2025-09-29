package io.jenkins.plugins.unblocked.notify;

import static io.jenkins.plugins.unblocked.notify.NotifyExecution.extractSignature;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.model.listeners.RunListener;
import hudson.util.Secret;

@Extension
public class NotifyListener extends RunListener<Run<?, ?>> {

    @Override
    public void onCompleted(@NonNull Run<?, ?> run, @NonNull TaskListener listener) {
        final String baseUrl = null;
        final Secret signature = extractSignature(run);

        Notifier.submit(baseUrl, signature, run);
    }
}
