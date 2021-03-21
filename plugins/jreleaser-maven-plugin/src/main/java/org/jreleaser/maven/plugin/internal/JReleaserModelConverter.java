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
package org.jreleaser.maven.plugin.internal;

import org.jreleaser.maven.plugin.Announce;
import org.jreleaser.maven.plugin.Artifact;
import org.jreleaser.maven.plugin.Brew;
import org.jreleaser.maven.plugin.Changelog;
import org.jreleaser.maven.plugin.Chocolatey;
import org.jreleaser.maven.plugin.Distribution;
import org.jreleaser.maven.plugin.GitService;
import org.jreleaser.maven.plugin.Gitea;
import org.jreleaser.maven.plugin.Github;
import org.jreleaser.maven.plugin.Gitlab;
import org.jreleaser.maven.plugin.Jreleaser;
import org.jreleaser.maven.plugin.Packagers;
import org.jreleaser.maven.plugin.Plug;
import org.jreleaser.maven.plugin.Project;
import org.jreleaser.maven.plugin.Release;
import org.jreleaser.maven.plugin.Scoop;
import org.jreleaser.maven.plugin.Sdkman;
import org.jreleaser.maven.plugin.Signing;
import org.jreleaser.maven.plugin.Slot;
import org.jreleaser.maven.plugin.Snap;
import org.jreleaser.maven.plugin.Twitter;
import org.jreleaser.maven.plugin.Zulip;
import org.jreleaser.model.JReleaserModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Andres Almiray
 * @since 0.1.0
 */
public final class JReleaserModelConverter {
    private JReleaserModelConverter() {
        // noop
    }

    public static JReleaserModel convert(Jreleaser jreleaser) {
        JReleaserModel jreleaserModel = new JReleaserModel();
        jreleaserModel.setProject(convertProject(jreleaser.getProject()));
        jreleaserModel.setRelease(convertRelease(jreleaser.getRelease()));
        jreleaserModel.setPackagers(convertPackagers(jreleaser.getPackagers()));
        jreleaserModel.setAnnounce(convertAnnounce(jreleaser.getAnnounce()));
        jreleaserModel.setSigning(convertSigning(jreleaser.getSigning()));
        jreleaserModel.setFiles(convertArtifacts(jreleaser.getFiles()));
        jreleaserModel.setDistributions(convertDistributions(jreleaserModel, jreleaser.getDistributions()));
        return jreleaserModel;
    }

    private static org.jreleaser.model.Project convertProject(Project project) {
        org.jreleaser.model.Project p = new org.jreleaser.model.Project();
        p.setName(project.getName());
        p.setVersion(project.getVersion());
        p.setDescription(project.getDescription());
        p.setLongDescription(project.getLongDescription());
        p.setWebsite(project.getWebsite());
        p.setLicense(project.getLicense());
        p.setJavaVersion(project.getJavaVersion());
        p.setTags(project.getTags());
        p.setAuthors(project.getAuthors());
        p.setExtraProperties(project.getExtraProperties());
        return p;
    }

    private static org.jreleaser.model.Release convertRelease(Release release) {
        org.jreleaser.model.Release r = new org.jreleaser.model.Release();
        r.setGithub(convertGithub(release.getGithub()));
        r.setGitlab(convertGitlab(release.getGitlab()));
        r.setGitea(convertGitea(release.getGitea()));
        return r;
    }

    private static org.jreleaser.model.Github convertGithub(Github github) {
        if (null == github) return null;
        org.jreleaser.model.Github g = new org.jreleaser.model.Github();
        convertGitService(github, g);
        g.setTargetCommitish(github.getTargetCommitish());
        g.setDraft(github.isDraft());
        g.setPrerelease(github.isPrerelease());
        return g;
    }

    private static org.jreleaser.model.Gitlab convertGitlab(Gitlab gitlab) {
        if (null == gitlab) return null;
        org.jreleaser.model.Gitlab g = new org.jreleaser.model.Gitlab();
        convertGitService(gitlab, g);
        g.setRef(gitlab.getRef());
        return g;
    }

    private static org.jreleaser.model.Gitea convertGitea(Gitea gitea) {
        if (null == gitea) return null;
        org.jreleaser.model.Gitea g = new org.jreleaser.model.Gitea();
        convertGitService(gitea, g);
        g.setTargetCommitish(gitea.getTargetCommitish());
        g.setDraft(gitea.isDraft());
        g.setPrerelease(gitea.isPrerelease());
        return g;
    }

