package io.javaoperatorsdk.operator.processing.event.source;

import io.fabric8.kubernetes.api.model.HasMetadata;
import io.javaoperatorsdk.operator.api.config.ResourceConfiguration;
import io.javaoperatorsdk.operator.processing.event.source.controller.ResourceCache;

public interface ResourceEventSource<T extends HasMetadata, P extends HasMetadata>
    extends EventSource<P> {
  ResourceCache<T> getResourceCache();

  ResourceConfiguration<T, ? extends ResourceConfiguration> getConfiguration();
}