package io.jenkins.plugins.unblocked.notify;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.model.Run;
import hudson.plugins.git.util.BuildData;
import io.jenkins.plugins.unblocked.utils.Http;
import io.jenkins.plugins.unblocked.utils.Json;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.TreeMap;
import javax.annotation.Nullable;
import jenkins.model.Jenkins;

public class Notifier {

    public static HttpResponse<String> submit(
            @Nullable String baseUrl, @NonNull String signature, @NonNull Run<?, ?> run) {
        final var payload = buildPayload(run);
        final var json = Json.stringify(payload);
        return Http.post(baseUrl, json, signature);
    }

    private static Map<String, Object> buildPayload(final Run<?, ?> run) {
        final var jenkinsUrl = Jenkins.get().getRootUrl();

        final var payload = new TreeMap<String, Object>();
        payload.put("_class", NotifyStep.class.getName());

        final var result = run.getResult();
        if (result != null) {
            payload.put("result", result.toString());
        }
        payload.put("startedAt", run.getStartTimeInMillis());
        payload.put("duration", run.getDuration());
        if (result != null && result.isCompleteBuild()) {
            payload.put("endedAt", run.getStartTimeInMillis() + run.getDuration());
        }

        payload.put("displayName", run.getDisplayName());
        payload.put("number", run.getNumber());
        payload.put("id", run.getId());
        payload.put("url", jenkinsUrl + run.getUrl());

        final var actions = run.getActions(BuildData.class);
        if (!actions.isEmpty()) {
            final BuildData data = actions.get(0);
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
            if (!data.remoteUrls.isEmpty()) {
                payload.put("repo", data.remoteUrls.toArray()[0].toString());
            }
        }

        return payload;
    }
}
