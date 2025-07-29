package io.jenkins.plugins.unblocked;

import hudson.Extension;
import hudson.util.FormValidation;
import io.jenkins.plugins.unblocked.utils.Urls;
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
        if (value == null || value.isBlank() || Urls.isValid(value)) {
            return FormValidation.ok();
        }
        return FormValidation.error("Invalid URL");
    }
}
