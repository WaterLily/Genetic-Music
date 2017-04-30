package storage;

import jm.constants.Pitches;
import models.Model;
import models.Monster;

import java.util.ArrayList;
import java.util.List;

public class RandomStorage implements StorageService {

  private static final int MCL = 4; //
  private static final int LENGTH = 4; //
  private static final int NUM_MONSTERS = 4;
  private static final int REST = Pitches.REST;
  private static int[] cPitches = {REST, REST, 60, 62, 64, 65, 67, 69, 71, 72, 74, 76};

  public List<Monster> loadMonsters() {
    List<Monster> monsters = new ArrayList<>();

    for (int j = 0; j < NUM_MONSTERS; j++) {

      Model.SimpleNote[] notes1 = new Model.SimpleNote[MCL];
      Model.SimpleNote[] notes2 = new Model.SimpleNote[MCL];

      for (int i = 0; i < MCL; i++) {
        notes1[i] = new Model.SimpleNote(cPitches[(int) (Math.random() * cPitches.length)], LENGTH);
        notes2[i] = new Model.SimpleNote(cPitches[(int) (Math.random() * cPitches.length)], LENGTH);
      }

      Monster monster1 = new Monster(notes1, notes2);

      monsters.add(monster1);
    }

    return monsters;

  }
}