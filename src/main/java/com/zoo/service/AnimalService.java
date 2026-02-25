package com.zoo.service;

import com.zoo.service.FileStorageService;
import com.zoo.dto.AnimalRequest;
import com.zoo.dto.AnimalResponse;
import com.zoo.model.Animal;
import com.zoo.repository.AnimalRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.core.io.Resource;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AnimalService {

    private final AnimalRepository animalRepository;
    private final FileStorageService fileStorageService;

    @Transactional
    public AnimalResponse addAnimal(AnimalRequest request) {
        log.info("Добавление нового животного: {} ({})", request.getName(), request.getSpecies());

        Animal animal = new Animal();
        animal.setName(request.getName());
        animal.setSpecies(request.getSpecies());
        animal.setAge(request.getAge());

        Animal savedAnimal = animalRepository.save(animal);
        log.info("Животное добавлено с ID: {}", savedAnimal.getId());

        return AnimalResponse.fromEntity(savedAnimal);
    }

    public List<AnimalResponse> getAllAnimals() {
        log.info("Получение списка всех животных");
        return animalRepository.findAll()
                .stream()
                .map(AnimalResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public String saveAllAnimalsToJson() {
        List<Animal> animals = animalRepository.findAll();
        return fileStorageService.saveAnimalsToJson(animals);
    }

    public String saveAllAnimalsToCsv() {
        List<Animal> animals = animalRepository.findAll();
        return fileStorageService.saveAnimalsToCsv(animals);
    }

    public String saveAllAnimalsToTxt() {
        List<Animal> animals = animalRepository.findAll();
        return fileStorageService.saveAnimalsToTxt(animals);
    }

    public List<String> getSavedFiles() {
        return fileStorageService.getSavedFiles();
    }

    public Resource getFileAsResource(String filename) {
        return fileStorageService.loadFileAsResource(filename);
    }

    public boolean fileExists(String filename) {
        return fileStorageService.fileExists(filename);
    }

    public AnimalResponse getAnimalById(Long id) {
        log.info("Поиск животного по ID: {}", id);
        Animal animal = animalRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Животное с ID " + id + " не найдено"));
        return AnimalResponse.fromEntity(animal);
    }

    public List<AnimalResponse> getAnimalsBySpecies(String species) {
        log.info("Поиск животных по виду: {}", species);
        return animalRepository.findBySpeciesContainingIgnoreCase(species)
                .stream()
                .map(AnimalResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public List<AnimalResponse> getAnimalsByName(String name) {
        log.info("Поиск животных по имени: {}", name);
        return animalRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(AnimalResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public List<AnimalResponse> getYoungerAnimals(Integer age) {
        log.info("Поиск животных младше {} лет", age);
        return animalRepository.findByAgeLessThan(age)
                .stream()
                .map(AnimalResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public List<AnimalResponse> getOlderAnimals(Integer age) {
        log.info("Поиск животных старше {} лет", age);
        return animalRepository.findByAgeGreaterThan(age)
                .stream()
                .map(AnimalResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public List<AnimalResponse> getAnimalsByAgeRange(Integer minAge, Integer maxAge) {
        log.info("Поиск животных в возрасте от {} до {} лет", minAge, maxAge);
        return animalRepository.findByAgeBetween(minAge, maxAge)
                .stream()
                .map(AnimalResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public List<AnimalResponse> getAnimalsBySpeciesAndAgeRange(String species, Integer minAge, Integer maxAge) {
        log.info("Поиск животных вида {} в возрасте от {} до {} лет", species, minAge, maxAge);
        return animalRepository.findBySpeciesAndAgeBetween(species, minAge, maxAge)
                .stream()
                .map(AnimalResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public List<String> getAllSpecies() {
        log.info("Получение списка всех видов животных");
        return animalRepository.findAllSpecies();
    }

    public Long getCountBySpecies(String species) {
        log.info("Подсчет количества животных вида: {}", species);
        return animalRepository.countBySpecies(species);
    }

    @Transactional
    public AnimalResponse updateAnimal(Long id, AnimalRequest request) {
        log.info("Обновление информации о животном с ID: {}", id);

        Animal animal = animalRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Животное с ID " + id + " не найдено"));

        animal.setName(request.getName());
        animal.setSpecies(request.getSpecies());
        animal.setAge(request.getAge());

        Animal updatedAnimal = animalRepository.save(animal);
        log.info("Информация о животном с ID {} обновлена", id);

        return AnimalResponse.fromEntity(updatedAnimal);
    }

    @Transactional
    public void deleteAnimal(Long id) {
        log.info("Удаление животного с ID: {}", id);

        if (!animalRepository.existsById(id)) {
            throw new EntityNotFoundException("Животное с ID " + id + " не найдено");
        }

        animalRepository.deleteById(id);
        log.info("Животное с ID {} удалено", id);
    }

    @Transactional
    public void deleteAllAnimals() {
        log.info("Удаление всех животных");
        animalRepository.deleteAll();
        log.info("Все животные удалены");
    }
}
