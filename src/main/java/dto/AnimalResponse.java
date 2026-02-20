package dto;

import model.Animal;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AnimalResponse {
    private Long id;
    private String name;
    private String species;
    private Integer age;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static AnimalResponse fromEntity(Animal animal) {
        AnimalResponse response = new AnimalResponse();
        response.setId(animal.getId());
        response.setName(animal.getName());
        response.setSpecies(animal.getSpecies());
        response.setAge(animal.getAge());
        response.setCreatedAt(animal.getCreatedAt());
        response.setUpdatedAt(animal.getUpdatedAt());
        return response;
    }
}
