package com.uplora.googledocs.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleTextAndEndIndex {

    private StringBuilder simpleText;
    private List<Integer> endIndex;
}
