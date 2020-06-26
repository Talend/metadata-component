package com.talend.components.service;

import com.talend.components.datastore.MetadataDatastore;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.talend.sdk.component.api.service.Service;
import org.talend.sdk.component.api.service.http.Response;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Accessors
@Slf4j
@Service
public class MetadataService {

    @Getter
    @Service
    public InventoryClient inventoryClient;

    public void initClients(MetadataDatastore datastore) {
        inventoryClient.base(datastore.getEndpoint());
    }

    public String extractCsrfToken(final Map<String, List<String>> headers) {
        String csrfToken = headers == null || headers.isEmpty()
                || !headers.containsKey("Set-Cookie") ? null :
                headers.get("Set-Cookie").iterator().next();

        if (csrfToken == null || csrfToken.isEmpty()) {
            return null;
        }

        csrfToken = ((csrfToken.contains(";")) ? csrfToken.split(";")[0] : csrfToken);
        csrfToken = ((csrfToken.contains("=")) ? csrfToken.split("=")[1] : csrfToken);
        return csrfToken;
    }

    public static JsonObject jsonFromString(String jsonObjectStr) {



        JsonReader jsonReader = Json.createReader(new StringReader(jsonObjectStr));
        JsonObject object = jsonReader.readObject();
        jsonReader.close();

        return object;
    }

    public String getDatasetLabel(String str) {
        Pattern p = Pattern.compile("(?<=label=).*?(?=,)");
        Matcher m = p.matcher(str);

        // Get second match
        m.find();
        m.find();


        return m.group();
    }

    public String getDatasetId(String str) {
        Pattern p = Pattern.compile("(?<=id=).*?(?=,)");
        Matcher m = p.matcher(str);

        String group = "";
        // Get second match
        while(m.find()) {
            group = m.group();
        }
        return group;
    }

    public String getRespVariable(String str, String var) {
        Pattern p = Pattern.compile("(?<=" + var + "=).*?(?=,)");
        Matcher m = p.matcher(str);

        m.find();
        return m.group();
    }

}