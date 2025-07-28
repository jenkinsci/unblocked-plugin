package io.jenkins.plugins.unblocked;

import hudson.Extension;
import hudson.util.FormValidation;
import jenkins.model.GlobalConfiguration;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;

@Extension
public class UnblockedGlobalConfiguration extends GlobalConfiguration {

    private String baseUrl;

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
        this.baseUrl = baseUrl;
        save();
    }

    public FormValidation doCheckBaseUrl(@QueryParameter String value) {
        return FormValidation.validateRequired(value);
    }
}
