package com.desafio.todo.taskmanager.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.desafio.todo.taskmanager.model.Priority;
import com.desafio.todo.taskmanager.model.Task;
import com.desafio.todo.taskmanager.model.TaskStatus;

public interface TaskRepository extends MongoRepository<Task, String> {

    // Find by Status
    List<Task> findByStatus(TaskStatus status);

    // Find by priority
    List<Task> findByPriority(Priority priority);

    // Find by DueDate
    List<Task> findByDueDateBefore(LocalDateTime date);

    List<Task> findByDueDateAfter(LocalDateTime date);

    List<Task> findByDueDateBetween(LocalDateTime date);

    // Find by State and Priority
    List<Task> findByStatusAndPriority(TaskStatus status, Priority priority);

    // Find overDue Task
    @Query("{'dueDate': {$lt: ?0} , 'status': {$ne: 'COMPLETADA'}}")
    List<Task> findOverdueTasks(LocalDateTime date);

    // Find by Title
    @Query("{'title' : {$regex: ?0, $options: 'i'}}")
    List<Task> findByTitleContainingIgnoreCase(String title);

    // Count by Status
    long countByStatus(TaskStatus status);

    // Sort by createdAt Desc
    List<Task> findAllByOrderByCreatedAtDesc();

    // Sort by dueDate Asc
    List<Task> findAllByOrderByDueDateAsc();

    @Query("{'status': {$in: ?0}, 'priority': {$in: ?1}}")
    List<Task> findByStatusInAndPriorityIn(List<TaskStatus> statuses, List<Priority> priorities);

}
