/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2020-2021 Andres Almiray.
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
package org.jreleaser.maven.plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.jreleaser.util.StringUtils.isNotBlank;

/**
 * @author Andres Almiray
 * @since 0.1.0
 */
public class Snap extends AbstractTool {
    public static final String NAME = "snap";

    private final List<String> localPlugs = new ArrayList<>();
    private final List<Plug> plugs = new ArrayList<>();
    private final List<Slot> slots = new ArrayList<>();
    private String base;
    private String grade;
    private String confinement;
    private File exportedLogin;
    private Boolean remoteBuild;

    public Snap() {
        super(NAME);
    }

    void setAll(Snap snap) {
        super.setAll(snap);
        this.base = snap.base;
        this.grade = snap.grade;
        this.confinement = snap.confinement;
        this.exportedLogin = snap.exportedLogin;
        this.remoteBuild = snap.remoteBuild;
        setLocalPlugs(localPlugs);
        setPlugs(plugs);
        setSlots(slots);
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getConfinement() {
        return confinement;
    }

    public void setConfinement(String confinement) {
        this.confinement = confinement;
    }

    public List<String> getLocalPlugs() {
        return localPlugs;
    }

    public void setLocalPlugs(List<String> localPlugs) {
        this.localPlugs.clear();
        this.localPlugs.addAll(localPlugs);
    }

    public void addLocalPlugs(List<String> localPlugs) {
        this.localPlugs.addAll(localPlugs);
    }

    public void addLocalPlug(String localPlug) {
        if (isNotBlank(localPlug)) {
            this.localPlugs.add(localPlug.trim());
        }
    }

    public void removeLocalPlug(String localPlug) {
        if (isNotBlank(localPlug)) {
            this.localPlugs.remove(localPlug.trim());
        }
    }

    public List<Plug> getPlugs() {
        return plugs;
    }

    public void setPlugs(List<Plug> plugs) {
        this.plugs.clear();
        this.plugs.addAll(plugs);
    }

    public void addPlugs(List<Plug> plugs) {
        this.plugs.addAll(plugs);
    }

    public void addPlug(Plug plug) {
        if (null != plug) {
            this.plugs.add(plug);
        }
    }

    public void removePlug(Plug plug) {
        if (null != plug) {
            this.plugs.remove(plug);
        }
    }

    public List<Slot> getSlots() {
        return slots;
    }

    public void setSlots(List<Slot> slots) {
        this.slots.clear();
        this.slots.addAll(slots);
    }

    public void addSlots(List<Slot> slots) {
        this.slots.addAll(slots);
    }

    public void addSlot(Slot slot) {
        if (null != slot) {
            this.slots.add(slot);
        }
    }

    public void removeSlot(Slot slot) {
        if (null != slot) {
            this.slots.remove(slot);
        }
    }

    public File getExportedLogin() {
        return exportedLogin;
    }

    public void setExportedLogin(File exportedLogin) {
        this.exportedLogin = exportedLogin;
    }

    public Boolean getRemoteBuild() {
        return remoteBuild;
    }

    public boolean isRemoteBuild() {
        return remoteBuild != null && remoteBuild;
    }

    public void setRemoteBuild(Boolean remoteBuild) {
        this.remoteBuild = remoteBuild;
    }

    @Override
    public boolean isSet() {
        return super.isSet() ||
            isNotBlank(base) ||
            isNotBlank(grade) ||
            isNotBlank(confinement) ||
            null != exportedLogin ||
            null != remoteBuild ||
            !localPlugs.isEmpty() ||
            !plugs.isEmpty() ||
            !slots.isEmpty();
    }
}