package dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AnimalRequest {

    @NotBlank(message = "Имя животного не может быть пустым")
    @Size(min = 2, max = 100, message = "Имя должно содержать от 2 до 100 символов")
    private String name;

    @NotBlank(message = "Вид животного не может быть пустым")
    @Size(min = 2, max = 100, message = "Вид должен содержать от 2 до 100 символов")
    private String species;

    @NotNull(message = "Возраст не может быть пустым")
    @Min(value = 0, message = "Возраст должен быть положительным числом")
    private Integer age;
}
