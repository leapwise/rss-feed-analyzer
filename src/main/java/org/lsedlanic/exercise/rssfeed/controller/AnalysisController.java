package org.lsedlanic.exercise.rssfeed.controller;

import org.lsedlanic.exercise.rssfeed.models.AnalysisResult;
import org.lsedlanic.exercise.rssfeed.service.AnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class AnalysisController {
    @Autowired
    private AnalysisService service;

    @PostMapping("/analyse/new")
    public ResponseEntity<UUID> analyzeFeeds(@RequestBody List<String> urls) {
        try {
            UUID analysisId = service.analyzeFeeds(urls);
            return new ResponseEntity<>(analysisId, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/frequency/{id}")
    public ResponseEntity<List<AnalysisResult>> getTopResults(@PathVariable UUID id) {
        try {
            List<AnalysisResult> topResults = service.getTopResults(id);
            return new ResponseEntity<>(topResults, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    }

