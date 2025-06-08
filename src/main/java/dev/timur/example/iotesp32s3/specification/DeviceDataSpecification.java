package dev.timur.example.iotesp32s3.specification;

import dev.timur.example.iotesp32s3.model.DeviceData;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для создания динамических спецификаций запросов к данным IoT устройств.
 * Предоставляет статические методы для построения составных и гибких критериев поиска
 * по временным рядам данных с использованием JPA Criteria API.
 * Поддерживает фильтрацию по временным диапазонам, значениям сенсоров,
 * аномалиям и композитные запросы для аналитики телеметрии.
 */
public class DeviceDataSpecification {

    /**
     * Создает спецификацию для фильтрации данных конкретного устройства.
     * 
     * @param deviceId идентификатор устройства
     * @return спецификация для фильтрации по ID устройства
     */
    public static Specification<DeviceData> hasDeviceId(Long deviceId) {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.equal(root.get("device").get("id"), deviceId);
    }

    /**
     * Создает спецификацию для фильтрации данных после указанной даты.
     * 
     * @param timestamp дата и время, после которых ищутся данные
     * @return спецификация для фильтрации по времени
     */
    public static Specification<DeviceData> timestampAfter(LocalDateTime timestamp) {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.greaterThan(root.get("timestamp"), timestamp);
    }

    /**
     * Создает спецификацию для фильтрации данных до указанной даты.
     * 
     * @param timestamp дата и время, до которых ищутся данные
     * @return спецификация для фильтрации по времени
     */
    public static Specification<DeviceData> timestampBefore(LocalDateTime timestamp) {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.lessThan(root.get("timestamp"), timestamp);
    }

    /**
     * Создает спецификацию для фильтрации данных в указанном временном диапазоне.
     * 
     * @param start начальная дата и время диапазона
     * @param end конечная дата и время диапазона
     * @return спецификация для фильтрации по временному диапазону
     */
    public static Specification<DeviceData> timestampBetween(LocalDateTime start, LocalDateTime end) {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.between(root.get("timestamp"), start, end);
    }

    /**
     * Создает спецификацию для фильтрации данных по диапазону температур.
     * 
     * @param minTemp минимальная температура
     * @param maxTemp максимальная температура
     * @return спецификация для фильтрации по температуре
     */
    public static Specification<DeviceData> temperatureBetween(Float minTemp, Float maxTemp) {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.between(root.get("temperature"), minTemp, maxTemp);
    }

    /**
     * Создает спецификацию для фильтрации данных по диапазону влажности.
     * 
     * @param minHumidity минимальная влажность
     * @param maxHumidity максимальная влажность
     * @return спецификация для фильтрации по влажности
     */
    public static Specification<DeviceData> humidityBetween(Float minHumidity, Float maxHumidity) {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.between(root.get("humidity"), minHumidity, maxHumidity);
    }

    /**
     * Создает спецификацию для поиска данных с температурой выше указанного значения.
     * Полезно для обнаружения перегрева оборудования.
     * 
     * @param temperature пороговое значение температуры
     * @return спецификация для поиска высокой температуры
     */
    public static Specification<DeviceData> temperatureGreaterThan(Float temperature) {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.greaterThan(root.get("temperature"), temperature);
    }

    /**
     * Создает спецификацию для поиска данных с температурой ниже указанного значения.
     * Полезно для обнаружения аномально низких температур.
     * 
     * @param temperature пороговое значение температуры
     * @return спецификация для поиска низкой температуры
     */
    public static Specification<DeviceData> temperatureLessThan(Float temperature) {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.lessThan(root.get("temperature"), temperature);
    }

    /**
     * Создает спецификацию для поиска данных с влажностью выше указанного значения.
     * Полезно для обнаружения высокой влажности.
     * 
     * @param humidity пороговое значение влажности
     * @return спецификация для поиска высокой влажности
     */
    public static Specification<DeviceData> humidityGreaterThan(Float humidity) {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.greaterThan(root.get("humidity"), humidity);
    }

