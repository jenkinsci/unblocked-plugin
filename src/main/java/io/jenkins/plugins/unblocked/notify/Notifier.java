package io.jenkins.plugins.unblocked.notify;

import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import hudson.model.Run;
import hudson.plugins.git.util.BuildData;
import io.jenkins.plugins.unblocked.utils.Http;
import io.jenkins.plugins.unblocked.utils.Json;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.TreeMap;
import jenkins.model.Jenkins;

public class Notifier {

    private static final String eventType = "Notify";

    public static HttpResponse<String> submit(
            @Nullable String baseUrl, @NonNull String signature, @NonNull Run<?, ?> run) {
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

        var plugin = jenkins.getPluginManager().getPlugin("unblocked");
        if (plugin != null) {
            payload.put("pluginVersion", plugin.getVersion());
        }
    }

    private static void injectRepo(Map<String, Object> payload, final Run<?, ?> run) {
        final var actions = run.getActions(BuildData.class);

        if (actions.isEmpty()) {
            return;
        }

        final BuildData data = actions.get(0);
        if (!data.remoteUrls.isEmpty()) {
            payload.put("repoUrl", data.remoteUrls.toArray()[0].toString());
        }

        if (!data.buildsByBranchName.isEmpty()) {
            final var branch = data.buildsByBranchName.keySet().toArray()[0].toString();
            payload.put("repoBranch", branch);
            final var build = data.buildsByBranchName.get(branch);
            if (build != null) {
                final var revision = build.getRevision();
                if (revision != null) {
                    payload.put("repoVersion", revision.getSha1().name());
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
