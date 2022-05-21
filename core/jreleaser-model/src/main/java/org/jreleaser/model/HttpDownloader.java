/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2020-2022 The JReleaser authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jreleaser.model;

import org.jreleaser.util.Env;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.jreleaser.util.Constants.HIDE;
import static org.jreleaser.util.Constants.UNSET;
import static org.jreleaser.util.StringUtils.isNotBlank;

/**
 * @author Andres Almiray
 * @since 1.1.0
 */
public class HttpDownloader extends AbstractDownloader implements Http {
    public static final String TYPE = "http";

    private final Map<String, String> headers = new LinkedHashMap<>();
    private String username;
    private String password;
    private Authorization authorization;

    public HttpDownloader() {
        super(TYPE);
    }

    void setAll(HttpDownloader http) {
        super.setAll(http);
        this.username = http.username;
        this.password = http.password;
        this.authorization = http.authorization;
        setHeaders(http.headers);
    }

    public Http.Authorization resolveAuthorization() {
        if (null == authorization) {
            authorization = Http.Authorization.NONE;
        }

        return authorization;
    }

    public String getResolvedUsername() {
        return Env.resolve("HTTP_" + Env.toVar(name) + "_USERNAME", username);
    }

    public String getResolvedPassword() {
        return Env.resolve("HTTP_" + Env.toVar(name) + "_PASSWORD", password);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Http.Authorization getAuthorization() {
        return authorization;
    }

    public void setAuthorization(Http.Authorization authorization) {
        this.authorization = authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = Http.Authorization.of(authorization);
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers.putAll(headers);
    }

    public void addHeaders(Map<String, String> headers) {
        this.headers.putAll(headers);
    }

    @Override
    protected void asMap(Map<String, Object> props, boolean full) {
        props.put("authorization", authorization);
        props.put("username", isNotBlank(getResolvedUsername()) ? HIDE : UNSET);
        props.put("password", isNotBlank(getResolvedPassword()) ? HIDE : UNSET);
        props.put("headers", headers);
    }
}
