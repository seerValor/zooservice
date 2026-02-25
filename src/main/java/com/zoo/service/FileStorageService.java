package com.zoo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.zoo.model.Animal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.net.MalformedURLException;

@Service
@Slf4j
public class FileStorageService {

    private final ObjectMapper objectMapper;
    private final String storageDirectory;

    public FileStorageService(@Value("${app.storage.directory:./storage}") String storageDirectory) {
        this.storageDirectory = storageDirectory;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        // Создаем директорию для хранения, если её нет
        createStorageDirectory();
    }

    private void createStorageDirectory() {
        try {
            Path path = Paths.get(storageDirectory);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
                log.info("Создана директория для хранения: {}", storageDirectory);
            }
        } catch (IOException e) {
            log.error("Ошибка при создании директории: {}", e.getMessage());
        }
    }

    /**
     * Сохранить список животных в JSON файл
     */
    public String saveAnimalsToJson(List<Animal> animals) {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String filename = String.format("animals_%s.json", timestamp);
            Path filePath = Paths.get(storageDirectory, filename);

            objectMapper.writeValue(filePath.toFile(), animals);
            log.info("Сохранено {} животных в файл: {}", animals.size(), filePath);

            return filePath.toString();
        } catch (IOException e) {
            log.error("Ошибка при сохранении в файл: {}", e.getMessage());
            throw new RuntimeException("Не удалось сохранить животных в файл", e);
        }
    }

    /**
     * Сохранить список животных в CSV файл
     */
    public String saveAnimalsToCsv(List<Animal> animals) {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String filename = String.format("animals_%s.csv", timestamp);
            Path filePath = Paths.get(storageDirectory, filename);

            StringBuilder csv = new StringBuilder();
            // Заголовки
            csv.append("ID,Name,Species,Age,CreatedAt\n");

            // Данные
            for (Animal animal : animals) {
                csv.append(String.format("%d,\"%s\",\"%s\",%d,%s\n",
                        animal.getId(),
                        animal.getName(),
                        animal.getSpecies(),
                        animal.getAge(),
                        animal.getCreatedAt() != null ? animal.getCreatedAt().toString() : "null"
                ));
            }

            Files.write(filePath, csv.toString().getBytes());
            log.info("Сохранено {} животных в CSV файл: {}", animals.size(), filePath);

            return filePath.toString();
        } catch (IOException e) {
            log.error("Ошибка при сохранении в CSV: {}", e.getMessage());
            throw new RuntimeException("Не удалось сохранить животных в CSV файл", e);
        }
    }

    /**
     * Сохранить список животных в текстовый файл (простой формат)
     */
    public String saveAnimalsToTxt(List<Animal> animals) {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String filename = String.format("animals_%s.txt", timestamp);
            Path filePath = Paths.get(storageDirectory, filename);

            StringBuilder content = new StringBuilder();
            content.append("СПИСОК ЖИВОТНЫХ В ЗООПАРКЕ\n");
            content.append("=".repeat(50)).append("\n");
            content.append(String.format("Всего животных: %d\n", animals.size()));
            content.append("=".repeat(50)).append("\n\n");

            for (int i = 0; i < animals.size(); i++) {
                Animal animal = animals.get(i);
                content.append(String.format("%d. %s (%s) - %d лет\n",
                        i + 1,
                        animal.getName(),
                        animal.getSpecies(),
                        animal.getAge()
                ));
            }

            content.append("\n").append("=".repeat(50)).append("\n");
            content.append(String.format("Дата выгрузки: %s\n",
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"))));

            Files.write(filePath, content.toString().getBytes());
            log.info("Сохранено {} животных в TXT файл: {}", animals.size(), filePath);

            return filePath.toString();
        } catch (IOException e) {
            log.error("Ошибка при сохранении в TXT: {}", e.getMessage());
            throw new RuntimeException("Не удалось сохранить животных в TXT файл", e);
        }
    }

    /**
     * Получить список всех сохраненных файлов
     */
    public List<String> getSavedFiles() {
        try {
            return Files.list(Paths.get(storageDirectory))
                    .filter(Files::isRegularFile)
                    .map(Path::getFileName)         // получаем имя файла
                    .map(Path::toString)            // преобразуем в строку
                    .toList();
        } catch (IOException e) {
            log.error("Ошибка при получении списка файлов: {}", e.getMessage());
            return List.of();
        }
    }

    public Resource loadFileAsResource(String filename) {
        try {
            Path filePath = Paths.get(storageDirectory).resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("Файл не найден: " + filename);
            }
        } catch (MalformedURLException ex) {
            throw new RuntimeException("Ошибка при загрузке файла: " + filename, ex);
        }
    }

    public boolean fileExists(String filename) {
        Path filePath = Paths.get(storageDirectory).resolve(filename).normalize();
        return Files.exists(filePath) && Files.isRegularFile(filePath);
    }
}