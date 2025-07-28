package io.jenkins.plugins.unblocked.notify;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.model.Run;
import io.jenkins.plugins.unblocked.UnblockedGlobalConfiguration;
import java.util.Collections;
import java.util.Set;
import javax.annotation.Nullable;
import org.jenkinsci.plugins.workflow.steps.Step;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepDescriptor;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

public class NotifyStep extends Step {

    @DataBoundConstructor
    public NotifyStep() {}

    @Nullable
    private String baseUrl;

    @DataBoundSetter
    public void setBaseUrl(@Nullable String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getBaseUrl() {
        return baseUrl != null ? baseUrl : UnblockedGlobalConfiguration.get().getBaseUrl();
    }

    @Override
    public StepExecution start(StepContext context) throws Exception {
        final var baseUrl = getBaseUrl();
        return new NotifyExecution(context, baseUrl);
    }

    @Extension
    public static class NotifyStepDescriptor extends StepDescriptor {

        @Override
        public String getFunctionName() {
            return "unblockedNotify";
        }

        @NonNull
        @Override
        public String getDisplayName() {
            return "Send notification to Unblocked";
        }

        @Override
        public Set<? extends Class<?>> getRequiredContext() {
            return Collections.singleton(Run.class);
        }
    }
}
