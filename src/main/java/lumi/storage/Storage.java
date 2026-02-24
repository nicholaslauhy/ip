package lumi.storage;

import lumi.exception.LumiException;
import lumi.task.*;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

/**
 * Handles persistence of Lumi tasks to and from a plain text file.
 * <p>
 * The storage file uses one task per line, with fields separated by {@code " | "}.
 * The general format is:
 * <pre>
 * TYPE | IS_DONE | DESCRIPTION [| EXTRA_FIELDS...]
 * </pre>
 * where:
 * <ul>
 *   <li>{@code TYPE} is {@code T} (Todo), {@code D} (Deadline), or {@code E} (Event)</li>
 *   <li>{@code IS_DONE} is {@code 1} for done, {@code 0} for not done</li>
 *   <li>{@code DESCRIPTION} is the task description text</li>
 * </ul>
 * <p>
 * Extra fields depend on the task type:
 * <ul>
 *   <li>Todo: {@code T | done | desc}</li>
 *   <li>Deadline: {@code D | done | desc | byDateTime}</li>
 *   <li>Event: {@code E | done | desc | fromDateTime | toDateTime}</li>
 * </ul>
 * <p>
 * Date/time fields are stored using {@link LocalDateTime#parse(CharSequence)}
 * (ie {@code 2019-10-15T18:00}).
 */
public class Storage {
    /**
     * Path to the text file used for task persistence
     */
    private final Path filePath;

    /**
     * Creates storage handler that reads/writes tasks from a relative file path
     * @param relativePath Relative path to the storage file (eg {@code "./data/lumi.txt"})
     */
    public Storage(String relativePath){
        this.filePath = Paths.get(relativePath);
    }

    /**
     * Loads tasks from disk into a new {@link TaskList}
     * <p>
     * If a storage file does not exist, an empty {@link TaskList} is returned
     * Each non-empty line must match the expected storage format;
     * otherwise a {@link LumiException} is thrown to indicate invalid format
     * @return A task list populated with tasks loaded from disk
     * @throws LumiException If the file cannot be read or contains invalid task
     */
    public TaskList load() throws LumiException {
        try{
            if (!Files.exists(filePath)){
                return new TaskList();
            }

            List<String> lines = Files.readAllLines(filePath);
            TaskList list = new TaskList();

            for (String line : lines){
                if (line.trim().isEmpty()){
                    continue;
                }
                Task task = decode(line);
                list.add(task);
            }
            return list;
        } catch (IOException e){
            throw new LumiException("I CANNOT LOAD YOUR TASKS!! AHHHH " + e.getMessage());
        }
    }

    /**
     * Saves the given task list to disk, overwriting any exisiting file content
     * <p>
     * The parent directory is created automatically if it does not exist
     * @param taskList Task list to persist
     * @throws LumiException If the file cannot be written
     */
    public void save(TaskList taskList) throws LumiException {
        try {
            // ensure parent folder exists
            Path parentFolder = filePath.getParent();
            if (parentFolder != null){
                Files.createDirectories(parentFolder);
            }
            ArrayList<String> lines = new ArrayList<>();
            for (Task t : taskList.all()){
                lines.add(t.toStorageString());
            }
            Files.write(filePath, lines);
        } catch (IOException e){
            throw new LumiException("AHHH I CANNOT SAVE THIS!!" + e.getMessage());
        }
    }

    private Task decode(String line) throws LumiException {
        // split by " | " safely
        String[] parts = line.split("\\s\\|\\s");
        if (parts.length < 3) {
            throw new LumiException("This file format is corrupt!! TRY AGAIN!!" + line);
        }

        String type = parts[0].trim();
        boolean isDone = parts[1].trim().equals("1");
        String desc = parts[2].trim();

        Task t;
        switch (type) {
        case "T":
            t = new Todo(desc);
            break;
        case "D":
            if (parts.length < 4) {
                throw new LumiException("This deadline line is corrupted!! TRY AGAIN!!" + line);
            }

            try {
                LocalDateTime byDate = LocalDateTime.parse(parts[3].trim());
                t = new Deadline(desc, byDate);
            } catch (DateTimeParseException e) {
                throw new LumiException("Stored deadline date is invalid!! FIX YOUR FILE!! " + parts[3]);
            }
            break;
        case "E":
            if (parts.length < 5) {
                throw new LumiException("This event line is corrupted!! TRY AGAIN!! " + line);
            }

            try {
                LocalDateTime fromDate = LocalDateTime.parse(parts[3].trim());
                LocalDateTime toDate = LocalDateTime.parse(parts[4].trim());
                t = new Event(desc, fromDate, toDate);
            } catch (DateTimeParseException e) {
                throw new LumiException("Stored event date is invalid!! FIX YOUR FILE!!");
            }
            break;
        default:
            throw new LumiException("This task type is unknown!! FIX THIS!! " + type);
        }
        t.setDone(isDone);
        return t;
    }
}
