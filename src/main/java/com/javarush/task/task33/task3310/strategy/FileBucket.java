package com.javarush.task.task33.task3310.strategy;

import com.javarush.task.task33.task3310.ExceptionHandler;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileBucket {
    private Path path;

    public FileBucket() {
        try {
            this.path = Files.createTempFile(null, null);
            Files.deleteIfExists(path);
            Files.createFile(path);
        } catch (IOException e) {
            ExceptionHandler.log(e);
        } finally {
            if (path != null)
                path.toFile().deleteOnExit();
        }
    }

    public long getFileSize() {
        long size = 0L;
        try {
            size = Files.size(path);
        } catch (IOException e) {
            ExceptionHandler.log(e);
            return size;
        }
        return size;
    }

    public void putEntry(Entry entry) {
        try (OutputStream fos = Files.newOutputStream(path);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(entry);
        } catch (IOException e) {
            ExceptionHandler.log(e);
        }
    }

    public Entry getEntry() {
        if (getFileSize() <= 0)
            return null;
        Entry entry = null;
        try (InputStream fis = Files.newInputStream(path);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            entry = (Entry) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            ExceptionHandler.log(e);
        }
        return entry;
    }

    public void remove() {
        try {
            Files.delete(path);
        } catch (IOException e) {
            ExceptionHandler.log(e);
        }
    }
}
