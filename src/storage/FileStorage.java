package storage;

import static utils.Utils.checkNotNull;

import models.Monster;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements {@link StorageService} using serialization.
 */
public class FileStorage implements StorageService {

  private final File file;

  public FileStorage(File file) {
    this.file = checkNotNull(file);
  }

  @Override
  public List<Monster> loadMonsters() {
    List<Monster> creatures = new ArrayList<>();
    try (FileInputStream fis = new FileInputStream(file);
         ObjectInputStream ois = new ObjectInputStream(fis)) {
      creatures = (List<Monster>) ois.readObject();
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }
    return creatures.isEmpty() ? new RandomStorage().loadMonsters() : creatures;
  }

  @Override
  public void save(List<Monster> monsters) {
    try (FileOutputStream fout = new FileOutputStream(file);
         ObjectOutputStream oos = new ObjectOutputStream(fout)) {
      oos.writeObject(monsters);
      fout.close();
      oos.close();
    } catch (IOException e) {
      e.printStackTrace();
      // TODO use a backup save file
    }
  }
}
