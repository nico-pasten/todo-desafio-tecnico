package com.desafio.todo.taskmanager.model;

public enum Priority {

    BAJA("baja"),
    MEDIA("media"),
    ALTA("alta");

    private final String value;

    Priority(String value) {
        this.value = value;

    }

    public String getValue() {
        return value;
    }

    public static Priority fromValue(String value) {
        for (Priority priority : Priority.values()) {
            if (priority.value.equalsIgnoreCase(value)) {

                return priority;
            }
        }
        throw new IllegalArgumentException("Prioridad no valida: " + value);

    }

}
