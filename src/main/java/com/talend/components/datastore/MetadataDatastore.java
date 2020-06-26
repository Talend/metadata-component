package com.talend.components.datastore;

import java.io.Serializable;

import org.talend.sdk.component.api.configuration.Option;
import org.talend.sdk.component.api.configuration.constraint.Required;
import org.talend.sdk.component.api.configuration.type.DataStore;
import org.talend.sdk.component.api.configuration.ui.layout.GridLayout;
import org.talend.sdk.component.api.configuration.ui.layout.GridLayouts;
import org.talend.sdk.component.api.configuration.ui.widget.Credential;
import org.talend.sdk.component.api.meta.Documentation;

import lombok.Data;
import lombok.ToString;

@Data
@DataStore(MetadataDatastore.NAME)
@GridLayouts({ //
        @GridLayout({
                @GridLayout.Row({ "endpoint" }),
                @GridLayout.Row({ "token" })
        })
})
@Documentation(MetadataDatastore.NAME)
@ToString
public class MetadataDatastore implements Serializable {

    public static final String NAME = "MetadataDatastore";

    @Option
    @Required
    @Documentation("Metadata endpoint")
    private String endpoint = "https://tdc.at.cloud.talend.com";

    @Option
    @Required
    @Documentation("Personal Access Token")
    private String token;

}