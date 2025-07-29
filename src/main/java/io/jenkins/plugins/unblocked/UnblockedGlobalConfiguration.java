package io.jenkins.plugins.unblocked;

import hudson.Extension;
import hudson.util.FormValidation;
import jenkins.model.GlobalConfiguration;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;

@Extension
public class UnblockedGlobalConfiguration extends GlobalConfiguration {

    private String baseUrl;
    private String signature;

    public UnblockedGlobalConfiguration() {
        load();
    }

    public static UnblockedGlobalConfiguration get() {
        return GlobalConfiguration.all().get(UnblockedGlobalConfiguration.class);
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    @DataBoundSetter
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = UnblockedConfig.doNormalizeBaseUrl(baseUrl);
        save();
    }

    public FormValidation doCheckBaseUrl(@QueryParameter String value) {
        return UnblockedConfig.doCheckBaseUrl(value);
    }

    public String getSignature() {
        return signature;
    }

    @DataBoundSetter
    public void setSignature(String signature) {
        this.signature = UnblockedConfig.doNormalizeSignature(signature);
        save();
    }

    public FormValidation doCheckSignature(@QueryParameter String value) {
        return UnblockedConfig.doCheckSignature(value);
    }
}
