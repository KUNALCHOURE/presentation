package com.gnp.core.core.models;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.jcr.Session;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.result.SearchResult;

@Model(adaptables = SlingHttpServletRequest.class)
public class NewsListModel {

    @SlingObject
    private ResourceResolver resolver;

    @Inject
    private SlingHttpServletRequest request;

    private List<Resource> articles;

    @PostConstruct
    protected void init() {

     
Map<String, String> map = new HashMap<>();

map.put("path", "/content/dam/gnp-new-portal/articles");
map.put("type", "dam:Asset");

map.put("1_property", "jcr:content/data/cq:model");
map.put("1_property.value", "/conf/gnp-new-portal/settings/dam/cfm/models/article");

// ✅ IMPORTANT: return ALL results
map.put("p.limit", "-1");

// ✅ Category filter
String categoryParam = request.getParameter("category");

if (categoryParam != null && !categoryParam.isEmpty()) {
    map.put("2_property", "jcr:content/data/master/cq:tags");
    map.put("2_property.value", categoryParam);
    map.put("2_property.operation", "like");
}

// ✅ Search
String searchText = request.getParameter("search");
if (searchText != null && !searchText.isEmpty()) {
    map.put("fulltext", searchText);
}

// ✅ Sort
map.put("orderby", "@jcr:content/data/master/publishdate");
map.put("orderby.sort", "desc");


        QueryBuilder builder = resolver.adaptTo(QueryBuilder.class);
        Session session = resolver.adaptTo(Session.class);

        Query query = builder.createQuery(PredicateGroup.create(map), session);
        SearchResult result = query.getResult();

        articles = new ArrayList<>();

        Iterator<Resource> iterator = result.getResources();
        while (iterator.hasNext()) {
            articles.add(iterator.next());
        }
    }

    public List<Resource> getArticles() {
        return articles;
    }

    
}