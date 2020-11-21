package com.uplora.googledocs.controller;

import com.uplora.googledocs.entity.MergeRequest;
import com.uplora.googledocs.service.GoogleDocsService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class GoogleDocsController {

    private GoogleDocsService googleDocsService;

    public GoogleDocsController(GoogleDocsService googleDocsService) {
        this.googleDocsService = googleDocsService;
    }

    @GetMapping("/{documentId}")
    public void main(@PathVariable(value = "documentId") String documentId) throws IOException {
//        googleDocsService.addRowsToTable(2);
        googleDocsService.main(documentId);
    }

    @PostMapping("/merge")
    public void mergeText(@RequestBody MergeRequest mergeRequest) throws IOException {
        googleDocsService.mergeText(mergeRequest.getValues());
    }
}
