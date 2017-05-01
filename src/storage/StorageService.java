package storage;

import models.Monster;

import java.util.List;

public interface StorageService {

  List<Monster> loadMonsters();

  void save(List<Monster> monsters);
}