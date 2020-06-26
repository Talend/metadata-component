package com.talend.components.dataset;

import java.io.Serializable;
import java.util.List;

import com.talend.components.datastore.MetadataDatastore;
import lombok.Data;
import lombok.ToString;

import org.talend.sdk.component.api.configuration.Option;
import org.talend.sdk.component.api.configuration.action.Suggestable;
import org.talend.sdk.component.api.configuration.constraint.Required;
import org.talend.sdk.component.api.configuration.type.DataSet;
import org.talend.sdk.component.api.meta.Documentation;
import org.talend.sdk.component.api.configuration.ui.layout.GridLayout;


@Data
@DataSet("MetadataDataset")
@Documentation("Metadata Dataset")
@ToString
@GridLayout({
        @GridLayout.Row({ "datastore" }),
        @GridLayout.Row({ "dataset" })
})

public class MetadataDataset implements Serializable {

    @Option
    @Documentation("Connection")
    private MetadataDatastore datastore;

    @Option
    @Required
    @Documentation("Dataset")
    @Suggestable(value = "LIST_DATASETS", parameters = { "../datastore" })
    private String dataset;

}