package io.jenkins.plugins.unblocked.notify;

import hudson.model.Run;
import hudson.plugins.git.util.BuildData;
import io.jenkins.plugins.unblocked.utils.Http;
import io.jenkins.plugins.unblocked.utils.Json;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;
import jenkins.model.Jenkins;

public class Notifier {

    public static HttpResponse<String> submit(Run<?, ?> run) {
        return submit(null, run);
    }

    public static HttpResponse<String> submit(@Nullable String baseUrl, Run<?, ?> run) {
        final var payload = buildPayload(run);
        final var json = Json.stringify(payload);
        return Http.post(baseUrl, json);
    }

    private static Map<String, Object> buildPayload(final Run<?, ?> run) {
        final var payload = new HashMap<String, Object>();
        payload.put("jenkinsUrl", Jenkins.get().getRootUrl());

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
        payload.put("externalizableId", run.getExternalizableId());
        payload.put("url", run.getUrl());

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
