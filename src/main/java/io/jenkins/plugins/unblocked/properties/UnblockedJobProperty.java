package io.jenkins.plugins.unblocked.properties;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.model.Job;
import io.jenkins.plugins.unblocked.UnblockedConfig;
import jenkins.model.OptionalJobProperty;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

public class UnblockedJobProperty extends OptionalJobProperty<Job<?, ?>> {

    private UnblockedConfig config = new UnblockedConfig();

    @DataBoundConstructor
    public UnblockedJobProperty() {}

    public UnblockedConfig getConfig() {
        return config;
    }

    @DataBoundSetter
    public void setConfig(UnblockedConfig config) {
        this.config = config;
    }

    @Extension
    public static final class DescriptorImpl extends OptionalJobProperty.OptionalJobPropertyDescriptor {

        @NonNull
        @Override
        public String getDisplayName() {
            return "Unblocked job settings";
        }

        @Override
        public boolean isApplicable(Class<? extends Job> jobType) {
            return true;
        }
    }
}
