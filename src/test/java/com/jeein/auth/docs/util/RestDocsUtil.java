package com.jeein.auth.docs.util;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.snippet.Snippet;

public class RestDocsUtil {

    public static RestDocumentationResultHandler doc(String identifier, Snippet... snippets) {
        return document(identifier, preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                        snippets);
    }
}
