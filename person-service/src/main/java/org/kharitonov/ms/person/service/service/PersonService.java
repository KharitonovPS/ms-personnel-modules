package org.kharitonov.ms.person.service.service;

import lombok.RequiredArgsConstructor;
import org.kharitonov.ms.person.service.domain.Person;
import org.kharitonov.ms.person.service.mapper.PersonDTOMapper;
import org.kharitonov.ms.person.service.repository.PersonRepo;
import org.kharitonov.ms.person.service.util.PersonNotFoundException;
import org.kharitonov.ms.person.service.util.PredicateByKey;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepo personRepo;
    private final PersonDTOMapper personDTOMapper;

    // Задача 1: Фильтрация и преобразование
    // Отфильтруй людей с четным возрастом, умножьте возраст на 2.
    // Верните список результатов.
    public List<Person> filterMultiplyEven() {
        List<Person> personList = personRepo.findAll();
        return personList.stream()
                .filter(p -> p.getAge() % 2 == 0)
                .map(person -> new Person(person.getId(), person.getName(), person.getAge() * 2))
                .toList();
    }

    // Задача 2: Сортировка пользователей
    // Отфильтруй список людей по возрасту в порядке возрастания.
    public List<Person> sortByAge() {
        List<Person> personList = personRepo.findAll();
        return personList.stream()
                .sorted(Comparator.comparingInt(Person::getAge))
                .toList();
    }

    // Задача 3: Группировка элементов
    // Сгруппируй людей по первой букве в имени
    public Map<Character, List<Person>> sortByFirstCharOfName() {
        List<Person> personList = personRepo.findAll();
        return personList.stream()
                .collect(groupingBy(person -> person.getName().charAt(0)));
    }

    // Задача 4: Поиск максимального возраста
    // Найди человека с максимальным возрастом
    public Person findMaxAge() {
        List<Person> personList = personRepo.findAll();
        return personList.stream()
                .max(Comparator.comparing(Person::getAge))
                .orElseThrow(NoSuchElementException::new);
    }

    // Задача 5: Пропуск и ограничение
    // Пропусти первые 2 элемента и возьмите следующие 3 элемента из списка.
    public List<Person> skipAndLimitation() {
        List<Person> personList = personRepo.findAll();
        return personList.stream()
                .skip(2)
                .limit(3)
                .collect(Collectors.toList());
    }

    // Задача 6: Объединение имен
    // Объедини все имена людей в одну строку, разделяя их запятой.
    public String personNamesAsString() {
        List<Person> personList = personRepo.findAll();
        return personList.stream()
                .map(Person::getName)
                .collect(Collectors.joining(","));
    }

    // Задача 7: Проверка условия
    // Проверь, есть ли хотя бы один человек несовершеннолетний.
    public Boolean isUnderage() {
        List<Person> personList = personRepo.findAll();
        long count = personList.stream()
                .filter(p -> p.getAge() < 18)
                .count();
        return count > 0;
    }

    // Задача 8: Трансформация возрастов
    // Увеличь возраст каждого человека на 10% и верните новый список.
    public List<Person> increaseAgeByTenPercent() {
        List<Person> personList = personRepo.findAll();
        return personList.stream()
                .peek(person -> person.setAge(
                        ((int) (person.getAge() +
                                (person.getAge() *
                                        (10.0 / 100.0))
                        ))))
                .collect(Collectors.toList());
    }

    // Задача 9: Удаление дубликатов имен
    // Удали все повторяющиеся имена из списка.
    public List<Person> deleteDuplicate() {
        List<Person> personList = personRepo.findAll();
        return personList.stream()
                .filter(PredicateByKey.distinctByKey(Person::getName))
                .collect(Collectors.toList());
    }

    // Задача 10: Сводная статистика по возрастам
    // Получи статистику о возрастах: сумма, среднее, минимум, максимум.
    public Map<String, Integer> getStatistic() {
        Map<String, Integer> resultMap = new HashMap<>();
        List<Person> personList = personRepo.findAll();
        int sum = personList
                .stream()
                .mapToInt(Person::getAge)
                .sum();

        OptionalDouble average = personList
                .stream()
                .mapToInt(Person::getAge)
                .average();
        double averageDoubleValue;
        if (average.isPresent()) {
            averageDoubleValue = average.getAsDouble();
        } else {
            averageDoubleValue = average.orElse(0);
        }

        Person maxAge = findMaxAge();

        int min = personList
                .stream()
                .min(Comparator.comparing(Person::getAge))
                .map(Person::getAge)
                .orElseThrow(NoSuchElementException::new);

        resultMap.put("Cumulative", sum);
        resultMap.put("Average", (int) averageDoubleValue);
        resultMap.put("MaxValue", maxAge.getAge());
        resultMap.put("MinValue", min);

        return resultMap;
    }

    public void save(Person person) {
        enrichPerson(person);

        personRepo.save(person);
    }

    private void enrichPerson(Person person) {
        person.setCreatedAt(LocalDateTime.now());
        person.setUpdatedAt(LocalDateTime.now());
        person.setCreatedWho("ADMIN");
    }

    public List<Person> findAll() {
        return personRepo.findAll();
    }

    public Person findById(long id) {
        return personRepo.findById(id)
                .orElseThrow(() -> new PersonNotFoundException(id));
    }

    public Person updatePerson(Long id, Person newPerson) {

        return personRepo.findById(id)
                .map(person -> {
                    person.setName(newPerson.getName());
                    person.setAge(newPerson.getAge());
                    person.setUpdatedAt(LocalDateTime.now());
                    return personRepo.save(person);
                })
                .orElseGet(() -> {
                    enrichPerson(newPerson);
                    return personRepo.save(newPerson);
                });
    }

    public void deleteById(Long id) {
        personRepo.deleteById(id);
    }
}
