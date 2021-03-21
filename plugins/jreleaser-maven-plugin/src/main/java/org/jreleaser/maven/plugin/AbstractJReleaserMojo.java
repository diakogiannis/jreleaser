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

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.jreleaser.maven.plugin.internal.JReleaserLoggerAdapter;
import org.jreleaser.maven.plugin.internal.JReleaserModelConfigurer;
import org.jreleaser.maven.plugin.internal.JReleaserModelConverter;
import org.jreleaser.model.JReleaserContext;
import org.jreleaser.model.JReleaserModel;
import org.jreleaser.model.JReleaserModelValidator;
import org.jreleaser.util.Logger;

import java.io.File;
import java.util.List;

/**
 * @author Andres Almiray
 * @since 0.1.0
 */
abstract class AbstractJReleaserMojo extends AbstractMojo {
    /**
     * The project whose model will be checked.
     */
    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    protected MavenProject project;

    @Parameter(required = true)
    protected Jreleaser jreleaser;

    @Parameter(property = "jreleaser.output.directory", defaultValue = "${project.build.directory}/jreleaser")
    protected File outputDirectory;

    @Parameter(property = "jreleaser.dryrun")
    protected boolean dryrun;

    protected Logger getLogger() {
        return new JReleaserLoggerAdapter(getLog());
    }

    protected JReleaserModel convertAndValidateModel() throws MojoExecutionException {
        JReleaserModel jreleaserModel = JReleaserModelConverter.convert(jreleaser);
        JReleaserModelConfigurer.configure(jreleaserModel, project);
        List<String> errors = JReleaserModelValidator.validate(getLogger(), project.getBasedir().toPath(), jreleaserModel);
        if (!errors.isEmpty()) {
            getLog().error("== JReleaser ==");
            errors.forEach(getLog()::error);
            throw new MojoExecutionException("JReleaser for project " + project.getArtifactId() + " has not been properly configured.");
        }

        return jreleaserModel;
    }

    protected JReleaserContext createContext() throws MojoExecutionException {
        return new JReleaserContext(
            getLogger(),
            convertAndValidateModel(),
            project.getBasedir().toPath(),
            outputDirectory.toPath(),
            dryrun);
    }
}