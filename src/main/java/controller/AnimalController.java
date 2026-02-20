package controller;

import dto.AnimalRequest;
import dto.AnimalResponse;
import service.AnimalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}