package storage;

import models.Model;
import models.Monster;

import java.util.ArrayList;
import java.util.List;

public class RandomStorage implements StorageService {

    private static final int MCL = 8; // # 1/8th notes in melody chromosome
    private static final int NUM_MONSTERS = 4;
    private static final int REST = Integer.MIN_VALUE;
    private static int[] cPitches = {REST, REST, 60, 62, 64, 65, 67, 69, 71, 72, 74, 76};

    public List<Monster> loadMonsters(){
        List<Monster> monsters = new ArrayList<>();
        
        for (int j = 0; j < NUM_MONSTERS; j++) {
        
            Model.Note[] notes1 = new Model.Note[MCL];
            Model.Note[] notes2 = new Model.Note[MCL];
            
            for (int i = 0; i < MCL; i++) {
                notes1[i] = new Model.Note(cPitches[(int)(Math.random() * cPitches.length)], 8);
                notes2[i] = new Model.Note(cPitches[(int)(Math.random() * cPitches.length)], 8);
            }
            
            Monster monster1 = new Monster(notes1, notes2); //fixme make less direct
            
            monsters.add(monster1);
        }
        
        return monsters;
        
    }
}