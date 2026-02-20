package repository;

import model.Animal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long>{

    // Поиск животных по виду
    List<Animal> findBySpecies(String species);

    // Поиск животных по имени (частичное совпадение, без учета регистра)
    List<Animal> findByNameContainingIgnoreCase(String name);

    // Поиск животных младше определенного возраста
    List<Animal> findByAgeLessThan(Integer age);

    // Поиск животных старше определенного возраста
    List<Animal> findByAgeGreaterThan(Integer age);

    // Поиск животных по виду и возрастному диапазону
    List<Animal> findBySpeciesAndAgeBetween(String species, Integer minAge, Integer maxAge);

    // Поиск по возрастному диапазону
    List<Animal> findByAgeBetween(Integer minAge, Integer maxAge);

    // Подсчет количества животных по виду
    Long countBySpecies(String species);

    // Кастомный запрос - поиск животных, имена которых начинаются с определенной буквы
    @Query("SELECT a FROM Animal a WHERE LOWER(a.name) LIKE LOWER(CONCAT(:letter, '%'))")
    List<Animal> findByNameStartingWith(@Param("letter") String letter);

    // Кастомный запрос - получение всех видов животных
    @Query("SELECT DISTINCT a.species FROM Animal a ORDER BY a.species")
    List<String> findAllSpecies();

    // Поиск по нескольким видам
    @Query("SELECT a FROM Animal a WHERE a.species IN :speciesList")
    List<Animal> findBySpeciesIn(@Param("speciesList") List<String> speciesList);
}