    private static void convertGitService(GitService service, org.jreleaser.model.GitService s) {
        s.setOwner(service.getOwner());
        s.setName(service.getName());
        s.setRepoUrlFormat(service.getRepoUrlFormat());
        s.setCommitUrlFormat(service.getCommitUrlFormat());
        s.setDownloadUrlFormat(service.getDownloadUrlFormat());
        s.setReleaseNotesUrlFormat(service.getReleaseNotesUrlFormat());
        s.setLatestReleaseUrlFormat(service.getLatestReleaseUrlFormat());
        s.setIssueTrackerUrlFormat(service.getIssueTrackerUrlFormat());
        s.setUsername(service.getUsername());
        s.setPassword(service.getPassword());
        s.setTagName(service.getTagName());
        s.setReleaseName(service.getReleaseName());
        s.setCommitAuthorName(service.getCommitAuthorName());
        s.setCommitAuthorEmail(service.getCommitAuthorEmail());
        s.setSign(service.isSign());
        s.setSigningKey(service.getSigningKey());
        s.setOverwrite(service.isOverwrite());
        s.setAllowUploadToExisting(service.isAllowUploadToExisting());
        s.setApiEndpoint(service.getApiEndpoint());
        s.setChangelog(convertChangelog(service.getChangelog()));
    }

    private static org.jreleaser.model.Changelog convertChangelog(Changelog changelog) {
        org.jreleaser.model.Changelog c = new org.jreleaser.model.Changelog();
        c.setEnabled(changelog.isEnabled());
        c.setSort(changelog.getSort().name());
        c.setExternal(changelog.getExternal());
        return c;
    }

    private static org.jreleaser.model.Packagers convertPackagers(Packagers packagers) {
        org.jreleaser.model.Packagers p = new org.jreleaser.model.Packagers();
        if (packagers.getBrew().isSet()) p.setBrew(convertBrew(packagers.getBrew()));
        if (packagers.getChocolatey().isSet()) p.setChocolatey(convertChocolatey(packagers.getChocolatey()));
        if (packagers.getScoop().isSet()) p.setScoop(convertScoop(packagers.getScoop()));
        if (packagers.getSnap().isSet()) p.setSnap(convertSnap(packagers.getSnap()));
        return p;
    }

    private static org.jreleaser.model.Announce convertAnnounce(Announce announce) {
        org.jreleaser.model.Announce a = new org.jreleaser.model.Announce();
        if (announce.getSdkman().isSet()) a.setSdkman(convertSdkman(announce.getSdkman()));
        if (announce.getTwitter().isSet()) a.setTwitter(convertTwitter(announce.getTwitter()));
        if (announce.getZulip().isSet()) a.setZulip(convertZulip(announce.getZulip()));
        return a;
    }

    private static org.jreleaser.model.Sdkman convertSdkman(Sdkman sdkman) {
        org.jreleaser.model.Sdkman a = new org.jreleaser.model.Sdkman();
        if (sdkman.isEnabledSet()) a.setEnabled(sdkman.isEnabled());
        a.setConsumerKey(sdkman.getConsumerKey());
        a.setConsumerToken(sdkman.getConsumerToken());
        a.setCandidate(sdkman.getCandidate());
        a.setMajor(sdkman.isMajor());
        return a;
    }

    private static org.jreleaser.model.Twitter convertTwitter(Twitter twitter) {
        org.jreleaser.model.Twitter a = new org.jreleaser.model.Twitter();
        if (twitter.isEnabledSet()) a.setEnabled(twitter.isEnabled());
        a.setConsumerKey(twitter.getConsumerKey());
        a.setConsumerSecret(twitter.getConsumerSecret());
        a.setAccessToken(twitter.getAccessToken());
        a.setAccessTokenSecret(twitter.getAccessTokenSecret());
        a.setStatus(twitter.getStatus());
        return a;
    }

    private static org.jreleaser.model.Zulip convertZulip(Zulip zulip) {
        org.jreleaser.model.Zulip a = new org.jreleaser.model.Zulip();
        if (zulip.isEnabledSet()) a.setEnabled(zulip.isEnabled());
        a.setAccount(zulip.getAccount());
        a.setApiKey(zulip.getApiKey());
        a.setApiHost(zulip.getApiHost());
        a.setChannel(zulip.getChannel());
        a.setSubject(zulip.getSubject());
        a.setMessage(zulip.getMessage());
        return a;
    }

    private static org.jreleaser.model.Signing convertSigning(Signing signing) {
        org.jreleaser.model.Signing s = new org.jreleaser.model.Signing();
        if (signing.isEnabledSet()) s.setEnabled(signing.isEnabled());
        s.setArmored(signing.isArmored());
        s.setKeyRingFile(signing.getKeyRingFile());
        s.setPassphrase(signing.getPassphrase());
        return s;
    }

    private static Map<String, org.jreleaser.model.Distribution> convertDistributions(JReleaserModel model, List<Distribution> distributions) {
        Map<String, org.jreleaser.model.Distribution> ds = new LinkedHashMap<>();
        for (Distribution distribution : distributions) {
            ds.put(distribution.getName(), convertDistribution(model, distribution));
        }
        return ds;
    }

