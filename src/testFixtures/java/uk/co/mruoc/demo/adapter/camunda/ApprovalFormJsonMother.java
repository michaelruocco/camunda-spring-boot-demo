package uk.co.mruoc.demo.adapter.camunda;

import uk.co.mruoc.file.content.ContentLoader;

public class ApprovalFormJsonMother {

    public static String build() {
        return ContentLoader.loadContentFromClasspath("approval-form.json");
    }

}
