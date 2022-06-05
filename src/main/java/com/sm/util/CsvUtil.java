package com.sm.util;

import com.sm.orm.entity.Employee;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CsvUtil {
    public static String TYPE = "text/csv";

    public static boolean hasCSVFormat(MultipartFile file) {
        if (!TYPE.equalsIgnoreCase(file.getContentType())) {
            return false;
        }
        return true;
    }

    public static List<Employee> fileToEmployee(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8")); CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

            List<Employee> employeeList = new ArrayList<>();
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                Employee employee = Employee.builder().name(csvRecord.get("name")).age(Integer.valueOf(csvRecord.get("age").trim())).department(csvRecord.get("department")).build();
                employeeList.add(employee);
            }

            return employeeList;
        } catch (IOException e) {
            throw new RuntimeException("failed to parse CSV file: " + e.getMessage());
        }
    }
}
