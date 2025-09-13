package io.jenkins.plugins.unblocked.properties;

import com.cloudbees.hudson.plugins.folder.AbstractFolder;
import com.cloudbees.hudson.plugins.folder.AbstractFolderProperty;
import com.cloudbees.hudson.plugins.folder.AbstractFolderPropertyDescriptor;
import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import io.jenkins.plugins.unblocked.UnblockedConfig;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

public class UnblockedFolderProperty extends AbstractFolderProperty<AbstractFolder<?>> {

    private UnblockedConfig config;
    private transient boolean enabled;

    @DataBoundConstructor
    public UnblockedFolderProperty() {}

    @CheckForNull
    public UnblockedConfig getConfig() {
        return config;
    }

    @DataBoundSetter
    public void setConfig(UnblockedConfig config) {
        this.config = enabled ? config : null;
    }

    public boolean isEnabled() {
        return config != null;
    }

    @DataBoundSetter
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (!enabled) {
            this.config = null;
        }
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
