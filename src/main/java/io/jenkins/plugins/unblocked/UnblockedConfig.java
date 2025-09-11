package io.jenkins.plugins.unblocked;

import hudson.Extension;
import hudson.model.Describable;
import hudson.model.Descriptor;
import hudson.security.Permission;
import hudson.util.FormValidation;
import io.jenkins.plugins.unblocked.utils.Urls;
import java.util.Objects;
import jenkins.model.Jenkins;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.verb.POST;

public class UnblockedConfig implements Describable<UnblockedConfig> {

    private String baseUrl;
    private String signature;

    @DataBoundConstructor
    public UnblockedConfig() {}

    public String getBaseUrl() {
        return baseUrl;
    }

    @DataBoundSetter
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = doNormalizeBaseUrl(baseUrl);
    }

    public static String doNormalizeBaseUrl(String baseUrl) {
        return baseUrl == null || baseUrl.isBlank() ? null : baseUrl;
    }

    @POST
    public static FormValidation doCheckBaseUrl(@QueryParameter String value) {
        Jenkins.get().checkPermission(Permission.CONFIGURE);
        if (value == null || value.isBlank() || Urls.isValid(value)) {
            return FormValidation.ok();
        }
        return FormValidation.error("Invalid URL");
    }

    public String getSignature() {
        return signature;
    }

    @DataBoundSetter
    public void setSignature(String signature) {
        this.signature = doNormalizeSignature(signature);
    }

    public static String doNormalizeSignature(String signature) {
        return Objects.requireNonNull(signature, "Missing required signature");
    }

    @POST
    public static FormValidation doCheckSignature(@QueryParameter String value) {
        Jenkins.get().checkPermission(Permission.CONFIGURE);
        if (value == null) {
            return FormValidation.error("Signature is required");
        }
        return FormValidation.ok();
    }

    @Override
    public Descriptor<UnblockedConfig> getDescriptor() {
        return new DescriptorImpl();
    }

    @Extension
    public static final class DescriptorImpl extends Descriptor<UnblockedConfig> {

        @Override
        public String getDisplayName() {
            return "Unblocked Configuration";
        }

        @POST
        public FormValidation doCheckBaseUrl(@QueryParameter String value) {
            Jenkins.get().checkPermission(Permission.CONFIGURE);
            return UnblockedConfig.doCheckBaseUrl(value);
        }

        @POST
        public FormValidation doCheckSignature(@QueryParameter String value) {
            Jenkins.get().checkPermission(Permission.CONFIGURE);
            return UnblockedConfig.doCheckSignature(value);
        }
    }
}
