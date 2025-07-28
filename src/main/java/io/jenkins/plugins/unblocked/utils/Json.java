package io.jenkins.plugins.unblocked.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.StringWriter;

public class Json {

    public static String stringify(Object object) {
        var jsonMapper = new ObjectMapper();
        var sw = new StringWriter();

        try {
            jsonMapper.writeValue(sw, object);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return sw.toString();
    }
}
