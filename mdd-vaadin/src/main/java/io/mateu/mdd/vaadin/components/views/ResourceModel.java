package io.mateu.mdd.vaadin.components.views;


import io.mateu.mdd.shared.interfaces.IResource;

public class ResourceModel {

    private IResource resource;

    public IResource getResource() {
        return resource;
    }

    public void setResource(IResource resource) {
        this.resource.setType(resource.getType());
        this.resource.setName(resource.getName());
        this.resource.setBytes(resource.getBytes());
        this.resource.setPath(resource.getPath());
    }

    public ResourceModel(IResource original) {
        this.resource = original;
    }
}
