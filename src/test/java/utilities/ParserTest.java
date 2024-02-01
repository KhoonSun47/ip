package utilities;

import org.junit.jupiter.api.Test;

import commands.Commands;
import utilities.Parser;
import utilities.StorageStub;
import utilities.TaskListStub;
import tasks.Todo;
import tasks.Deadline;
import tasks.Event;

import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Test class for the Parser in the utilities packages
 */
public class ParserTest {
    private TaskListStub taskListStub;
    private StorageStub storageStub;
    private Parser parser;

    @BeforeEach
    public void setUp() {
        taskListStub = new TaskListStub();
        storageStub = new StorageStub("noStringPath");
        parser = new Parser(taskListStub, storageStub);
    }

    /**
     * Test the parseCommands method for toDo switch case for the correct input
     * (assertTrue if it is added successfully, assertFalse if its not added
     * successfully)
     */
    @Test
    public void parseCommands_toDoCommand_addsToDo() {
        String additionalInfo = "Read Book";
        parser.parseCommands(Commands.todo, additionalInfo);
        assertFalse(taskListStub.getTasks().isEmpty());
        assertTrue(taskListStub.getTasks().get(0) instanceof Todo);
    }

    /**
     * Test the parseCommands method for toDo switch case for the incorrect input
     * (assertTrue since it should not be added, and list should be empty)
     */
    @Test
    public void parseCommands_incorrectToDoCommand_doesNotAddTask() {
        parser.parseCommands(Commands.todo, "");
        assertTrue(taskListStub.getTasks().isEmpty());
    }

    /**
     * Test the parseCommands method for deadline switch case for the correct input
     * (assertTrue if it is added successfully, assertFalse if its not added
     * successfully)
     */
    @Test
    public void parseCommands_deadlineCommand_addsDeadline() {
        String additionalInfo = "CS2103 Assignment 1 /by 12/12/2023 1800";
        parser.parseCommands(Commands.deadline, additionalInfo);
        assertFalse(taskListStub.getTasks().isEmpty());
        assertTrue(taskListStub.getTasks().get(0) instanceof Deadline);
    }

    /**
     * Test the parseCommands method for deadline switch case for the incorrect
     * input (assertTrue since it should not be added, and list should be empty)
     */
    @Test
    public void parseCommands_incorrectDeadlineCommand_doesNotAddTask() {
        parser.parseCommands(Commands.deadline, "CS2103 Assignment 1");
        parser.parseCommands(Commands.deadline, "CS2103 Assignment 1 /by");
        parser.parseCommands(Commands.deadline, "CS2103 Assignment 1 /by 12/12/2023 6pm");
        assertTrue(taskListStub.getTasks().isEmpty());
    }

    /**
     * Test the parseCommands method for event switch case for the correct input
     * (assertTrue if it is added successfully, assertFalse if its not added
     * successfully)
     */
    @Test
    public void parseCommands_eventCommand_addsDeadline() {
        String additionalInfo = "CS2103 Assignment 2 /from 12/12/2023 1800 /to 13/12/2023 1700";
        parser.parseCommands(Commands.event, additionalInfo);
        assertFalse(taskListStub.getTasks().isEmpty());
        assertTrue(taskListStub.getTasks().get(0) instanceof Event);
    }

    /**
     * Test the parseCommands method for event switch case for the incorrect
     * input (assertTrue since it should not be added, and list should be empty)
     */
    @Test
    public void parseCommands_incorrectEventCommand_doesNotAddTask() {
        parser.parseCommands(Commands.event, "CS2103 Assignment 2");
        parser.parseCommands(Commands.event, "CS2103 Assignment 2 /from 12/12/2023 1800 ");
        parser.parseCommands(Commands.event, "CS2103 Assignment 2 /from 12/12/2023 1800 /to ");
        parser.parseCommands(Commands.event, "CS2103 Assignment 2 /from 12/12/2023 1800 /to 11/12/2023 1900");
        parser.parseCommands(Commands.event, "CS2103 Assignment 2 /from 12/12/2023 6pm /to 12/12/2023 7pm");
        assertTrue(taskListStub.getTasks().isEmpty());
    }

    /**
     * Test the parseCommands method for bye switch case, check whether a task been
     * added successfully to a file (since file should not be used for isolation, it
     * is temporaily stored in an arraylist instead)
     */
    @Test
    public void parseCommands_byeCommand_addTasksIntoFile() {
        Todo todo = new Todo("Test Task Added To File");
        taskListStub.addTask(todo);

        parser.parseCommands(Commands.bye, "");

        assertFalse(storageStub.loadFromFile().isEmpty());
        assertEquals(todo, storageStub.loadFromFile().get(0));
        assertFalse(parser.isExit());
    }

    /**
     * Test the parseCommands method for delete switch case, check whether a task
     * has been deleted successfully
     */
    @Test
    public void parseCommands_deleteCommand_deleteTaskFromList() {
        Todo todo = new Todo("Test Task Added To File");
        taskListStub.addTask(todo);

        // Since there is only one task, the ID of the task is 1
        parser.parseCommands(Commands.delete, "1");
        assertTrue(taskListStub.getTasks().isEmpty());
    }

    /**
     * Test the parseCommands method for mark switch case, check whether a task has
     * been marked successfully
     */
    @Test
    public void parseCommands_markCommand_markTaskAsDone() {
        Todo todo = new Todo("Test Task Added To File", false);
        taskListStub.addTask(todo);

        // Since there is only one task, the ID of the task is 1
        parser.parseCommands(Commands.mark, "1");
        assertEquals("X", taskListStub.getTasks().get(0).getStatusIcon());
    }

    /**
     * Test the parseCommands method for mark switch case, check whether a task has
     * been marked successfully
     */
    @Test
    public void parseCommands_markCommand_markTaskAsNotDone() {
        Todo todo = new Todo("Test Task Added To File", true);
        taskListStub.addTask(todo);

        // Since there is only one task, the ID of the task is 1
        parser.parseCommands(Commands.mark, "1");
        assertEquals(" ", taskListStub.getTasks().get(0).getStatusIcon());
    }
}
