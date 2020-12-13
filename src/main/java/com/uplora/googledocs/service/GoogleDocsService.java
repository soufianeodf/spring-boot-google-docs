package com.uplora.googledocs.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
import com.uplora.googledocs.entity.*;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Map.Entry.comparingByKey;
import static java.util.stream.Collectors.toMap;

@Service
public class GoogleDocsService {

    private final Docs service;
    private final NetHttpTransport HTTP_TRANSPORT;

    private static final String APPLICATION_NAME = "Google Docs API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static String documentId;

    private static List<String> objectIds = new ArrayList<>();

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

    public void main(String documentId) throws IOException {

        // Prints the title of the requested doc:
        // https://docs.google.com/document/d/195j9eDD3ccgjQRttHhJPymLJUCOUjs-jmwTrekvdjFE/edit
        /*Document response = null;
        try {
            response = service.documents().get(documentId).execute();
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
                .batchUpdate(documentId, body).execute();*/

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
                .batchUpdate(documentId, body).execute();*/

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
                .batchUpdate(documentId, body).execute();*/
        Instant start = Instant.now();

        this.documentId = documentId;
        Document response = service.documents().get(documentId).execute();
        List<Request> requests = new ArrayList<>();

        Instant var00 = Instant.now();
        System.out.println("***00---> " + Duration.between(start, var00));

        FinalTextAndTables finalTextAndTables = extractTextFromDocument(response);

        Instant var0 = Instant.now();
        System.out.println("***0---> " + Duration.between(var00, var0));

        List<ResponseValue> values = getValuesFromLevel0API(extractVariablesFromText(finalTextAndTables.getFinalText()));

        Instant var1 = Instant.now();
        System.out.println("***1---> " + Duration.between(var0, var1));

        List<List<HashMap<String, String>>> theTables = new ArrayList<>();  

        finalTextAndTables.getTables().forEach(e -> {
            try {
                theTables.add(getValuesFromLevel1API(extractVariablesFromText(e), objectIds.get(finalTextAndTables.getTables().indexOf(e))));
            } catch (JsonProcessingException jsonProcessingException) {
                jsonProcessingException.printStackTrace();
            }
        });

        Instant var2 = Instant.now();
        System.out.println("***2---> " + Duration.between(var1, var2));

        MergeRequest mergeRequest = new MergeRequest(values, theTables);

        AtomicInteger i = new AtomicInteger(1);
        mergeRequest.getTables().stream().forEach(e -> {
            try {
                transformTheTable(e, i.getAndIncrement());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        Instant var3 = Instant.now();
        System.out.println("***3---> " + Duration.between(var2, var3));

        mergeText(mergeRequest.getValues(), requests);

        Instant var4 = Instant.now();
        System.out.println("***4---> " + Duration.between(var3, var4));

        // transform list to set to remove redundancy
        Set<String> helperAnnotations = new HashSet<>(objectIds);
        helperAnnotations.add("dynamic_table");
        helperAnnotations.forEach(e -> {
            removeHelperAnnotations(e, requests);
        });

        Instant var5 = Instant.now();
        System.out.println("***5---> " + Duration.between(var4, var5));

        BatchUpdateDocumentRequest body = new BatchUpdateDocumentRequest();
        service.documents().batchUpdate(documentId, body.setRequests(requests)).execute();

        Instant var6 = Instant.now();
        System.out.println("***6---> " + Duration.between(var5, var6));

        Instant end = Instant.now();
        System.out.println("***end---> " + Duration.between(start, end));
    }

    public void removeHelperAnnotations(String helperAnnotationName, List<Request> requests) {
        // remove helper annotation from document
            requests.add(new Request()
                    .setReplaceAllText(new ReplaceAllTextRequest()
                            .setContainsText(new SubstringMatchCriteria()
                                    .setText("${" + helperAnnotationName + "}")
                                    .setMatchCase(true))
                            .setReplaceText("")));
    }

    public void transformTheTable(List<HashMap<String, String>> table, int helperAnnotationNumber) throws IOException {


        System.out.println("--------------------------->" + helperAnnotationNumber);


        Instant start = Instant.now();

        int endIndexOfDynamicTableWord = 0;
        int indexOfFirstCell = 0;
        int tableRowsNumber = table.size();
        int tableColumnsNumber = table.get(0).size();
        String helperAnnotationName = "${dynamic_table}";

        Document response = service.documents().get(documentId).execute();

        Instant var0 = Instant.now();
        System.out.println("0---> " + Duration.between(start, var0));

        List<StructuralElement> elements = response.getBody().getContent();

        Instant var1 = Instant.now();
        System.out.println("1---> " + Duration.between(var0, var1));

        int numberOfOccurrences = 1;
        for (StructuralElement element : elements) {
            // get the endIndex of "${dynamic_table}"
            if(element.getParagraph() != null) {
                for (ParagraphElement paragraphElement : element.getParagraph().getElements()) {
                    if(paragraphElement.getTextRun().getContent().contains(helperAnnotationName + "\n")) {
                        if(numberOfOccurrences == helperAnnotationNumber) {
                            endIndexOfDynamicTableWord = paragraphElement.getEndIndex();
                        }
                        numberOfOccurrences++;
                    } else if(paragraphElement.getTextRun().getContent().contains(helperAnnotationName)) {
                        if(numberOfOccurrences == helperAnnotationNumber) {
                            endIndexOfDynamicTableWord = paragraphElement.getEndIndex() + 1;
                        }
                        numberOfOccurrences++;
                    }
                }
            }
        }

        Instant var2 = Instant.now();
        System.out.println("2---> " + Duration.between(var1, var2));

        for (StructuralElement element : elements) {
            // get the sum of the lengths of the table title's
            if (element.getTable() != null && element.getEndIndex() > endIndexOfDynamicTableWord) {
                tableColumnsNumber = element.getTable().getColumns();
                for (TableCell cell : element.getTable().getTableRows().get(0).getTableCells()) {
                    indexOfFirstCell += cell.getContent().get(0)
                            .getParagraph().getElements().get(0)
                            .getTextRun().getContent()
                            .replace("\n", "").length();
                }
                // break after you find the first table that respect the conditions
                break;
            }
        }

        Instant var3 = Instant.now();
        System.out.println("3---> " + Duration.between(var2, var3));

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

        Instant var4 = Instant.now();
        System.out.println("4---> " + Duration.between(var3, var4));

        // delete first row from table
        requests.add(new Request().setDeleteTableRow(new DeleteTableRowRequest()
                .setTableCellLocation(new TableCellLocation()
                        .setTableStartLocation(new Location()
                                .setIndex(endIndexOfDynamicTableWord))
                        .setRowIndex(1)
                        .setColumnIndex(1))));

        Instant var5 = Instant.now();
        System.out.println("5---> " + Duration.between(var4, var5));

        // fill cells of table with related values
        int j = 0;
        for (HashMap<String, String> row: table) {
            j++;
            // transform Map to List
            List<String> list = row.values().stream().collect(Collectors.toList());
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

        BatchUpdateDocumentRequest body = new BatchUpdateDocumentRequest();
        service.documents().batchUpdate(documentId, body.setRequests(requests)).execute();

        Instant var6 = Instant.now();
        System.out.println("6---> " + Duration.between(var5, var6));
    }

    public void mergeText(List<ResponseValue> values, List<Request> requests) throws IOException {
        values.forEach(value -> {
            requests.add(new Request()
                    .setReplaceAllText(new ReplaceAllTextRequest()
                            .setContainsText(new SubstringMatchCriteria()
                                    .setText("${" + value.getId() + "__f}")
                                    .setMatchCase(true))
                            .setReplaceText(value.getName() != null ? value.getName() : value.getValue())));
        });
    }

    public MergeRequest getValuesFromEndpoint(Set<String> list) {

        List<ResponseValue> values = new ArrayList<>();
        values.add(new ResponseValue("company_name", "Uplora", ""));
        values.add(new ResponseValue("company_address", "uplora company street", ""));
        values.add(new ResponseValue("company_city", "uplora company city", ""));
        values.add(new ResponseValue("prepared_date", "Jun 06, 2020", ""));
        values.add(new ResponseValue("exp_date", "Aug 06, 2020", ""));
        values.add(new ResponseValue("customer_name", "john doe", ""));
        values.add(new ResponseValue("customer_street", "customer street", ""));
        values.add(new ResponseValue("customer_city", "customer city", ""));

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

        return new MergeRequest(values, tables);
    }

    public List<ResponseValue> getValuesFromLevel0API(Set<String> list) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://d2.uplora.com/api/coreapi/objects/1000135000000000/records/1";

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("X-Authorization", "Bearer " + getAccessToken());

        ObjectMapper objectMapper = new ObjectMapper();

        List<RequestValue> request = new ArrayList<>();

        list.forEach(e -> {
            RequestValue value = new RequestValue(
                    e.substring(0, e.length() - 3),
                    e
            );
            request.add(value);
        });

        ArrayNode body = objectMapper.valueToTree(request);

        HttpEntity<String> entity = new HttpEntity<>(
                body.toString()
                , headers
        );

        ResponseEntity<ResponseValue[]> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, ResponseValue[].class);
        ResponseValue[] values = responseEntity.getBody();

        return Arrays.asList(values);
    }

    public List<HashMap<String, String>> getValuesFromLevel1API(Set<String> list, String objectNumber) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://d2.uplora.com/api/dyndata/objects/" + objectNumber.substring(0, objectNumber.length() - 3) + "/records/search?limit=100&tenantId=ravi&userId=10";

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("X-Authorization", "Bearer " + getAccessToken());

        ObjectMapper objectMapper = new ObjectMapper();

        List<RequestValue> request = new ArrayList<>();

/*        list.forEach(e -> {
            RequestValue value = new RequestValue(
                    e.substring(2, e.length() - 4),
                    e.substring(2, e.length() - 1)
            );
            request.add(value);
        });*/

        list.forEach(e -> {
            RequestValue value = new RequestValue(
                    e.substring(0, e.length() - 3),
                    e
            );
            request.add(value);
        });

        ObjectNode body = (ObjectNode) objectMapper.readTree("{}");
        body.putArray("fields").addAll((ArrayNode) objectMapper.valueToTree(request));
        body.putArray("conditions").add(objectMapper.readTree("{\"rules\":[{\"field\":\"parentId__sys\",\"value\":2,\"operator\":\"=\"}],\"condition\":\"and\"}"));

        HttpEntity<String> entity = new HttpEntity<>(
                body.toString()
                , headers
        );

        ResponseEntity<List> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, List.class);
        List<HashMap<String, String>> table = new ArrayList<>();
        List<List<HashMap<String, String>>> tempTable = responseEntity.getBody();
        tempTable.forEach(e -> {
            HashMap<String, String> row = new LinkedHashMap<>();
            for(HashMap<String, String> theRow : e) {
                List<String> element = new ArrayList<>(theRow.values());
                row.put(element.get(0), String.valueOf(element.get(1)));
            }
            table.add(row);
        });
        return table;
    }

