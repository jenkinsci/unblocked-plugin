package io.jenkins.plugins.unblocked.notify;

import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import hudson.model.Run;
import hudson.util.Secret;
import io.jenkins.plugins.unblocked.UnblockedPlugin;
import io.jenkins.plugins.unblocked.utils.Http;
import io.jenkins.plugins.unblocked.utils.Json;
import io.jenkins.plugins.unblocked.utils.Runs;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.TreeMap;
import jenkins.model.Jenkins;

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
        payload.put("repoUrl", Runs.repoUrl(run));
        payload.put("repoVersion", Runs.repoVersion(run));
        payload.put("repoBranch", Runs.repoBranch(run));
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
