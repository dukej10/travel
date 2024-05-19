package com.dukez.best_travel.api.controllers;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dukez.best_travel.infrastructure.abstract_service.IReportService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/report")
@Tag(name = "Report", description = "Endpoints for the report catalog in the system.")
public class ReportController {
    private final IReportService reportService;

    @GetMapping
    public ResponseEntity<Resource> get() {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.xlsx");

        var fileInBytes = this.reportService.readFile();
        ByteArrayResource response = new ByteArrayResource(fileInBytes);

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(fileInBytes.length)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(response);
    }
}