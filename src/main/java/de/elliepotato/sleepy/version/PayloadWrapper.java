package de.elliepotato.sleepy.version;

import com.google.common.collect.Maps;
import com.google.gson.annotations.Expose;

import java.util.Map;

/**
 * @author Ellie :: 27/07/2019
 */
public class PayloadWrapper {

    @Expose
    private String endpoint;
    @Expose
    private String method;
    @Expose
    private Map<String, String> payload;

    // Error

    @Expose(serialize = false)
    private int code;
    @Expose(serialize = false)
    private String message;

    public PayloadWrapper() {
        payload = Maps.newHashMap();
    }

    public PayloadWrapper addPayload(String key, String value) {
        this.payload.put(key, value);
        return this;
    }

    public String getMethod() {
        return method;
    }

    public PayloadWrapper setMethod(String method) {
        this.method = method;
        return this;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public PayloadWrapper setEndpoint(String endpoint) {
        this.endpoint = endpoint;
        return this;
    }

    public Map<String, String> getPayload() {
        return payload;
    }

    public int getResponseCode() {
        return code;
    }

    public String getResponseMessage() {
        return message;
    }

}
