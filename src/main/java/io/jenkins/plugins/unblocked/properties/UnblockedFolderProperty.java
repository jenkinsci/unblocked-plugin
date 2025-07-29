package io.jenkins.plugins.unblocked.properties;

import com.cloudbees.hudson.plugins.folder.AbstractFolder;
import com.cloudbees.hudson.plugins.folder.AbstractFolderProperty;
import com.cloudbees.hudson.plugins.folder.AbstractFolderPropertyDescriptor;
import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.util.FormValidation;
import io.jenkins.plugins.unblocked.UnblockedConfig;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;

public class UnblockedFolderProperty extends AbstractFolderProperty<AbstractFolder<?>> {

    private final UnblockedConfig config = new UnblockedConfig();

    @DataBoundConstructor
    public UnblockedFolderProperty() {}

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

    @Extension
    public static final class DescriptorImpl extends AbstractFolderPropertyDescriptor {

        @NonNull
        @Override
        public String getDisplayName() {
            return "Unblocked folder settings";
        }

        public FormValidation doCheckBaseUrl(@QueryParameter String value) {
            return UnblockedConfig.doCheckBaseUrl(value);
        }
    }
}