    private String getAccessToken() {
        return "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkB0b2tlbGF1LmNvbSIsImp0aSI6InRva2VsYXUiLCJjb2NvVXNlcklkIjoxMCwiYnUiOiJBZG1pbmlzdHJhdG9ycyIsImJ1c2luZXNzVW5pdElkIjoxMSwic2NvcGVzIjpbIkdSQU5UX0RBU0hCT0FSRF9SRUFEIiwiR1JBTlRfQU5BTFlUSUNTX1JFQUQiLCJHUkFOVF9DUkVBVEVfQUNDRVNTIiwiR1JBTlRfRVNJR04iLCJHUkFOVF9FTUFJTCIsIkdSQU5UX0FOQUxZVElDU19DUkVBVEUiLCJHUkFOVF9BUFBST1ZBTF9BQ0NFU1MiLCJHUkFOVF9XRl9BQ0NFU1MiLCJHUkFOVF9QUklOVF9BQ0NFU1MiLCJHUkFOVF9SRVBPUlRfUkVBRCIsIkdSQU5UX0FETUlOIiwiR1JBTlRfUkVQT1JUX0NSRUFURSJdLCJpc3MiOiJodHRwOi8vdXBsb3JhLmNvbSIsImlhdCI6MTYwNzg4MzQ3MiwiZXhwIjoxNjA3OTY3NDcyfQ.r26Ek8Jn2GNtMHGVPwcr5kGOfQBAWAxYVse7D6uNZHSkceLUrInvGV7Kg2zbD1vPLsfOayzqb4b6sp40TGpfGQ";
    }

