package com.uplora.googledocs.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.docs.v1.Docs;
import com.google.api.services.docs.v1.DocsScopes;
import com.google.api.services.docs.v1.model.*;
import com.uplora.googledocs.entity.MergeRequest;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class GoogleDocsService {

    private final Docs service;
    private final NetHttpTransport HTTP_TRANSPORT;

    private static final String APPLICATION_NAME = "Google Docs API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final String DOCUMENT_ID = "1ZZWExvIj9t0eHzCr-z3uAXUEHwFt9DnefId2DabecDU";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(DocsScopes.DOCUMENTS);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    public GoogleDocsService() throws GeneralSecurityException, IOException {
        // Build a new authorized API client service.
        HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        service = new Docs.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = GoogleDocsService.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public void main(String... args) {

        // Prints the title of the requested doc:
        // https://docs.google.com/document/d/195j9eDD3ccgjQRttHhJPymLJUCOUjs-jmwTrekvdjFE/edit
        Document response = null;
        try {
            response = service.documents().get(DOCUMENT_ID).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String title = response.getTitle();
        System.out.printf("The title of the doc is: %s\n", title);

        // inserting text
        /*List<Request> requests = new ArrayList<>();
        requests.add(new Request().setInsertText(new InsertTextRequest()
                .setText("this is the last line\n")
                .setLocation(new Location().setIndex(1))));

        requests.add(new Request().setInsertText(new InsertTextRequest()
                .setText("this is the middle line\n")
                .setLocation(new Location().setIndex(1))));

        requests.add(new Request().setInsertText(new InsertTextRequest()
                .setText("this is the first line\n")
                .setLocation(new Location().setIndex(1))));

        BatchUpdateDocumentRequest body = new BatchUpdateDocumentRequest().setRequests(requests);
        BatchUpdateDocumentResponse response2 = service.documents()
                .batchUpdate(DOCUMENT_ID, body).execute();*/

        // deleting text. Deleting the following string -> 'middle '
        /*List<Request> requests = new ArrayList<>();
        requests.add(new Request().setDeleteContentRange(
                new DeleteContentRangeRequest()
                        .setRange(new Range()
                                .setStartIndex(36)
                                .setEndIndex(43))
        ));

        BatchUpdateDocumentRequest body = new BatchUpdateDocumentRequest().setRequests(requests);
        BatchUpdateDocumentResponse response3 = service.documents()
                .batchUpdate(DOCUMENT_ID, body).execute();*/

        // output Doc as JSON
        /*Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(gson.toJson(response));*/

        // print the body text
        /*List<StructuralElement> contents = response.getBody().getContent();
        for (StructuralElement content : contents) {
            if(content.getParagraph() != null) {
                List<ParagraphElement> elements = content.getParagraph().getElements();
                for (ParagraphElement element : elements) {
                    System.out.println(element.getTextRun().getContent());
                }
            }
        }*/
    }

    public void mergeText(MergeRequest mergeRequest) {
        List<Request> requests = new ArrayList<>();

        mergeRequest.getValues().forEach(value -> {
            requests.add(new Request()
                    .setReplaceAllText(new ReplaceAllTextRequest()
                            .setContainsText(new SubstringMatchCriteria()
                                    .setText("${" + value.getId() + "}")
                                    .setMatchCase(true))
                            .setReplaceText(value.getValue())));
        });

        mergeRequest.getTable().forEach(row -> {
            row.keySet().forEach(cell -> {
                requests.add(new Request()
                        .setReplaceAllText(new ReplaceAllTextRequest()
                                .setContainsText(new SubstringMatchCriteria()
                                        .setText("${" + cell + "}")
                                        .setMatchCase(true))
                                .setReplaceText(row.get(cell))));
            });
        });

        BatchUpdateDocumentRequest body = new BatchUpdateDocumentRequest();
        try {
            service.documents().batchUpdate(DOCUMENT_ID, body.setRequests(requests)).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
