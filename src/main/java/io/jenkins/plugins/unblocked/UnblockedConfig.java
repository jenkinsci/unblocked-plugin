package io.jenkins.plugins.unblocked;

import hudson.util.FormValidation;
import io.jenkins.plugins.unblocked.utils.Urls;
import org.kohsuke.stapler.QueryParameter;

public class UnblockedConfig {

    private String baseUrl;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = doNormalizeBaseUrl(baseUrl);
    }

    public static String doNormalizeBaseUrl(String baseUrl) {
        return baseUrl == null || baseUrl.isBlank() ? null : baseUrl;
    }

    public static FormValidation doCheckBaseUrl(@QueryParameter String value) {
        if (value == null || value.isBlank() || Urls.isValid(value)) {
            return FormValidation.ok();
        }
        return FormValidation.error("Invalid URL");
    }
}
