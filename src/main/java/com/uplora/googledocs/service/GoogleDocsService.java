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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.uplora.googledocs.entity.MergeRequest;
import com.uplora.googledocs.entity.Value;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class GoogleDocsService {

    private final Docs service;
    private final NetHttpTransport HTTP_TRANSPORT;

    private static final String APPLICATION_NAME = "Google Docs API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final String DOCUMENT_ID = "1Jp1q4XdcsYtifFXI3Xz6kJqpo4mw6SWaa58P9t5UuRM";

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

    public void main(String... args) throws IOException {

        // Prints the title of the requested doc:
        // https://docs.google.com/document/d/195j9eDD3ccgjQRttHhJPymLJUCOUjs-jmwTrekvdjFE/edit
        /*Document response = null;
        try {
            response = service.documents().get(DOCUMENT_ID).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String title = response.getTitle();
        System.out.printf("The title of the doc is: %s\n", title);*/

        // inserting text in backwards
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
//        Document response = service.documents().get(DOCUMENT_ID).execute();
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        System.out.println(gson.toJson(response));

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

        // inserting text into table and add new row
        /*List<Request> requests = new ArrayList<>();
        String hello = "first cell value";
        String voila = "second cell value";
        requests.add(new Request().setInsertText(new InsertTextRequest()
                .setText(hello)
                .setLocation(new Location().setIndex(5))));
        requests.add(new Request().setInsertText(new InsertTextRequest()
                .setText(voila)
                .setLocation(new Location().setIndex(5 + hello.length() + 2))));
        requests.add(new Request().setInsertTableRow(new InsertTableRowRequest()
                .setTableCellLocation(new TableCellLocation()
                        .setTableStartLocation(new Location()
                                .setIndex(2))
                        .setRowIndex(1)
                        .setColumnIndex(1))
                .setInsertBelow(true)));

        BatchUpdateDocumentRequest body =
                new BatchUpdateDocumentRequest().setRequests(requests);
        BatchUpdateDocumentResponse response = service.documents()
                .batchUpdate(DOCUMENT_ID, body).execute();*/

        List<String> helperAnnotationsName = new ArrayList<>();
        MergeRequest valuesFromEndpoint = getValuesFromEndpoint(extractVariablesFromText(extractTextFromDocument()));
        for (int i = 0; i < valuesFromEndpoint.getTables().size(); i++) {
            String helperAnnotationName = "${dynamic_table_" + (i + 1) + "}";
            helperAnnotationsName.add(helperAnnotationName);
            transformTheTable(valuesFromEndpoint.getTables().get(i), helperAnnotationName);
        }
        mergeText(valuesFromEndpoint.getValues());
        removeHelperAnnotations(helperAnnotationsName);
    }

    public void removeHelperAnnotations(List<String> helperAnnotationsName) throws IOException {
        List<Request> requests = new ArrayList<>();
        // remove helper annotations name
        for (String helperAnnotationName : helperAnnotationsName) {
            requests.add(new Request()
                    .setReplaceAllText(new ReplaceAllTextRequest()
                            .setContainsText(new SubstringMatchCriteria()
                                    .setText(helperAnnotationName)
                                    .setMatchCase(true))
                            .setReplaceText("")));
        }

        BatchUpdateDocumentRequest body = new BatchUpdateDocumentRequest();
        service.documents().batchUpdate(DOCUMENT_ID, body.setRequests(requests)).execute();
    }

    public void transformTheTable(List<HashMap<String, String>> table, String helperAnnotationName) throws IOException {
        int endIndexOfDynamicTableWord = 0;
        int indexOfFirstCell = 0;
        int tableRowsNumber = table.size();
        int tableColumnsNumber = table.get(0).size();

        Document response = service.documents().get(DOCUMENT_ID).execute();
        // print the response as json
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        System.out.println(gson.toJson(response));

        List<StructuralElement> elements = response.getBody().getContent();

        for (StructuralElement element : elements) {
            // get the endIndex of "${dynamic_table}"
            if(element.getParagraph() != null) {
                for (ParagraphElement paragraphElement : element.getParagraph().getElements()) {
                    if(paragraphElement.getTextRun().getContent().equalsIgnoreCase(helperAnnotationName + "\n")) {
                        endIndexOfDynamicTableWord = paragraphElement.getEndIndex();
                    } else if(paragraphElement.getTextRun().getContent().equalsIgnoreCase(helperAnnotationName)) {
                        endIndexOfDynamicTableWord = paragraphElement.getEndIndex() + 1;
                    }
                }
            }
        }

        boolean hasAlreadyEntered = false;
        for (StructuralElement element : elements) {
            // get the sum of the lengths of the table title's
            if (element.getTable() != null && element.getEndIndex() > endIndexOfDynamicTableWord && !hasAlreadyEntered) {
                hasAlreadyEntered = true;
                tableColumnsNumber = element.getTable().getColumns();
                for (TableCell cell : element.getTable().getTableRows().get(0).getTableCells()) {
                    indexOfFirstCell += cell.getContent().get(0)
                            .getParagraph().getElements().get(0)
                            .getTextRun().getContent()
                            .replace("\n", "").length();
                }
            }
        }

        indexOfFirstCell += endIndexOfDynamicTableWord + 3 + (tableColumnsNumber-1)*2 + 3;

//        System.out.println(endIndexOfDynamicTableWord);

        List<Request> requests = new ArrayList<>();

        // add rows to the table
        for(int i = 0; i < tableRowsNumber; i++) {
            requests.add(new Request().setInsertTableRow(new InsertTableRowRequest()
                    .setTableCellLocation(new TableCellLocation()
                            .setTableStartLocation(new Location()
                                    .setIndex(endIndexOfDynamicTableWord))
                            .setRowIndex(1)
                            .setColumnIndex(1))
                    .setInsertBelow(true)));
        }

        // delete first row from table
        requests.add(new Request().setDeleteTableRow(new DeleteTableRowRequest()
                .setTableCellLocation(new TableCellLocation()
                        .setTableStartLocation(new Location()
                                .setIndex(endIndexOfDynamicTableWord))
                        .setRowIndex(1)
                        .setColumnIndex(1))));

        // fill cells of table with related values
        int j = 0;
        for (HashMap<String, String> row: table) {
            j++;
            // transform Map to List
            List<String> list = new ArrayList<>(row.values());
            for (int i = 0; i < list.size(); i++) {
                if(i == 0 && j > 1) {
                    indexOfFirstCell += 3;
                }
                if(i != 0) {
                    indexOfFirstCell += list.get(i-1).length() + 2;
                }
                requests.add(new Request().setInsertText(new InsertTextRequest()
                        .setText(list.get(i))
                        .setLocation(new Location().setIndex(indexOfFirstCell))));
                if(i == list.size() -1) {
                    indexOfFirstCell += list.get(list.size() - 1).length();
                }
            }
        }

        // remove ${dynamic_table} annotation
//        requests.add(new Request()
//                .setReplaceAllText(new ReplaceAllTextRequest()
//                        .setContainsText(new SubstringMatchCriteria()
//                                .setText("${dynamic_table}")
//                                .setMatchCase(true))
//                        .setReplaceText("")));

        BatchUpdateDocumentRequest body = new BatchUpdateDocumentRequest();
        service.documents().batchUpdate(DOCUMENT_ID, body.setRequests(requests)).execute();
    }

    public void mergeText(List<Value> values) throws IOException {
        List<Request> requests = new ArrayList<>();

        values.forEach(value -> {
            requests.add(new Request()
                    .setReplaceAllText(new ReplaceAllTextRequest()
                            .setContainsText(new SubstringMatchCriteria()
                                    .setText("${" + value.getId() + "}")
                                    .setMatchCase(true))
                            .setReplaceText(value.getValue())));
        });

        BatchUpdateDocumentRequest body = new BatchUpdateDocumentRequest();
        service.documents().batchUpdate(DOCUMENT_ID, body.setRequests(requests)).execute();
    }

    public MergeRequest getValuesFromEndpoint(Set<String> list) {
        List<Value> values = new ArrayList<>();
        values.add(new Value("company_name", "Uplora"));
        values.add(new Value("company_address", "uplora company street"));
        values.add(new Value("company_city", "uplora company city"));
        values.add(new Value("prepared_date", "Jun 06, 2020"));
        values.add(new Value("exp_date", "Aug 06, 2020"));
        values.add(new Value("customer_name", "john doe"));
        values.add(new Value("customer_street", "customer street"));
        values.add(new Value("customer_city", "customer city"));

        // key should be unique
        HashMap<String, String> row_1 = new LinkedHashMap<>();
        row_1.put("col_11", "val_11");
        row_1.put("col_12", "val_12");
        row_1.put("col_13", "val_12");
        row_1.put("col_14", "val_12");
        row_1.put("col_15", "val_12");

        HashMap<String, String> row_2 = new LinkedHashMap<>();
        row_2.put("col_21", "val_21");
        row_2.put("col_22", "val_22");
        row_2.put("col_23", "val_22");
        row_2.put("col_24", "val_22");
        row_2.put("col_25", "val_22");

        HashMap<String, String> row_3 = new LinkedHashMap<>();
        row_3.put("col_31", "val_31");
        row_3.put("col_32", "val_32");
        row_3.put("col_33", "val_32");
        row_3.put("col_34", "val_32");
        row_3.put("col_35", "val_32");

        List<HashMap<String, String>> table = new ArrayList<>();
        table.add(row_1);
        table.add(row_2);
        table.add(row_3);

        List<List<HashMap<String, String>>> tables = new ArrayList<>();
        tables.add(table);
        tables.add(table);
        tables.add(table);
        tables.add(table);
        tables.add(table);

        return new MergeRequest(values, tables);
    }

    public Set<String> extractVariablesFromText(String text) {
        Set<String> variables = new HashSet<>();

        // scan for pattern: ${anything}
        Pattern pattern = Pattern.compile("\\$\\{(.*?)\\}");
        Matcher matcher = pattern.matcher(text);

        while(matcher.find()) {
            variables.add(matcher.group(1));
        }

        variables.remove("dynamic_table");

        return variables;
    }

    public String extractTextFromDocument() throws IOException {
        Document doc = service.documents().get(DOCUMENT_ID).execute();
        return readStructuralElements(doc.getBody().getContent());
    }

    /**
     * Recurses through a list of Structural Elements to read a document's text where text may be in
     * nested elements.
     *
     * @param elements a list of Structural Elements
     */
    private static String readStructuralElements(List<StructuralElement> elements) {
        StringBuilder sb = new StringBuilder();
        for (StructuralElement element : elements) {
            if (element.getParagraph() != null) {
                for (ParagraphElement paragraphElement : element.getParagraph().getElements()) {
                    sb.append(readParagraphElement(paragraphElement));
                }
            } else if (element.getTable() != null) {
                // The text in table cells are in nested Structural Elements and tables may be
                // nested.
                for (TableRow row : element.getTable().getTableRows()) {
                    for (TableCell cell : row.getTableCells()) {
                        sb.append(readStructuralElements(cell.getContent()));
                    }
                }
            } else if (element.getTableOfContents() != null) {
                // The text in the TOC is also in a Structural Element.
                sb.append(readStructuralElements(element.getTableOfContents().getContent()));
            }
        }
        return sb.toString();
    }

    /**
     * Returns the text in the given ParagraphElement.
     *
     * @param element a ParagraphElement from a Google Doc
     */
    private static String readParagraphElement(ParagraphElement element) {
        TextRun run = element.getTextRun();
        if (run == null || run.getContent() == null) {
            // The TextRun can be null if there is an inline object.
            return "";
        }
        return run.getContent();
    }
}
