package io.jenkins.plugins.unblocked.notify;

import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import hudson.model.Run;
import hudson.plugins.git.util.BuildData;
import hudson.util.Secret;
import io.jenkins.plugins.unblocked.UnblockedPlugin;
import io.jenkins.plugins.unblocked.utils.Http;
import io.jenkins.plugins.unblocked.utils.Json;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.TreeMap;
import jenkins.model.Jenkins;
import jenkins.plugins.git.AbstractGitSCMSource;
import jenkins.scm.api.SCMRevision;
import jenkins.scm.api.SCMRevisionAction;
import jenkins.scm.api.SCMSource;

public class Notifier {

    private static final String eventType = "Notify";

    public static HttpResponse<String> submit(
            @Nullable String baseUrl, @NonNull Secret signature, @NonNull Run<?, ?> run) {
        final var payload = buildPayload(run);
        final var json = Json.stringify(payload);
        return Http.post(baseUrl, eventType, json, signature);
    }

    private static Map<String, Object> buildPayload(final Run<?, ?> run) {
        final var payload = new TreeMap<String, Object>();
        injectJenkins(payload, run);
        injectRepo(payload, run);
        injectRun(payload, run);
        return payload;
    }

    private static void injectJenkins(final Map<String, Object> payload, final Run<?, ?> run) {
        var jenkins = Jenkins.get();

        var version = Jenkins.getVersion();
        if (version != null) {
            payload.put("jenkinsVersion", version.toString());
        }

        payload.put("jenkinsUrl", jenkins.getRootUrl());
        payload.put("pluginVersion", UnblockedPlugin.get().getVersion());
    }

    private static void injectRepo(Map<String, Object> payload, final Run<?, ?> run) {
        //
        final var scmRevisionAction = run.getAction(SCMRevisionAction.class);
        if (scmRevisionAction != null) {
            final var scmSource = SCMSource.SourceByItem.findSource(run.getParent());
            if (scmSource instanceof AbstractGitSCMSource gitSource) {
                final var repoUrl = gitSource.getRemote();
                if (repoUrl != null) {
                    payload.put("repoUrl", repoUrl);
                }
            }

            final SCMRevision revision = scmRevisionAction.getRevision();
            payload.put("repoBranch", revision.getHead().getName());
            payload.put("repoVersion", revision.toString());
        }

        if (!payload.containsKey("repoUrl")) {
            for (var action : run.getActions(BuildData.class)) {
                var it = action.remoteUrls.iterator();
                if (it.hasNext()) {
                    var repoUrl = it.next();
                    if (repoUrl != null) {
                        payload.put("repoUrl", repoUrl);
                        break;
                    }
                }
            }
        }

        if (!payload.containsKey("repoBranch")) {
            for (var action : run.getActions(BuildData.class)) {
                final var it = action.buildsByBranchName.keySet().iterator();
                if (it.hasNext()) {
                    final var branch = it.next();
                    if (branch != null) {
                        payload.put("repoBranch", branch);
                        final var build = action.buildsByBranchName.get(branch);
                        if (build != null) {
                            final var revision = build.getRevision();
                            if (revision != null) {
                                payload.put("repoVersion", revision.getSha1().name());
                            }
                        }
                    }
                    break;
                }
            }
        }
    }

    private static void injectRun(final Map<String, Object> payload, final Run<?, ?> run) {
        payload.put("runType", run.getClass().getName());
        payload.put("runId", run.getId());
        payload.put("runNumber", run.getNumber());
        payload.put("runSlug", run.getUrl());
        payload.put("runDisplayName", run.getDisplayName());

        final var result = run.getResult();
        if (result != null) {
            payload.put("runResult", result.toString());
        }

        payload.put("runStartedAt", run.getStartTimeInMillis());
        payload.put("runDuration", run.getDuration());
        if (result != null && result.isCompleteBuild()) {
            payload.put("runEndedAt", run.getStartTimeInMillis() + run.getDuration());
        }
    }
}
