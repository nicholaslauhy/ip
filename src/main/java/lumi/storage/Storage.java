package lumi.storage;

import lumi.exception.LumiException;
import lumi.task.*;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private final Path filePath;

    // store in relative path
    public Storage(String relativePath){
        this.filePath = Paths.get(relativePath);
    }

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

    public void save(TaskList taskList) throws LumiException {
        try {
            // ensure parent folder exists
            Path parentFolder = filePath.getParent();
            if (parentFolder != null){
                Files.createDirectories(parentFolder);
            }
            ArrayList<String> lines = new ArrayList<>();
            for (Task t : taskList.all()){
                lines.add(encode(t));
            }
            Files.write(filePath, lines);
        } catch (IOException e){
            throw new LumiException("AHHH I CANNOT SAVE THIS!!" + e.getMessage());
        }
    }

    private String encode(Task t){
        String done = t.isDone() ? "1" : "0";

        if (t instanceof Todo){
            return "T | " + done + " | " + t.getDescription();
        }
        if (t instanceof Deadline d){
            return "D | " + done + " | " + d.getDescription() + " | " + d.getBy();
        }
        if (t instanceof Event e){
            return "E | " + done + " | " + e.getDescription() + " | " + e.getFrom() + " | " + e.getTo();
        }
        // fallback response
        return "T | " + done + " | " + t.getDescription();
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
            t = new Deadline(desc, parts[3].trim());
            break;
        case "E":
            if (parts.length < 5) {
                throw new LumiException("This event line is corrupted!! TRY AGAIN!! " + line);
            }
            t = new Event(desc, parts[3].trim(), parts[4].trim());
            break;
        default:
            throw new LumiException("This task type is unknown!! FIX THIS!! " + type);
        }
        t.setDone(isDone);
        return t;
    }
}
