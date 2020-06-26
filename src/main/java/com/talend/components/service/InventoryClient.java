package com.talend.components.service;

import org.talend.sdk.component.api.service.http.*;

import javax.json.JsonArray;
import javax.json.JsonObject;
import java.util.ArrayList;

public interface InventoryClient extends HttpClient {

    @Request(path = "/api/v1/datasets", method = "GET")
    Response<ArrayList> getListDatasets(@Header("Content-Type") String contentType,
                                        @Header("Authorization") String authorization,
                                        @Header("Accept") String accept);

    @Request(path = "/api/v1/datasets/{datasetId}", method = "GET")
    Response<JsonObject> getDataset(@Header("Content-Type") String contentType,
                                    @Header("Authorization") String authorization,
                                    @Header("Accept") String accept,
                                    @Path("datasetId") String datasetId);

    @Request(path = "/api/v1/dataset-sample/{datasetId}", method = "PUT")
    Response<JsonObject> refreshSample(@Header("Content-Type") String contentType,
                                       @Header("Authorization") String authorization,
                                       @Header("Accept") String accept,
                                       @Header("Cookie") String cookie,
                                       @Header("X-CSRF-Token") String csrfToken,
                                       @Path("datasetId") String datasetId);

    @Request(path = "/api/v1/dataset-samples/{datasetId}", method = "GET")
    Response<ArrayList> getSampleInfo(@Header("Content-Type") String contentType,
                                    @Header("Authorization") String authorization,
                                    @Header("Accept") String accept,
                                    @Path("datasetId") String datasetId);
}