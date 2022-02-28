package io.javaoperatorsdk.operator.api.config.informer;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import io.fabric8.kubernetes.api.model.HasMetadata;
import io.javaoperatorsdk.operator.api.config.ConfigurationService;
import io.javaoperatorsdk.operator.api.config.DefaultResourceConfiguration;
import io.javaoperatorsdk.operator.api.config.ResourceConfiguration;
import io.javaoperatorsdk.operator.api.reconciler.EventSourceContext;
import io.javaoperatorsdk.operator.processing.event.ResourceID;
import io.javaoperatorsdk.operator.processing.event.source.AssociatedSecondaryResourceIdentifier;
import io.javaoperatorsdk.operator.processing.event.source.PrimaryResourcesRetriever;
import io.javaoperatorsdk.operator.processing.event.source.informer.Mappers;

public interface InformerConfiguration<R extends HasMetadata, P extends HasMetadata>
    extends ResourceConfiguration<R> {

  class DefaultInformerConfiguration<R extends HasMetadata, P extends HasMetadata> extends
      DefaultResourceConfiguration<R> implements InformerConfiguration<R, P> {

    private final PrimaryResourcesRetriever<R> secondaryToPrimaryResourcesIdSet;
    private final AssociatedSecondaryResourceIdentifier<P> associatedWith;

    protected DefaultInformerConfiguration(ConfigurationService service, String labelSelector,
        Class<R> resourceClass,
        PrimaryResourcesRetriever<R> secondaryToPrimaryResourcesIdSet,
        AssociatedSecondaryResourceIdentifier<P> associatedWith,
        Set<String> namespaces) {
      super(labelSelector, resourceClass, namespaces);
      setConfigurationService(service);
      this.secondaryToPrimaryResourcesIdSet =
          Objects.requireNonNullElse(secondaryToPrimaryResourcesIdSet,
              Mappers.fromOwnerReference());
      this.associatedWith =
          Objects.requireNonNullElseGet(associatedWith, () -> ResourceID::fromResource);
    }


    public PrimaryResourcesRetriever<R> getPrimaryResourcesRetriever() {
      return secondaryToPrimaryResourcesIdSet;
    }

    public AssociatedSecondaryResourceIdentifier<P> getAssociatedResourceIdentifier() {
      return associatedWith;
    }

  }

  PrimaryResourcesRetriever<R> getPrimaryResourcesRetriever();

  AssociatedSecondaryResourceIdentifier<P> getAssociatedResourceIdentifier();

  class InformerConfigurationBuilder<R extends HasMetadata, P extends HasMetadata> {

    private PrimaryResourcesRetriever<R> secondaryToPrimaryResourcesIdSet;
    private AssociatedSecondaryResourceIdentifier<P> associatedWith;
    private Set<String> namespaces;
    private String labelSelector;
    private final Class<R> resourceClass;
    private final ConfigurationService configurationService;

    private InformerConfigurationBuilder(Class<R> resourceClass,
        ConfigurationService configurationService) {
      this.resourceClass = resourceClass;
      this.configurationService = configurationService;
    }

    public InformerConfigurationBuilder<R, P> withPrimaryResourcesRetriever(
        PrimaryResourcesRetriever<R> primaryResourcesRetriever) {
      this.secondaryToPrimaryResourcesIdSet = primaryResourcesRetriever;
      return this;
    }

    public InformerConfigurationBuilder<R, P> withAssociatedSecondaryResourceIdentifier(
        AssociatedSecondaryResourceIdentifier<P> associatedWith) {
      this.associatedWith = associatedWith;
      return this;
    }


    public InformerConfigurationBuilder<R, P> withNamespaces(String... namespaces) {
      this.namespaces = namespaces != null ? Set.of(namespaces) : Collections.emptySet();
      return this;
    }

    public InformerConfigurationBuilder<R, P> withNamespaces(Set<String> namespaces) {
      this.namespaces = namespaces != null ? namespaces : Collections.emptySet();
      return this;
    }


    public InformerConfigurationBuilder<R, P> withLabelSelector(String labelSelector) {
      this.labelSelector = labelSelector;
      return this;
    }

    public InformerConfiguration<R, P> build() {
      return new DefaultInformerConfiguration<>(configurationService, labelSelector, resourceClass,
          secondaryToPrimaryResourcesIdSet, associatedWith,
          namespaces);
    }
  }

  static <R extends HasMetadata, P extends HasMetadata> InformerConfigurationBuilder<R, P> from(
      EventSourceContext<P> context, Class<R> resourceClass) {
    return new InformerConfigurationBuilder<>(resourceClass, context.getConfigurationService());
  }

  static InformerConfigurationBuilder from(ConfigurationService configurationService,
      Class resourceClass) {
    return new InformerConfigurationBuilder<>(resourceClass, configurationService);
  }

  static <R extends HasMetadata, P extends HasMetadata> InformerConfigurationBuilder<R, P> from(
      InformerConfiguration<R, P> configuration) {
    return new InformerConfigurationBuilder<R, P>(configuration.getResourceClass(),
        configuration.getConfigurationService())
            .withNamespaces(configuration.getNamespaces())
            .withLabelSelector(configuration.getLabelSelector())
            .withAssociatedSecondaryResourceIdentifier(
                configuration.getAssociatedResourceIdentifier())
            .withPrimaryResourcesRetriever(configuration.getPrimaryResourcesRetriever());
  }
}