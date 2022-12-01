package uk.co.mruoc.demo.adapter.camunda;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import uk.co.mruoc.file.FileLoader;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApprovalFormJsonMother {

    public static String build() {
        return FileLoader.loadContentFromClasspath("approval-form.json");
    }

}
