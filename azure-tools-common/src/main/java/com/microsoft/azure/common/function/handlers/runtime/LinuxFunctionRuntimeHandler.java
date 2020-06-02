/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 */

package com.microsoft.azure.common.function.handlers.runtime;

import com.microsoft.azure.management.appservice.FunctionApp;
import com.microsoft.azure.management.appservice.FunctionRuntimeStack;
import com.microsoft.azure.management.appservice.JavaVersion;

public class LinuxFunctionRuntimeHandler extends AbstractLinuxFunctionRuntimeHandler {

    // Todo: Will update FunctionRuntimeStack to java:3.0-java8 once service team release the stable build
    private static final FunctionRuntimeStack JAVA_8_RUNTIME = new FunctionRuntimeStack("java", "~3", "java|8",
            "DOCKER|mcr.microsoft.com/azure-functions/java:3.0-java8-appservice");
    private static final FunctionRuntimeStack JAVA_11_RUNTIME = new FunctionRuntimeStack("java", "~3", "java|11",
            "DOCKER|mcr.microsoft.com/azure-functions/java:3.0-java11-appservice");

    public static class Builder extends FunctionRuntimeHandler.Builder<Builder> {

        @Override
        public LinuxFunctionRuntimeHandler build() {
            return new LinuxFunctionRuntimeHandler(self());
        }

        @Override
        protected Builder self() {
            return this;
        }

    }

    protected LinuxFunctionRuntimeHandler(Builder builder) {
        super(builder);
    }

    @Override
    public FunctionApp.DefinitionStages.WithCreate defineAppWithRuntime() {
        checkFunctionExtensionVersion();
        final FunctionApp.DefinitionStages.WithDockerContainerImage withDockerContainerImage = defineLinuxFunction();
        return withDockerContainerImage.withBuiltInImage(getRuntimeStack());
    }

    @Override
    public FunctionApp.Update updateAppRuntime(FunctionApp app) {
        checkFunctionExtensionVersion();
        return app.update().withBuiltInImage(getRuntimeStack());
    }

    private FunctionRuntimeStack getRuntimeStack() {
        return javaVersion == JavaVersion.JAVA_8_NEWEST ? JAVA_8_RUNTIME : JAVA_11_RUNTIME;
    }
}
