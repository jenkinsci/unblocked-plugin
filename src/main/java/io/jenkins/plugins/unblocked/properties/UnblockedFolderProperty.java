package io.jenkins.plugins.unblocked.properties;

import com.cloudbees.hudson.plugins.folder.AbstractFolder;
import com.cloudbees.hudson.plugins.folder.AbstractFolderProperty;
import com.cloudbees.hudson.plugins.folder.AbstractFolderPropertyDescriptor;
import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import io.jenkins.plugins.unblocked.UnblockedConfig;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

public class UnblockedFolderProperty extends AbstractFolderProperty<AbstractFolder<?>> {

    private UnblockedConfig config = new UnblockedConfig();

    @DataBoundConstructor
    public UnblockedFolderProperty() {}

    public UnblockedConfig getConfig() {
        return config;
    }

    @DataBoundSetter
    public void setConfig(UnblockedConfig config) {
        this.config = config;
    }

    @Extension
    public static final class DescriptorImpl extends AbstractFolderPropertyDescriptor {

        @NonNull
        @Override
        public String getDisplayName() {
            return "Unblocked folder settings";
        }
    }
}
