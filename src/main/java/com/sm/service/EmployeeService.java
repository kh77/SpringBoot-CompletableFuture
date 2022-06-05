package com.sm.service;

import com.sm.orm.entity.Employee;
import com.sm.orm.repository.EmployeeRepository;
import com.sm.util.CsvUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@AllArgsConstructor
public class EmployeeService {
    private EmployeeRepository employeeRepository;

    @Async
    public CompletableFuture<List<Employee>> parseFileAndSaveEmployee(final InputStream inputStream) throws Exception {
        final long start = System.currentTimeMillis();
        List<Employee> employeeList = CsvUtil.fileToEmployee(inputStream);
        log.info("Size of employee list {}", employeeList.size());

        employeeList = employeeRepository.saveAll(employeeList);

        log.info("Time consumed: {}", (System.currentTimeMillis() - start));
        return CompletableFuture.completedFuture(employeeList);
    }

    @Async
    public CompletableFuture<List<Employee>> getAllEmployees() {
        log.info("List of employees");
        final List<Employee> employeeList = employeeRepository.findAll();
        return CompletableFuture.completedFuture(employeeList);
    }


    @Async
    public CompletableFuture<List<Employee>> findByName(String name) {
        log.info("List of employees by name.");
        List<Employee> employeeList = employeeRepository.findByNameIgnoreCase(name);
        return CompletableFuture.completedFuture(employeeList);
    }

    @Async
    public CompletableFuture<List<Employee>> findByAge(Integer age) {
        log.info("List of employees by age");
        List<Employee> employeeList = employeeRepository.findByAge(age);
        return CompletableFuture.completedFuture(employeeList);
    }
    public List<Employee> combineFutureData(List<Employee>... employee) {
        List<Employee> resultList = new ArrayList<>();
        for (List<Employee> list : employee) {
            resultList.addAll(list);
        }
        return resultList;
    }
}
