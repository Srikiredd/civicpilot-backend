package com.civicpilot.controller;

import com.civicpilot.entity.GovernmentService;
import com.civicpilot.service.GovernmentServiceService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
public class GovernmentServiceController {

    private final GovernmentServiceService service;

    public GovernmentServiceController(
            GovernmentServiceService service) {

        this.service = service;
    }

    @PostMapping
    public ResponseEntity<GovernmentService> createService(
            @RequestBody GovernmentService governmentService) {

        return new ResponseEntity<>(
                service.createService(governmentService),
                HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<GovernmentService>> getAllServices() {

        return ResponseEntity.ok(
                service.getAllServices());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GovernmentService> getService(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getServiceById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GovernmentService> updateService(
            @PathVariable Long id,
            @RequestBody GovernmentService governmentService) {

        return ResponseEntity.ok(
                service.updateService(
                        id,
                        governmentService));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteService(
            @PathVariable Long id) {

        service.deleteService(id);

        return ResponseEntity.ok(
                "Government service deleted successfully");
    }
}