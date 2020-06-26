package com.talend.components.source;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.json.JsonArray;
import javax.json.JsonObject;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import com.talend.components.service.InventoryClient;
import org.talend.sdk.component.api.configuration.Option;
import org.talend.sdk.component.api.input.Producer;
import org.talend.sdk.component.api.meta.Documentation;
import org.talend.sdk.component.api.record.Record;
import org.talend.sdk.component.api.service.completion.SuggestionValues;
import org.talend.sdk.component.api.service.http.Response;
import org.talend.sdk.component.api.service.record.RecordBuilderFactory;


import com.talend.components.service.MetadataService;

@Documentation("TODO fill the documentation for this source")
public class MetadataInputSource implements Serializable {
    private final MetadataInputMapperConfiguration configuration;
    private final MetadataService service;
    private final RecordBuilderFactory builderFactory;
    private final InventoryClient inventoryClient;
    private boolean done;
    private boolean updated;


    public MetadataInputSource(@Option("configuration") final MetadataInputMapperConfiguration configuration,
                        final MetadataService service,
                        final RecordBuilderFactory builderFactory,
                        final InventoryClient inventoryClient ) {
        this.configuration = configuration;
        this.service = service;
        this.builderFactory = builderFactory;
        this.inventoryClient = inventoryClient;

    }

    @PostConstruct
    public void init() {
        // this method will be executed once for the whole component execution,
        // this is where you can establish a connection for instance

        done = false;
        updated = false;
        inventoryClient.base("https://tdc.at.cloud.talend.com");

    }

    @Producer
    public Record next() {

        // Initialization
        Record.Builder b = builderFactory.newRecordBuilder();
        Double trustScore = 0.00;
        Long timestamp = 0L;
        String header = "default";
        String csrfToken = "default";
        Date date = new Date();
        Integer status = 0;
        String body = "";

        // If Metadata has already been retrieved
        if (done) {
            return null;
        }
        else {

            // First GET request of the dataset
            Response<ArrayList> response = inventoryClient.getSampleInfo(
                    "application/json",
                    "Bearer " + configuration.getDataset().getDatastore().getToken(),
                    "application/json",
                    configuration.getDataset().getDataset());

            status = response.status();

            // If first request is successful
            if (status == 200) {

                timestamp = Long.parseLong(service.getRespVariable(response.body().get(0).toString(), "updated"));
                // header = response.headers().toString();
                csrfToken = service.extractCsrfToken(response.headers());

                Timestamp ts = new Timestamp(timestamp);
                date = ts;

                // First GET request of the dataset
                Response<JsonObject> refresh = inventoryClient.refreshSample(
                        "application/json",
                        "Bearer " + configuration.getDataset().getDatastore().getToken(),
                        "application/json",
                        "csrfToken=" + csrfToken,
                        csrfToken,
                        configuration.getDataset().getDataset());

                status = refresh.status();
                header = refresh.headers().toString();

                while (!updated) {

                    try {
                        //  Block of code to try
                        Thread.sleep(5000);

                        // First GET request of the dataset
                        Response<ArrayList> dataset = inventoryClient.getSampleInfo(
                                "application/json",
                                "Bearer " + configuration.getDataset().getDatastore().getToken(),
                                "application/json",
                                configuration.getDataset().getDataset());

                        if (dataset.status() == 200 && dataset.body() != null) {

                            if(Long.parseLong(service.getRespVariable(dataset.body().get(0).toString(), "updated")) != timestamp) {

                                timestamp = Long.parseLong(service.getRespVariable(dataset.body().get(0).toString(), "updated"));
                                ts = new Timestamp(timestamp);
                                date = ts;


                                // First GET request of the dataset
                                Response<JsonObject> trustscore = inventoryClient.getDataset(
                                        "application/json",
                                        "Bearer " + configuration.getDataset().getDatastore().getToken(),
                                        "application/json",
                                        configuration.getDataset().getDataset());

                                trustScore = trustscore.body().getJsonNumber("trustScore").doubleValue();

                                updated = true;
                            }
                        } else {
                            updated = false;
                        }
                    }
                    catch(Exception e) {
                        updated = true;
                    }
                }


            }
            done = true;
        }

        // b.withInt("status", status);
        b.withDouble("trustScore", trustScore);
        b.withDateTime("updated", date);
        // b.withString("header", header);
        // b.withString("token", csrfToken);
        //b.withString("body", body);

        return b.build();
    }

    @PreDestroy
    public void release() {
        // this is the symmetric method of the init() one,
        // release potential connections you created or data you cached
    }

}