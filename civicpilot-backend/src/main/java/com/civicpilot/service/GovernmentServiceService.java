package com.civicpilot.service;

import com.civicpilot.entity.GovernmentService;
import com.civicpilot.repository.GovernmentServiceRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GovernmentServiceService {

    private final GovernmentServiceRepository repository;

    public GovernmentServiceService(
            GovernmentServiceRepository repository) {
        this.repository = repository;
    }

    public GovernmentService createService(
            GovernmentService service) {

        return repository.save(service);
    }

    public List<GovernmentService> getAllServices() {

        return repository.findAll();
    }

    public GovernmentService getServiceById(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Government service not found"));
    }

    public GovernmentService updateService(
            Long id,
            GovernmentService updatedService) {

        GovernmentService existing =
                getServiceById(id);

        existing.setName(updatedService.getName());
        existing.setDescription(updatedService.getDescription());
        existing.setCategory(updatedService.getCategory());
        existing.setDepartment(updatedService.getDepartment());
        existing.setEligibility(updatedService.getEligibility());
        existing.setRequiredDocuments(
                updatedService.getRequiredDocuments());
        existing.setProcedureDescription(
                updatedService.getProcedureDescription());

        return repository.save(existing);
    }

    public void deleteService(Long id) {

        if (!repository.existsById(id)) {
            throw new RuntimeException(
                    "Government service not found");
        }

        repository.deleteById(id);
    }
}