package utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import exceptions.WilliamException;
import tasks.Deadline;
import tasks.Event;
import tasks.Task;
import tasks.Todo;

/**
 * The Storage class deals with loading tasks from the file and saving tasks in the file
 */
public class Storage {
    private String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Load tasks from the hard disk
     * 
     * @return arraylist ArrayList of tasks
     * @throws WilliamException If the lists in the txt file is not in the expected format OR if the
     *         pattern is wrong
     */
    public List<Task> loadFromFile() throws FileNotFoundException, WilliamException {

        File f = new File(this.filePath);
        Scanner sc = new Scanner(f);
        List<Task> tasks = new ArrayList<Task>();
        /**
         * Pattern to match lines with five parts separated by pipes ('|')
         * 
         * Each part can contain any characters, with whitespace handling Capture non-whitespace
         * characters in the first two parts and at least one character in the last three parts
         */
        String matchingPattern =
                "^\\s*(\\S+)\\s*\\|\\s*(\\S+)\\s*\\|\\s*(.+?)\\s*\\|\\s*(.+?)\\s*\\|\\s*(.+?)\\s*$";
        Pattern pattern = Pattern.compile(matchingPattern);

        while (sc.hasNext()) {
            Matcher matcher = pattern.matcher(sc.nextLine());
            if (matcher.matches() == false) {
                throw new WilliamException(
                        "The lists is not in the expected format OR The pattern is wrong!");
            }
            String type = matcher.group(1);
            boolean isDone = Integer.parseInt(matcher.group(2)) == 1;
            String name = matcher.group(3);
            String firstPart = matcher.group(4);
            String secondPart = matcher.group(5);

            switch (type) {
                case "T":
                    tasks.add(new Todo(name, isDone));
                    break;
                case "E":
                    tasks.add(new Event(name, DateAndTimeParser.convertStringToDate(firstPart),
                            DateAndTimeParser.convertStringToDate(secondPart), isDone));
                    break;
                case "D":
                    tasks.add(new Deadline(name, DateAndTimeParser.convertStringToDate(firstPart),
                            isDone));
                    break;
            }
        }
        sc.close();
        return tasks;
    }

    /**
     * Save tasks into hard disk
     * 
     * @param arraylist Arraylist of tasks
     * @throws IOException if the arraylist cannot be written to the file
     */
    public void writeToFile(List<Task> tasks) throws IOException {
        FileWriter fw = new FileWriter(this.filePath);
        StringBuilder sb = new StringBuilder();
        int actualSizeComparison = tasks.size() - 1;
        try {
            for (int i = 0; i < tasks.size(); i++) {
                Task task = tasks.get(i);
                String name = task.getName();
                boolean isDoneBFormat = task.getIsDone();
                int isDone = 0;
                if (isDoneBFormat == true) {
                    isDone = 1;
                } else {
                    isDone = 0;
                }
                String[] times = task.getTimes();
                if (i != actualSizeComparison) {
                    sb.append(task.getType()).append(" | ").append(isDone).append(" | ")
                            .append(name).append(" | ").append(times[0]).append(" | ")
                            .append(times[1]).append(System.lineSeparator());
                } else {
                    sb.append(task.getType()).append(" | ").append(isDone).append(" | ")
                            .append(name).append(" | ").append(times[0]).append(" | ")
                            .append(times[1]);
                }
            }
            fw.write(sb.toString());
            fw.close();
        } catch (IOException e) {
            System.out.println("Something went wrong: " + e.getMessage());
        }
    }
}
