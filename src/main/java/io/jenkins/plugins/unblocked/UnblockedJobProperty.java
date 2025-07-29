package io.jenkins.plugins.unblocked;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.model.Job;
import hudson.util.FormValidation;
import io.jenkins.plugins.unblocked.utils.Urls;
import jenkins.model.OptionalJobProperty;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;

public class UnblockedJobProperty extends OptionalJobProperty<Job<?, ?>> {

    private String baseUrl;

    @DataBoundConstructor
    public UnblockedJobProperty() {}

    public String getBaseUrl() {
        return baseUrl;
    }

    @DataBoundSetter
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
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
            if (value == null || value.isBlank() || Urls.isValid(value)) {
                return FormValidation.ok();
            }
            return FormValidation.error("Invalid URL");
        }
    }
}
