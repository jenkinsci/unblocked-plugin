package io.jenkins.plugins.unblocked;

import hudson.Extension;
import hudson.model.Describable;
import hudson.model.Descriptor;
import hudson.security.Permission;
import hudson.util.FormValidation;
import hudson.util.Secret;
import io.jenkins.plugins.unblocked.utils.Urls;
import jenkins.model.Jenkins;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.verb.POST;

public class UnblockedConfig implements Describable<UnblockedConfig> {

    private Secret signature;
    private boolean disabled;

    @DataBoundConstructor
    public UnblockedConfig() {}

    public Secret getSignature() {
        return signature;
    }

    @DataBoundSetter
    public void setSignature(Secret signature) {
        this.signature = signature;
    }

    public boolean isDisabled() {
        return disabled;
    }

    @DataBoundSetter
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl) Jenkins.get().getDescriptorOrDie(getClass());
    }

    @Extension
    public static final class DescriptorImpl extends Descriptor<UnblockedConfig> {

        @POST
        public FormValidation doCheckBaseUrl(@QueryParameter String value) {
            Jenkins.get().checkPermission(Permission.CONFIGURE);
            if (value == null || value.isBlank() || Urls.isValid(value)) {
                return FormValidation.ok();
            }
            return FormValidation.error("Invalid URL");
        }

        @POST
        public FormValidation doCheckSignature(@QueryParameter String value, @QueryParameter boolean disabled) {
            Jenkins.get().checkPermission(Permission.CONFIGURE);
            if (value == null && !disabled) {
                return FormValidation.error("Signature is required");
            }
            return FormValidation.ok();
        }
    }
}
