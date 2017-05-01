package storage;

import jm.constants.Pitches;
import models.Gamete;
import models.Model;
import models.Monster;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomStorage implements StorageService {

  private static final int MCL = 4; //
  private static final int LENGTH = 4; //
  private static final int NUM_MONSTERS = 4;
  private static final int REST = Pitches.REST;
  private static int[] cPitches = {REST, REST, 60, 62, 64, 65, 67, 69, 71, 72, 74, 76};

  public List<Monster> loadMonsters() {
    List<Monster> monsters = new ArrayList<>();
    Random random = new Random();
    for (int j = 0; j < NUM_MONSTERS; j++) {
      monsters.add(new Monster(Gamete.generate(random), Gamete.generate(random)));
    }
    return monsters;
  }

  @Override
  public void save(List<Monster> monsters) {
    // Not applicable
  }
}