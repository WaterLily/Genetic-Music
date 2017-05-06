package models;

import jm.music.data.Note;
import models.Model.SimpleNote;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static jm.constants.Durations.SIXTEENTH_NOTE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/** Unit tests for {@link Monster}. */
@RunWith(JUnit4.class)
public class MonsterTest {

  @Test
  public void testMakeGamete_splicesMelodyChromosomes() {
    SimpleNote[] notes1 = {new SimpleNote(60, 8), new SimpleNote(62, 8), new SimpleNote(64, 8)};
    SimpleNote[] notes2 = {new SimpleNote(65, 8), new SimpleNote(67, 8), new SimpleNote(69, 8)};
    Monster monster = new Monster(
        Gamete.builder().setMelody(Gamete.makeMelodyBases(notes1)).build(),
        Gamete.builder().setMelody(Gamete.makeMelodyBases(notes2)).build());

    testMelodyGameteConstruction(monster, constructRandom(1.0), notes2);

    SimpleNote[] expected = {new SimpleNote(60, 8), new SimpleNote(67, 8), new SimpleNote(64, 8)};
    testMelodyGameteConstruction(monster, constructRandom(0.0), expected);

    SimpleNote[] expected2 = {new SimpleNote(60, 8), new SimpleNote(67, 8), new SimpleNote(69, 8)};
    testMelodyGameteConstruction(monster, constructRandom(0.0, 0.0, 1.0), expected2);
  }

  @Test
  public void testMakeGamete_includesTransformChromosomes() {
    SimpleNote[] notes = {new SimpleNote(60, 8)};
    Allele allele = new Allele.DoubleAllele(1);
    Monster monster = new Monster(
        Gamete.builder()
            .setMelody(Gamete.makeMelodyBases(notes))
            .addAllele(Locus.TIME_OFFSET, new Allele.DoubleAllele(0))
            .build(),
        Gamete.builder()
            .setMelody(Gamete.makeMelodyBases(notes))
            .addAllele(Locus.TIME_OFFSET, allele)
            .build());

    assertEquals(
        allele,
        monster
            .setRandom(constructRandom(1.0))
            .makeGamete()
            .transformAleles
            .get(Locus.TIME_OFFSET));
  }

  @Test
  public void testGetMelody() {
    SimpleNote[] notes1 = {new SimpleNote(60, 8), new SimpleNote(61, 8)};
    SimpleNote[] notes2 = {new SimpleNote(62, 8), new SimpleNote(63, 8)};
    SimpleNote[] expected = {
        new SimpleNote(63, 8), new SimpleNote(60, 8),
        new SimpleNote(61, 8), new SimpleNote(62, 8)};
    Monster monster = new Monster(
        Gamete.builder()
            .setMelody(Gamete.makeMelodyBases(notes1))
            .addAllele(Locus.TIME_OFFSET, new Allele.DoubleAllele(2 * SIXTEENTH_NOTE))
            .build(),
        Gamete.builder()
            .setMelody(Gamete.makeMelodyBases(notes2))
            .addAllele(Locus.TIME_OFFSET, new Allele.DoubleAllele(2 * SIXTEENTH_NOTE))
            .build());

    List<Note> melody = monster.getMelody().notes;

    List<SimpleNote> simpleMelody = new ArrayList<>(melody.size());
    for (Note note : melody) {
      simpleMelody.add(new SimpleNote(note.getPitch(), ((int)(4.1/note.getRhythmValue()))));
    }
    assertEquals(monster.getMelody().toString(), Arrays.asList(expected), simpleMelody);
  }

  private void testMelodyGameteConstruction(Monster monster, Random random, SimpleNote[] expected) {
    monster.setRandom(random);
    Gamete gamete = monster.makeGamete();
    assertTrue(gamete.toString(), containsExactlyNotes(gamete, expected));
  }

  private boolean containsExactlyNotes(Gamete gamete, SimpleNote[] notes) {
    Genome.MelodyBase base = gamete.melodyAlleles;
    for (SimpleNote note : notes) {
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