    public Set<String> extractVariablesFromText(String text) {
        Set<String> variables = new HashSet<>();

        // scan for pattern: ${anything_that_do_not_end_with_underscore_underscore_o}
        Pattern pattern = Pattern.compile("\\$\\{([^}]*?[^__o])\\}");
        Matcher matcher = pattern.matcher(text);

        while(matcher.find()) {
            variables.add(matcher.group(1));
        }

        variables.remove("dynamic_table");

        return variables;
    }

    public FinalTextAndTables extractTextFromDocument(Document doc) {
        List<StructuralElement> content = doc.getBody().getContent();
        Map<Integer, String> startIndexAndTablesContent = readStructuralElementsAndGetTablesContent(content);

        Map<Integer, String> startIndexAndTablesContentSorted = startIndexAndTablesContent
                .entrySet()
                .stream()
                .sorted(comparingByKey())
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

        SimpleTextAndEndIndex simpleTextAndEndIndex = readStructuralElementsAndGetSimpleText(content);

        List<String> tables = new ArrayList<>();
        StringBuilder finalText = new StringBuilder();
        finalText.append(simpleTextAndEndIndex.getSimpleText());
        int i = 0;
        for (Map.Entry<Integer, String> entry : startIndexAndTablesContentSorted.entrySet()) {
            if(simpleTextAndEndIndex.getEndIndex().size() > i && (entry.getKey().equals(simpleTextAndEndIndex.getEndIndex().get(i)) || entry.getKey().equals(simpleTextAndEndIndex.getEndIndex().get(i)+1))) {
                tables.add(entry.getValue());
                i++;
            } else {
                finalText.append(entry.getValue());
            }
        }

        return new FinalTextAndTables(finalText.toString(), tables);
    }