    private static org.jreleaser.model.Distribution convertDistribution(JReleaserModel model, Distribution distribution) {
        org.jreleaser.model.Distribution d = new org.jreleaser.model.Distribution();
        d.setName(distribution.getName());
        d.setType(distribution.getType().name());
        d.setExecutable(distribution.getExecutable());
        d.setJavaVersion(distribution.getJavaVersion());
        d.setTags(distribution.getTags());
        d.setExtraProperties(distribution.getExtraProperties());
        d.setArtifacts(convertArtifacts(distribution.getArtifacts()));

        if (distribution.getBrew().isSet()) d.setBrew(convertBrew(distribution.getBrew()));
        if (distribution.getChocolatey().isSet()) d.setChocolatey(convertChocolatey(distribution.getChocolatey()));
        if (distribution.getScoop().isSet()) d.setScoop(convertScoop(distribution.getScoop()));
        if (distribution.getSnap().isSet()) d.setSnap(convertSnap(distribution.getSnap()));

        return d;
    }

    private static List<org.jreleaser.model.Artifact> convertArtifacts(List<Artifact> artifacts) {
        List<org.jreleaser.model.Artifact> as = new ArrayList<>();
        for (Artifact artifact : artifacts) {
            as.add(convertArtifact(artifact));
        }
        return as;
    }

    private static Set<org.jreleaser.model.Artifact> convertArtifacts(Set<Artifact> artifacts) {
        Set<org.jreleaser.model.Artifact> as = new LinkedHashSet<>();
        for (Artifact artifact : artifacts) {
            as.add(convertArtifact(artifact));
        }
        return as;
    }

    private static org.jreleaser.model.Artifact convertArtifact(Artifact artifact) {
        org.jreleaser.model.Artifact a = new org.jreleaser.model.Artifact();
        a.setPath(artifact.getPath());
        a.setHash(artifact.getHash());
        a.setPlatform(artifact.getPlatform());
        a.setJavaVersion(artifact.getJavaVersion());
        return a;
    }

    private static org.jreleaser.model.Brew convertBrew(Brew brew) {
        org.jreleaser.model.Brew t = new org.jreleaser.model.Brew();
        if (brew.isEnabledSet()) t.setEnabled(brew.isEnabled());
        t.setTemplateDirectory(brew.getTemplateDirectory());
        t.setExtraProperties(brew.getExtraProperties());
        t.setDependencies(brew.getDependencies());
        return t;
    }

    private static org.jreleaser.model.Chocolatey convertChocolatey(Chocolatey chocolatey) {
        org.jreleaser.model.Chocolatey t = new org.jreleaser.model.Chocolatey();
        if (chocolatey.isEnabledSet()) t.setEnabled(chocolatey.isEnabled());
        t.setUsername(chocolatey.getUsername());
        t.setRemoteBuild(chocolatey.isRemoteBuild());
        t.setTemplateDirectory(chocolatey.getTemplateDirectory());
        t.setExtraProperties(chocolatey.getExtraProperties());
        return t;
    }

    private static org.jreleaser.model.Scoop convertScoop(Scoop scoop) {
        org.jreleaser.model.Scoop t = new org.jreleaser.model.Scoop();
        if (scoop.isEnabledSet()) t.setEnabled(scoop.isEnabled());
        t.setTemplateDirectory(scoop.getTemplateDirectory());
        t.setExtraProperties(scoop.getExtraProperties());
        t.setCheckverUrl(scoop.getCheckverUrl());
        t.setAutoupdateUrl(scoop.getAutoupdateUrl());
        return t;
    }

    private static org.jreleaser.model.Snap convertSnap(Snap snap) {
        org.jreleaser.model.Snap t = new org.jreleaser.model.Snap();
        if (snap.isEnabledSet()) t.setEnabled(snap.isEnabled());
        t.setTemplateDirectory(snap.getTemplateDirectory());
        t.setExtraProperties(snap.getExtraProperties());
        t.setBase(snap.getBase());
        t.setGrade(snap.getGrade());
        t.setConfinement(snap.getConfinement());
        t.setExportedLogin(snap.getExportedLogin().getAbsolutePath());
        t.setRemoteBuild(snap.isRemoteBuild());
        t.setLocalPlugs(snap.getLocalPlugs());
        t.setPlugs(convertPlugs(snap.getPlugs()));
        t.setSlots(convertSlots(snap.getSlots()));
        return t;
    }

    private static List<org.jreleaser.model.Plug> convertPlugs(List<Plug> plugs) {
        List<org.jreleaser.model.Plug> ps = new ArrayList<>();
        for (Plug plug : plugs) {
            ps.add(convertArtifact(plug));
        }
        return ps;
    }

    private static org.jreleaser.model.Plug convertArtifact(Plug plug) {
        org.jreleaser.model.Plug p = new org.jreleaser.model.Plug();
        p.setName(plug.getName());
        p.setAttributes(plug.getAttributes());
        return p;
    }

    private static List<org.jreleaser.model.Slot> convertSlots(List<Slot> slots) {
        List<org.jreleaser.model.Slot> ps = new ArrayList<>();
        for (Slot slot : slots) {
            ps.add(convertSlot(slot));
        }
        return ps;
    }

    private static org.jreleaser.model.Slot convertSlot(Slot slot) {
        org.jreleaser.model.Slot p = new org.jreleaser.model.Slot();
        p.setName(slot.getName());
        p.setAttributes(slot.getAttributes());
        p.setReads(slot.getReads());
        p.setWrites(slot.getWrites());
        return p;
    }
}