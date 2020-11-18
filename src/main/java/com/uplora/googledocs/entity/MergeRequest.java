package com.uplora.googledocs.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.List;

@Data
@AllArgsConstructor
public class MergeRequest {

    private List<Value> values;
    private List<LinkedHashMap<String, String>> table;

}
