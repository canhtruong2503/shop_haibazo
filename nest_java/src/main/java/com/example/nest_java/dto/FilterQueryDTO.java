package com.example.nest_java.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FilterQueryDTO {
    private Map<String, ArrayList<String>> filter;

    private Map<String, ArrayList<String>> skip;

 //   private Map<String, Object> extend;

    private Map<String, String> where;

    private String fullTextSearch;

}
