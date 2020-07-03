package com.codelang.mamba

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.codelang.mamba.config.MambaExtension
import com.codelang.mamba.utils.CheckUtils
import com.codelang.mamba.utils.Logger
import org.gradle.api.Plugin
import org.gradle.api.Project


public class MambaPlugin implements Plugin<Project> {
    static final String EXT_NAME = "mamba"

    @Override
    void apply(Project project) {
        def isApp = project.plugins.hasPlugin(AppPlugin)
        project.extensions.create(EXT_NAME, MambaExtension)
        if (isApp) {
            Logger.make(project)
            Logger.e("run MambaPlugin")

            def android = project.extensions.getByType(AppExtension)
            def transform = new MambaTransform(project)
            android.registerTransform(transform)


            project.afterEvaluate {
                MambaExtension extension = project.extensions.findByName(EXT_NAME) as MambaExtension
                CheckUtils.exclude = extension.exclude
                CheckUtils.include = extension.include
                CheckUtils.trackEnable = extension.trackEnable
                CheckUtils.methodEnable = extension.methodEnable

                Logger.e("extension exclude:" + extension.exclude)
                Logger.e("extension include:" + extension.include)
                Logger.e("extension trackEnable:" + extension.trackEnable)
                Logger.e("extension methodEnable:" + extension.methodEnable)
            }
        }
    }
}