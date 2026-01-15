package de.unibayreuth.se.taskboard;

import de.unibayreuth.se.taskboard.business.domain.Task;
import de.unibayreuth.se.taskboard.business.domain.User;
import de.unibayreuth.se.taskboard.business.ports.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Load initial data into the list via the list service from the business layer.
 */
@Component
@RequiredArgsConstructor
@Slf4j
@Profile("dev")
class LoadInitialData implements InitializingBean {
    private final TaskService taskService;
    // TODO: Fix this class after resolving the other TODOs.
    private final de.unibayreuth.se.taskboard.business.ports.UserService userService;

    @Override
    public void afterPropertiesSet() {
        log.info("Deleting existing data");
        userService.clear();
        taskService.clear();
        log.info("Loading initial data");
        List<User> users = TestFixtures.createUsers(userService);
        List<Task> tasks = TestFixtures.createTasks(taskService);
        if (!tasks.isEmpty()) {
            Task task1 = tasks.get(0);
            if (!users.isEmpty()) {
                task1.setAssigneeId(users.get(0).getId());
            }
            taskService.upsert(task1);
            if (tasks.size() > 1) {
                Task task2 = tasks.get(tasks.size() - 1);
                if (users.size() > 1) {
                    task2.setAssigneeId(users.get(users.size() - 1).getId());
                }
                taskService.upsert(task2);
            }
        }
    }
}