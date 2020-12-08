/**
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
 *
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.javaoperatorsdk.operator.sample;

import io.fabric8.kubernetes.client.CustomResource;
import io.javaoperatorsdk.operator.api.Context;
import io.javaoperatorsdk.operator.api.DeleteControl;
import io.javaoperatorsdk.operator.api.ResourceController;
import io.javaoperatorsdk.operator.api.UpdateControl;

/**
 * @author <a href="claprun@redhat.com">Christophe Laprun</a>
 */
public class TestResourceController implements ResourceController<TestResourceController.TestResource> {
    
    @Override
    public DeleteControl deleteResource(TestResource resource, Context<TestResource> context) {
        return null;
    }
    
    @Override
    public UpdateControl<TestResource> createOrUpdateResource(TestResource resource, Context<TestResource> context) {
        return null;
    }
    
    public static class TestResource extends CustomResource {
    }
}
