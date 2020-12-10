package com.uplora.googledocs.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FinalTextAndTables {

    private String finalText;
    private List<String> tables;
}
