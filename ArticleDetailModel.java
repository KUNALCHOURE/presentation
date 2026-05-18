package com.gnp.core.core.models;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;

import com.adobe.cq.dam.cfm.ContentFragment;

import org.apache.sling.api.resource.Resource;

@Model(adaptables = SlingHttpServletRequest.class)
public class ArticleDetailModel {

    @Inject
    private SlingHttpServletRequest request;

    private Resource masterNode;

    @PostConstruct
    protected void init() {
        String cfPath = request.getParameter("cfPath");

        if (cfPath != null) {
            Resource resource = request.getResourceResolver().getResource(cfPath);
            if (resource != null) {
                masterNode = resource.getChild("jcr:content/data/master");
            }
        }
    }

    public String getTitle() {
        return masterNode != null ? masterNode.getValueMap().get("title", "") : "";
    }

    public String getAuthor() {
        return masterNode != null ? masterNode.getValueMap().get("author", "") : "";
    }

    public String getPublishDate() {
        return masterNode != null ? masterNode.getValueMap().get("publishdate", "") : "";
    }

    public String getContent() {
        return masterNode != null ? masterNode.getValueMap().get("body", "") : "";
    }

    public String getCategory() {
        if (masterNode != null) {
            String[] categories = masterNode.getValueMap().get("category", String[].class);
            if (categories != null && categories.length > 0) {
                return categories[0].contains(":")
                        ? categories[0].split(":")[1]
                        : categories[0];
            }
        }
        return "";
    }

    public String getImage() {
        return masterNode != null ? masterNode.getValueMap().get("heroimage", "") : "";
    }
}
