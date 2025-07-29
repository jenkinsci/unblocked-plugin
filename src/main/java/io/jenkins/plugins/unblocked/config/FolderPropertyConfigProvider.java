package io.jenkins.plugins.unblocked.config;

import com.cloudbees.hudson.plugins.folder.AbstractFolder;
import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.model.Run;
import io.jenkins.plugins.unblocked.UnblockedConfig;
import io.jenkins.plugins.unblocked.properties.UnblockedFolderProperty;
import javax.annotation.Nullable;

@Extension
public class FolderPropertyConfigProvider implements UnblockedConfigProvider {

    @Override
    public @Nullable UnblockedConfig getUnblockedConfig(@NonNull Run<?, ?> run) {
        var job = run.getParent();
        var current = job.getParent();
        while (current instanceof AbstractFolder) {
            var folder = (AbstractFolder<?>) current;
            var prop = folder.getProperties().get(UnblockedFolderProperty.class);
            if (prop != null) {
                return prop.getConfig();
            }
            current = folder.getParent();
        }

        return null;
    }
}
