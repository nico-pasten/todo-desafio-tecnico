package com.desafio.todo.taskmanager.model;

public enum TaskStatus {

    PENDIENTE("pendiente"),
    EN_CURSO("en_curso"),
    COMPLETADA("completada");

    private final String value;

    TaskStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static TaskStatus fromString(String value) {
        for (TaskStatus status : TaskStatus.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Estado no valido: " + value);
    }

}
