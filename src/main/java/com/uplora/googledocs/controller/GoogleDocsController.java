package com.uplora.googledocs.controller;

import com.google.api.services.docs.v1.model.Request;
import com.uplora.googledocs.entity.MergeRequest;
import com.uplora.googledocs.service.GoogleDocsService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
public class GoogleDocsController {

    private GoogleDocsService googleDocsService;

    public GoogleDocsController(GoogleDocsService googleDocsService) {
        this.googleDocsService = googleDocsService;
    }

    @GetMapping("/call")
    public void callApi() throws IOException {
        Set<String> list = new HashSet<>();
        list.add("${1000537__f}");
        list.add("${1000547__f}");
        list.add("${1000549__f}");
        list.add("${1000550__f}");
        list.add("${1000551__f}");
        list.add("${1000552__f}");

        googleDocsService.getValuesFromLevel1API(list);
    }

    @GetMapping("/{documentId}")
    public void main(@PathVariable(value = "documentId") String documentId) throws IOException {
        googleDocsService.main(documentId);
    }

/*    @GetMapping("/extract-text")
    public String extract() throws IOException {
        return googleDocsService.extractTextFromDocument();
    }*/

    @GetMapping("/output-doc")
    public String outputDocAsJson() throws IOException {
        return googleDocsService.outputDocAsJson();
    }

    @PostMapping("/merge")
    public void mergeText(@RequestBody MergeRequest mergeRequest) throws IOException {
        googleDocsService.mergeText(mergeRequest.getValues(), new ArrayList<>());
    }
}
