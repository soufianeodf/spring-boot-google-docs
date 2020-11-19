package com.uplora.googledocs.controller;

import com.uplora.googledocs.entity.MergeRequest;
import com.uplora.googledocs.service.GoogleDocsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class GoogleDocsController {

    private GoogleDocsService googleDocsService;

    public GoogleDocsController(GoogleDocsService googleDocsService) {
        this.googleDocsService = googleDocsService;
    }

    @GetMapping("/")
    public void main() throws IOException {
//        googleDocsService.addRowsToTable(2);
        googleDocsService.main();
    }

    @PostMapping("/merge")
    public void mergeText(@RequestBody MergeRequest mergeRequest) throws IOException {
        googleDocsService.mergeText(mergeRequest.getValues());
    }
}
