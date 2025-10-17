# Unblocked Plugin

Unblocked helps you find accurate answers about your application
by augmenting your source code with context from GitHub, Slack,
and [more](https://docs.getunblocked.com/what-is-unblocked).

When a CI job fails on a pull request, Unblocked will post a report
as a comment in your pull request, helping you quickly identify and
resolve the issue.

> https://getunblocked.com

### Pipeline Step

The Unblocked plugin is designed to automatically trigger a post-build
notification. You can optionally add the step manually to your pipeline
for explicit control, as shown below:

```groovy
pipeline {

    // steps..

    post {
        always {
            unblockedNotify()
        }
    }
}
```
