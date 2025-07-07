package com.desafio.todo.taskmanager.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.desafio.todo.taskmanager.dto.CreateTaskRequest;
import com.desafio.todo.taskmanager.dto.TaskResponse;
import com.desafio.todo.taskmanager.dto.TaskStatistics;
import com.desafio.todo.taskmanager.dto.UpdateTaskRequest;
import com.desafio.todo.taskmanager.model.Priority;
import com.desafio.todo.taskmanager.model.Task;
import com.desafio.todo.taskmanager.model.TaskStatus;
import com.desafio.todo.taskmanager.repository.TaskRepository;

public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // Create a new Task
    public TaskResponse createTask(CreateTaskRequest request) {
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setDueDate(request.getDueDate());
        task.setPriority(request.getPriority());
        task.setStatus(TaskStatus.PENDIENTE);
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        Task savedTask = taskRepository.save(task);

        return TaskResponse.fromTask(savedTask);

    }

    // Get all Tasks with optional filters
    public List<TaskResponse> getAllTasks(TaskStatus status, Priority priority, LocalDateTime dueDateFrom,
            LocalDateTime dueDateTo) {
        List<Task> tasks;

        if (status != null && priority != null) {
            tasks = taskRepository.findByStatusAndPriority(status, priority);
        } else if (status != null) {
            tasks = taskRepository.findByStatus(status);

        } else if (priority != null) {
            tasks = taskRepository.findByPriority(priority);

        } else {
            tasks = taskRepository.findAll();
        }

        // filter by due date if is provided

        if (dueDateFrom != null && dueDateTo != null) {
            tasks = tasks.stream()
                    .filter(task -> task.getDueDate() != null &&
                            task.getDueDate().isAfter(dueDateFrom.minusSeconds(1)) &&
                            task.getDueDate().isBefore(dueDateTo.plusSeconds(1)))
                    .toList();
        } else if (dueDateFrom != null) {
            tasks = tasks.stream()
                    .filter(task -> task.getDueDate() != null &&
                            task.getDueDate().isAfter(dueDateFrom.minusSeconds(1)))
                    .toList();
        } else if (dueDateTo != null) {
            tasks = tasks.stream()
                    .filter(task -> task.getDueDate() != null &&
                            task.getDueDate().isBefore(dueDateTo.plusSeconds(1)))
                    .toList();
        }

        return tasks.stream().map(TaskResponse::fromTask).toList();

    }

    public Optional<TaskResponse> getTaskById(String id) {
        return taskRepository.findById(id).map(TaskResponse::fromTask);
    }

    public Optional<TaskResponse> updateTask(String id, UpdateTaskRequest request) {
        Optional<Task> optionalTask = taskRepository.findById(id);

        if (optionalTask.isEmpty()) {
            return Optional.empty();
        }

        Task task = optionalTask.get();

        // update only not null fields

        if (request.getTitle() != null) {
            task.setTitle(request.getTitle());
        }

        if (request.getDescription() != null) {
            task.setDescription(request.getDescription());
        }
        if (request.getDueDate() != null) {
            task.setDueDate(request.getDueDate());
        }
        if (request.getPriority() != null) {
            task.setPriority(request.getPriority());
        }
        if (request.getStatus() != null) {
            task.setStatus(request.getStatus());
        }

        Task updatedTask = taskRepository.save(task);
        return Optional.of(TaskResponse.fromTask(updatedTask));

    }

    // Complete a task - change status to COMPLETADA
    public Optional<TaskResponse> completeTask(String id) {

        Optional<Task> optionalTask = taskRepository.findById(id);

        if (optionalTask.isEmpty()) {
            return Optional.empty();
        }

        Task task = optionalTask.get();
        task.setStatus(TaskStatus.COMPLETADA);
        task.setUpdatedAt(LocalDateTime.now());

        Task updatedTask = taskRepository.save(task);

        return Optional.of(TaskResponse.fromTask(updatedTask));

    }

    // Delete a task
    public boolean deleteTask(String id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Search by title
    public List<TaskResponse> searchByTitle(String title) {
        List<Task> tasks = taskRepository.findByTitleContainingIgnoreCase(title);
        return tasks.stream().map(TaskResponse::fromTask).toList();
    }

    // Get Overdue Tasks
    public List<TaskResponse> getOverdueTasks() {

        List<Task> overdueTasks = taskRepository.findOverdueTasks(LocalDateTime.now());
        return overdueTasks.stream().map(TaskResponse::fromTask).toList();

    }

    // Get general Taks Statistics

    public TaskStatistics getTaskStatistics() {
        long totalTasks = taskRepository.count();
        long pendingTasks = taskRepository.countByStatus(TaskStatus.PENDIENTE);
        long inProgressTasks = taskRepository.countByStatus(TaskStatus.EN_CURSO);
        long completedTasks = taskRepository.countByStatus(TaskStatus.COMPLETADA);
        List<Task> overDueTasks = taskRepository.findOverdueTasks(LocalDateTime.now());
        long overdueCount = overDueTasks.size();

        return new TaskStatistics(totalTasks, pendingTasks, inProgressTasks, completedTasks, overdueCount);

    }

}