    private static Map<Integer, String> readStructuralElementsAndGetTablesContent(List<StructuralElement> elements) {
        Map<Integer, String> result = new HashMap<>();
        Integer startIndexList;
        for (StructuralElement theElement : elements) {
             if (theElement.getTable() != null) {
                // The text in table cells are in nested Structural Elements and tables may be
                // nested.
                startIndexList = theElement.getStartIndex();
                StringBuilder tableContent = new StringBuilder();
                for (TableRow row : theElement.getTable().getTableRows()) {
                    for (TableCell cell : row.getTableCells()) {
                        for (StructuralElement element : cell.getContent()) {
                            if (element.getParagraph() != null) {
                                for (ParagraphElement paragraphElement : element.getParagraph().getElements()) {
                                    tableContent.append(readParagraphElement(paragraphElement));
                                }
                            }
                        }
                    }
                }
                 result.put(startIndexList, tableContent.toString());
            }
        }

        return result;
    }

    private static SimpleTextAndEndIndex readStructuralElementsAndGetSimpleText(List<StructuralElement> elements) {
        StringBuilder sb = new StringBuilder();
        List<Integer> endIndexList = new ArrayList<>();
        Pattern p = Pattern.compile("\\$\\{([^}]*?__o)\\}");

        for (StructuralElement element : elements) {
            if (element.getParagraph() != null) {
                for (ParagraphElement paragraphElement : element.getParagraph().getElements()) {
                    String content = readParagraphElement(paragraphElement);
                    Matcher matcher = p.matcher(content);
                    if(content.contains("${dynamic_table}")) {
                        endIndexList.add(paragraphElement.getEndIndex());
                    }
                    if(matcher.find()) {
//                        System.out.println("regex ----------->" + matcher.group(1));
                        objectIds.add(matcher.group(1));
                    }
                    sb.append(content);
                }
            } else if (element.getTableOfContents() != null) {
                // The text in the TOC is also in a Structural Element.
                sb.append(readStructuralElementsAndGetSimpleText(element.getTableOfContents().getContent()));
            }
        }
        return new SimpleTextAndEndIndex(sb, endIndexList);
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

    public String outputDocAsJson() throws IOException {
        Document response = service.documents().get("1fNaafF58aErUjgrIqMN6hNhuIsCSLAGRJIgF-ZawqCU").execute();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(response);
    }
}
