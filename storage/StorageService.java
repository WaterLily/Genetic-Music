package storage;

import models.Monster;

import java.util.List;


public interface StorageService {
    
    public List<Monster> loadMonsters();
}