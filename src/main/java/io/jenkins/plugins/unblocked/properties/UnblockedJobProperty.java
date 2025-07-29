package io.jenkins.plugins.unblocked.properties;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.model.Job;
import hudson.util.FormValidation;
import io.jenkins.plugins.unblocked.UnblockedConfig;
import jenkins.model.OptionalJobProperty;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;

public class UnblockedJobProperty extends OptionalJobProperty<Job<?, ?>> {

    private final UnblockedConfig config = new UnblockedConfig();

    @DataBoundConstructor
    public UnblockedJobProperty() {}

    public UnblockedConfig getConfig() {
        return config;
    }

    public String getBaseUrl() {
        return config.getBaseUrl();
    }

    @DataBoundSetter
    public void setBaseUrl(String baseUrl) {
        config.setBaseUrl(baseUrl);
    }

    public String getSignature() {
        return config.getSignature();
    }

    @DataBoundSetter
    public void setSignature(String signature) {
        config.setSignature(signature);
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

        public FormValidation doCheckBaseUrl(@QueryParameter String value) {
            return UnblockedConfig.doCheckBaseUrl(value);
        }

        public FormValidation doCheckSignature(@QueryParameter String value) {
            return UnblockedConfig.doCheckSignature(value);
        }
    }
}
