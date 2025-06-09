package dev.timur.example.iotesp32s3.exception;

/**
 * Исключение для случаев когда ресурс не найден
 */
public class ResourceNotFoundException extends RuntimeException {
    
    /**
     * Конструктор с сообщением
     * @param message сообщение об ошибке
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
    /**
     * Конструктор с сообщением и причиной
     * @param message сообщение об ошибке
     * @param cause причина исключения
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
} 