package main;

import storage.BasisStorage;
import storage.HeytingAlgebraStorage;
import storage.SetObjectStorage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static classutils.LoadPaths.*;

public class Project {
    private static Project PROJECT = null;

    private String name;

    BasisStorage basisStorage = BasisStorage.getInstance();
    HeytingAlgebraStorage heytingStorage = HeytingAlgebraStorage.getInstance();
    SetObjectStorage setObjectStorage = SetObjectStorage.getInstance();

    public static Project getInstance() {
        if (PROJECT == null) PROJECT = new Project();
        return PROJECT;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


   public void setProject(){
        basisStorage.empty();
        heytingStorage.empty();
        setObjectStorage.empty();

        loadToStorage(BASISPATH);
        loadToStorage(HEYTINGALGEBRAPATH);
        loadToStorage(SETOBJECTPATH);

    }

    private String getStoragePath(String storagePath){
        return DATAPATH + name + storagePath;
    }

    private void loadToStorage(String storagePath){
        try (Stream<Path> directories = Files.walk(Paths.get(getStoragePath(storagePath)))) {
            directories
                    .filter(Files::isRegularFile)
                    .forEach(file->{
                        String filename = file.getFileName().toString().split("\\.")[0];
                        switch (storagePath){
                           case "/Basis/" -> basisStorage.load(filename);
                           case "/HeytingAlgebras/" -> heytingStorage.load(filename);
                            default -> setObjectStorage.load(filename);
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
