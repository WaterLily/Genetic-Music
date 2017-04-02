package models;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/** Unit tests for {@link Monster}. */
@RunWith(JUnit4.class)
public class MonsterTest {

  @Test
  public void testMakeGamete() {
    Model.Note[] notes1 = {new Model.Note(60, 8), new Model.Note(62, 8), new Model.Note(64, 8)};
    Model.Note[] notes2 = {new Model.Note(65, 8), new Model.Note(67, 8), new Model.Note(69, 8)};
    Monster monster = new Monster(notes1, notes2);

    testGameteConstruction(monster, constructRandom(1.0), notes2);

    Model.Note[] expected = {new Model.Note(60, 8), new Model.Note(67, 8), new Model.Note(64, 8)};
    testGameteConstruction(monster, constructRandom(0.0), expected);

    Model.Note[] expected2 = {new Model.Note(60, 8), new Model.Note(67, 8), new Model.Note(69, 8)};
    testGameteConstruction(monster, constructRandom(0.0, 0.0, 1.0), expected2);
  }

  @Test
  public void testGetMelody() {
    Model.Note[] notes1 = {new Model.Note(60, 8), new Model.Note(62, 8), new Model.Note(64, 8)};
    Model.Note[] notes2 = {new Model.Note(65, 8), new Model.Note(67, 8), new Model.Note(69, 8)};
    Monster monster = new Monster(notes1, notes2);
    List<Model.Note> expected = new ArrayList<>();
    expected.addAll(Arrays.asList(notes1));
    expected.addAll(Arrays.asList(notes2));

    assertEquals(monster.getMelody().toString(), monster.getMelody(), expected);
  }

  private void testGameteConstruction(Monster monster, Random random, Model.Note[] expected) {
    monster.setRandom(random);
    Model.Gamete gamete = monster.makeGamete();
    assertTrue(gamete.toString(), containsExactlyNotes(gamete, expected));
  }

  private boolean containsExactlyNotes(Model.Gamete gamete, Model.Note[] notes) {
    Model.MelodyBase base = gamete.melodyChromosome;
    for (Model.Note note : notes) {
      if (base == null || !base.note.equals(note)) {
        return false;
      }
      base = base.next();
    }
    return true;
  }

  private Random constructRandom(final double... values) {
    return new Random() {
      private int count = 0;

      public double nextDouble() {
        double toreturn = values[count % values.length];
        count++;
        return toreturn;
      }
    };
  }
}