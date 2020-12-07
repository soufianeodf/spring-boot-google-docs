package com.uplora.googledocs.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
@AllArgsConstructor
public class MergeRequestV2 {

    private List<ResponseValue> values;
    private List<List<Cell>> table;

}
