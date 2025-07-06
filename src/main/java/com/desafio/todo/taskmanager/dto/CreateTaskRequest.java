package com.desafio.todo.taskmanager.dto;

import java.time.LocalDateTime;

import com.desafio.todo.taskmanager.model.Priority;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateTaskRequest {

    @NotBlank(message = "El titulo es obligatorio")
    @Size(min = 1, max = 200, message = "El titulo debe tener entre 1 y 200 caracteres")
    private String title;

    @Size()
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dueDate;

    @NotNull(message = "La prioridad es obligatoria")
    private Priority priority;

}
