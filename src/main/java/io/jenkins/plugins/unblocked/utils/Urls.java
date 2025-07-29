package io.jenkins.plugins.unblocked.utils;

import java.net.URL;

public class Urls {

    public static boolean isValid(String url) {
        try {
            new URL(url);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
