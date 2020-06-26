package com.talend.components.service;

import com.talend.components.datastore.MetadataDatastore;
import lombok.extern.slf4j.Slf4j;
import org.talend.sdk.component.api.configuration.Option;
import org.talend.sdk.component.api.service.Service;

import org.talend.sdk.component.api.service.completion.SuggestionValues;
import org.talend.sdk.component.api.service.completion.Suggestions;
import org.talend.sdk.component.api.service.http.Response;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class UIActionService extends MetadataService {

    public final String LIST_DATASETS = "LIST_DATASETS";

    @Suggestions(LIST_DATASETS)
    public SuggestionValues getListDataset(@Option final MetadataDatastore datastore) {

        inventoryClient.base("https://tdc.at.cloud.talend.com");

        Response<ArrayList> response = inventoryClient.getListDatasets(
                "application/json",
                "Bearer " + datastore.getToken(),
                "application/json");

        List<SuggestionValues.Item> listDatasets = new ArrayList<>();

        System.out.println(response.toString());

        for (int i=0; i < response.body().size(); i++) {
            String resp = response.body().get(i).toString();

            String id = getDatasetId(resp);
            String label = getDatasetLabel(resp);

            listDatasets.add(new SuggestionValues.Item(id, label));
        }
        return new SuggestionValues(false, listDatasets);
    }

}