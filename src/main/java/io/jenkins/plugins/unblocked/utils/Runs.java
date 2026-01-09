package io.jenkins.plugins.unblocked.utils;

import hudson.model.Run;
import hudson.plugins.git.util.BuildData;

public class Runs {

    public static String repoUrl(final Run<?, ?> run) {
        for (var buildData : run.getActions(BuildData.class)) {
            var removeUrls = buildData.remoteUrls;
            if (removeUrls != null) {
                var it = removeUrls.iterator();
                if (it.hasNext()) {
                    var repoUrl = it.next();
                    if (repoUrl != null) {
                        return repoUrl;
                    }
                }
            }
        }
        return null;
    }

    public static String repoVersion(final Run<?, ?> run) {
        for (var buildData : run.getActions(BuildData.class)) {
            var revision = buildData.getLastBuiltRevision();
            if (revision != null) {
                var sha1 = revision.getSha1String();
                if (sha1 != null) {
                    return sha1;
                }
            }
        }
        return null;
    }

    public static String repoBranch(final Run<?, ?> run) {
        for (var buildData : run.getActions(BuildData.class)) {
            var revision = buildData.getLastBuiltRevision();
            if (revision != null) {
                var it = revision.getBranches().iterator();
                if (it.hasNext()) {
                    var branch = it.next().getName();
                    if (branch != null) {
                        return branch;
                    }
                }
            }
        }
        return null;
    }
}
