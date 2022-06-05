package com.sm.controller;

import com.sm.orm.entity.Employee;
import com.sm.service.EmployeeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

@RestController
@RequestMapping("/api/employee")
@Slf4j
@AllArgsConstructor
public class EmployeeController {

    private EmployeeService employeeService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity uploadFile(@RequestParam(value = "files") MultipartFile[] files) {
        try {
            for (final MultipartFile file : files) {
                employeeService.parseFileAndSaveEmployee(file.getInputStream());
            }
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (final Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public CompletableFuture<ResponseEntity> getAllEmployees() {
        return employeeService.getAllEmployees().<ResponseEntity>thenApply(ResponseEntity::ok).exceptionally(handleGetCarFailure);
    }

    @GetMapping("/{name}/{age}")
    public CompletableFuture<ResponseEntity> findByNameOrAge(@PathVariable Optional<String> name, @PathVariable Optional<Integer> age) throws ExecutionException, InterruptedException {

        CompletableFuture<List<Employee>> employeeByName = employeeService.findByName(name.orElse(""));
        CompletableFuture<List<Employee>> employeeByAge = employeeService.findByAge(age.orElse(0));

        CompletableFuture.allOf(employeeByName, employeeByAge).join();
        return CompletableFuture.completedFuture(employeeService.combineFutureData(employeeByName.get(), employeeByAge.get())).
                <ResponseEntity>thenApply(ResponseEntity::ok)
                .exceptionally(handleGetCarFailure);
    }

    private static Function<Throwable, ResponseEntity<? extends List<Employee>>> handleGetCarFailure = throwable -> {
        log.error("Failed to read records: {}", throwable);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    };
}
