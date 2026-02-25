package com.zoo.controller;

import com.zoo.dto.AnimalRequest;
import com.zoo.dto.AnimalResponse;
import com.zoo.service.AnimalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;


import java.util.List;

@RestController
@RequestMapping("/api/animals")
@RequiredArgsConstructor
public class AnimalController {

    private final AnimalService animalService;

    @PostMapping
    public ResponseEntity<AnimalResponse> addAnimal(@Valid @RequestBody AnimalRequest request) {
        AnimalResponse createdAnimal = animalService.addAnimal(request);
        return new ResponseEntity<>(createdAnimal, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AnimalResponse>> getAllAnimals() {
        List<AnimalResponse> animals = animalService.getAllAnimals();
        return ResponseEntity.ok(animals);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnimalResponse> getAnimalById(@PathVariable Long id) {
        AnimalResponse animal = animalService.getAnimalById(id);
        return ResponseEntity.ok(animal);
    }

    @GetMapping("/species/{species}")
    public ResponseEntity<List<AnimalResponse>> getAnimalsBySpecies(@PathVariable String species) {
        List<AnimalResponse> animals = animalService.getAnimalsBySpecies(species);
        return ResponseEntity.ok(animals);
    }

    @GetMapping("/search")
    public ResponseEntity<List<AnimalResponse>> searchAnimalsByName(@RequestParam String name) {
        List<AnimalResponse> animals = animalService.getAnimalsByName(name);
        return ResponseEntity.ok(animals);
    }

    @GetMapping("/younger/{age}")
    public ResponseEntity<List<AnimalResponse>> getYoungerAnimals(@PathVariable Integer age) {
        List<AnimalResponse> animals = animalService.getYoungerAnimals(age);
        return ResponseEntity.ok(animals);
    }

    @GetMapping("/older/{age}")
    public ResponseEntity<List<AnimalResponse>> getOlderAnimals(@PathVariable Integer age) {
        List<AnimalResponse> animals = animalService.getOlderAnimals(age);
        return ResponseEntity.ok(animals);
    }

    @GetMapping("/age-range")
    public ResponseEntity<List<AnimalResponse>> getAnimalsByAgeRange(
            @RequestParam Integer min,
            @RequestParam Integer max) {
        List<AnimalResponse> animals = animalService.getAnimalsByAgeRange(min, max);
        return ResponseEntity.ok(animals);
    }

    @GetMapping("/species/{species}/age-range")
    public ResponseEntity<List<AnimalResponse>> getAnimalsBySpeciesAndAgeRange(
            @PathVariable String species,
            @RequestParam Integer min,
            @RequestParam Integer max) {
        List<AnimalResponse> animals = animalService.getAnimalsBySpeciesAndAgeRange(species, min, max);
        return ResponseEntity.ok(animals);
    }

    @GetMapping("/species/list")
    public ResponseEntity<List<String>> getAllSpecies() {
        List<String> species = animalService.getAllSpecies();
        return ResponseEntity.ok(species);
    }

    @GetMapping("/species/{species}/count")
    public ResponseEntity<Long> getCountBySpecies(@PathVariable String species) {
        Long count = animalService.getCountBySpecies(species);
        return ResponseEntity.ok(count);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnimalResponse> updateAnimal(
            @PathVariable Long id,
            @Valid @RequestBody AnimalRequest request) {
        AnimalResponse updatedAnimal = animalService.updateAnimal(id, request);
        return ResponseEntity.ok(updatedAnimal);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnimal(@PathVariable Long id) {
        animalService.deleteAnimal(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllAnimals() {
        animalService.deleteAllAnimals();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/export/json")
    public ResponseEntity<String> exportAnimalsToJson() {
        String filePath = animalService.saveAllAnimalsToJson();
        return ResponseEntity.ok("Животные экспортированы в файл: " + filePath);
    }

    @GetMapping("/export/csv")
    public ResponseEntity<String> exportAnimalsToCsv() {
        String filePath = animalService.saveAllAnimalsToCsv();
        return ResponseEntity.ok("Животные экспортированы в файл: " + filePath);
    }

    @GetMapping("/export/txt")
    public ResponseEntity<String> exportAnimalsToTxt() {
        String filePath = animalService.saveAllAnimalsToTxt();
        return ResponseEntity.ok("Животные экспортированы в файл: " + filePath);
    }

    @GetMapping("/export")
    public ResponseEntity<List<String>> getFilesList() {
        return ResponseEntity.ok(animalService.getSavedFiles());
    }

    @GetMapping("/export/download/{filename:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        // Проверяем, существует ли файл
        if (!animalService.fileExists(filename)) {
            return ResponseEntity.notFound().build();
        }

        try {
            // Загружаем файл как ресурс
            Resource resource = animalService.getFileAsResource(filename);

            // Определяем Content-Type на основе расширения
            String contentType = determineContentType(filename);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + resource.getFilename() + "\"")
                    .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    private String determineContentType(String filename) {
        if (filename.endsWith(".json")) {
            return "application/json";
        } else if (filename.endsWith(".csv")) {
            return "text/csv";
        } else if (filename.endsWith(".txt")) {
            return "text/plain";
        } else {
            return "application/octet-stream";
        }
    }
}