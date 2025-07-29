package io.jenkins.plugins.unblocked;

import hudson.util.FormValidation;
import io.jenkins.plugins.unblocked.utils.Urls;
import java.util.Objects;
import org.kohsuke.stapler.QueryParameter;

public class UnblockedConfig {

    private String baseUrl;
    private String signature;

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

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = doNormalizeSignature(signature);
    }

    public static String doNormalizeSignature(String signature) {
        return Objects.requireNonNull(signature, "Missing required signature");
    }

    public static FormValidation doCheckSignature(@QueryParameter String value) {
        if (value == null) {
            return FormValidation.error("Signature is required");
        }
        return FormValidation.ok();
    }
}