    /**
     * Создает спецификацию для поиска данных с влажностью ниже указанного значения.
     * Полезно для обнаружения низкой влажности.
     * 
     * @param humidity пороговое значение влажности
     * @return спецификация для поиска низкой влажности
     */
    public static Specification<DeviceData> humidityLessThan(Float humidity) {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.lessThan(root.get("humidity"), humidity);
    }

    // RGB фильтры
    
    /**
     * Создает спецификацию для фильтрации данных по диапазону красного цвета RGB.
     * 
     * @param min минимальное значение красного цвета (0-255)
     * @param max максимальное значение красного цвета (0-255)
     * @return спецификация для фильтрации по красному цвету
     */
    public static Specification<DeviceData> redColorBetween(Integer min, Integer max) {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.between(root.get("redColor"), min, max);
    }

    /**
     * Создает спецификацию для фильтрации данных по диапазону зеленого цвета RGB.
     * 
     * @param min минимальное значение зеленого цвета (0-255)
     * @param max максимальное значение зеленого цвета (0-255)
     * @return спецификация для фильтрации по зеленому цвету
     */
    public static Specification<DeviceData> greenColorBetween(Integer min, Integer max) {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.between(root.get("greenColor"), min, max);
    }

    /**
     * Создает спецификацию для фильтрации данных по диапазону синего цвета RGB.
     * 
     * @param min минимальное значение синего цвета (0-255)
     * @param max максимальное значение синего цвета (0-255)
     * @return спецификация для фильтрации по синему цвету
     */
    public static Specification<DeviceData> blueColorBetween(Integer min, Integer max) {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.between(root.get("blueColor"), min, max);
    }

    /**
     * Создает спецификацию для фильтрации данных по диапазону яркости.
     * 
     * @param min минимальное значение яркости (0-255)
     * @param max максимальное значение яркости (0-255)
     * @return спецификация для фильтрации по яркости
     */
    public static Specification<DeviceData> brightnessBetween(Integer min, Integer max) {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.between(root.get("brightness"), min, max);
    }

    // Комплексные условия
    
    /**
     * Создает спецификацию для обнаружения высокой температуры.
     * Алиас для temperatureGreaterThan с более понятным названием.
     * 
     * @param threshold пороговое значение высокой температуры
     * @return спецификация для поиска высокой температуры
     */
    public static Specification<DeviceData> isHighTemperature(Float threshold) {
        return temperatureGreaterThan(threshold);
    }

    /**
     * Создает спецификацию для обнаружения низкой температуры.
     * Алиас для temperatureLessThan с более понятным названием.
     * 
     * @param threshold пороговое значение низкой температуры
     * @return спецификация для поиска низкой температуры
     */
    public static Specification<DeviceData> isLowTemperature(Float threshold) {
        return temperatureLessThan(threshold);
    }

    /**
     * Создает спецификацию для обнаружения высокой влажности.
     * Алиас для humidityGreaterThan с более понятным названием.
     * 
     * @param threshold пороговое значение высокой влажности
     * @return спецификация для поиска высокой влажности
     */
    public static Specification<DeviceData> isHighHumidity(Float threshold) {
        return humidityGreaterThan(threshold);
    }

    /**
     * Создает спецификацию для обнаружения низкой влажности.
     * Алиас для humidityLessThan с более понятным названием.
     * 
     * @param threshold пороговое значение низкой влажности
     * @return спецификация для поиска низкой влажности
     */
    public static Specification<DeviceData> isLowHumidity(Float threshold) {
        return humidityLessThan(threshold);
    }

    /**
     * Создает спецификацию для поиска недавних данных.
     * Алиас для timestampAfter с более понятным названием.
     * 
     * @param since дата, после которой данные считаются недавними
     * @return спецификация для поиска недавних данных
     */
    public static Specification<DeviceData> isRecentData(LocalDateTime since) {
        return timestampAfter(since);
    }

