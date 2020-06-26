package com.talend.components.source;

import java.io.Serializable;

import com.talend.components.dataset.MetadataDataset;

import org.talend.sdk.component.api.configuration.Option;
import org.talend.sdk.component.api.configuration.ui.layout.GridLayout;
import org.talend.sdk.component.api.meta.Documentation;

@GridLayout({
    // the generated layout put one configuration entry per line,
    // customize it as much as needed
    @GridLayout.Row({ "dataset" })
})
@Documentation("TODO fill the documentation for this configuration")
public class MetadataInputMapperConfiguration implements Serializable {
    @Option
    @Documentation("TODO fill the documentation for this parameter")
    private MetadataDataset dataset;

    public MetadataDataset getDataset() {
        return dataset;
    }

    public MetadataInputMapperConfiguration setDataset(MetadataDataset dataset) {
        this.dataset = dataset;
        return this;
    }
}