    /**
     * Создает спецификацию для поиска данных с нормальными условиями.
     * Объединяет фильтры по температуре и влажности в допустимых диапазонах.
     * 
     * @param minTemp минимальная нормальная температура
     * @param maxTemp максимальная нормальная температура
     * @param minHumidity минимальная нормальная влажность
     * @param maxHumidity максимальная нормальная влажность
     * @return спецификация для поиска данных с нормальными условиями
     */
    public static Specification<DeviceData> hasNormalConditions(Float minTemp, Float maxTemp, Float minHumidity, Float maxHumidity) {
        return temperatureBetween(minTemp, maxTemp)
                .and(humidityBetween(minHumidity, maxHumidity));
    }

    /**
     * Создает композитную спецификацию для комплексного поиска данных устройств.
     * Объединяет несколько условий поиска с помощью логического И (AND).
     * Автоматически исключает null условия из итогового запроса.
     * 
     * @param deviceId идентификатор устройства (может быть null)
     * @param timestampAfter данные после указанной даты (может быть null)
     * @param timestampBefore данные до указанной даты (может быть null)
     * @param minTemp минимальная температура (может быть null)
     * @param maxTemp максимальная температура (может быть null)
     * @param minHumidity минимальная влажность (может быть null)
     * @param maxHumidity максимальная влажность (может быть null)
     * @return композитная спецификация для комплексного поиска
     */
    public static Specification<DeviceData> buildComplexQuery(
            Long deviceId,
            LocalDateTime timestampAfter,
            LocalDateTime timestampBefore,
            Float minTemp,
            Float maxTemp,
            Float minHumidity,
            Float maxHumidity) {
        
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            // Фильтр по устройству
            if (deviceId != null) {
                predicates.add(criteriaBuilder.equal(root.get("device").get("id"), deviceId));
            }
            
            // Временные фильтры
            if (timestampAfter != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("timestamp"), timestampAfter));
            }
            
            if (timestampBefore != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("timestamp"), timestampBefore));
            }
            
            // Фильтры по температуре
            if (minTemp != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("temperature"), minTemp));
            }
            
            if (maxTemp != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("temperature"), maxTemp));
            }
            
            // Фильтры по влажности
            if (minHumidity != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("humidity"), minHumidity));
            }
            
            if (maxHumidity != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("humidity"), maxHumidity));
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    /**
     * Создает спецификацию для поиска последних данных устройств.
     * Полезно для получения актуального состояния сенсоров.
     * 
     * @param deviceIds список идентификаторов устройств
     * @param since дата, после которой ищутся данные
     * @return спецификация для поиска последних данных
     */
    public static Specification<DeviceData> latestDataForDevices(List<Long> deviceIds, LocalDateTime since) {
        return deviceIds == null || deviceIds.isEmpty() || since == null ? null :
            (root, query, criteriaBuilder) -> {
                return criteriaBuilder.and(
                    root.get("device").get("id").in(deviceIds),
                    criteriaBuilder.greaterThanOrEqualTo(root.get("timestamp"), since)
                );
            };
    }

    // Поиск аномалий
    public static Specification<DeviceData> findAnomalies(
            Float tempThresholdLow, Float tempThresholdHigh,
            Float humidityThresholdLow, Float humidityThresholdHigh) {
        
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.or(
                criteriaBuilder.lessThan(root.get("temperature"), tempThresholdLow),
                criteriaBuilder.greaterThan(root.get("temperature"), tempThresholdHigh),
                criteriaBuilder.lessThan(root.get("humidity"), humidityThresholdLow),
                criteriaBuilder.greaterThan(root.get("humidity"), humidityThresholdHigh)
            );
    }

    // Для аналитики
    public static Specification<DeviceData> forAnalytics(Long deviceId, LocalDateTime since) {
        return hasDeviceId(deviceId).and(timestampAfter(since));
    }
} 