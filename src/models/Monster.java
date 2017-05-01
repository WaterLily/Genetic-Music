package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static jm.constants.Durations.WHOLE_NOTE;
import static utils.Utils.checkNotNull;

import jm.music.data.Note;
import models.Genome.MelodyBase;
import models.Model.Melody;
import models.Model.SimpleNote;

/** Models a singing creature. */
public class Monster implements Serializable {

  private final Genome genes;
  private Melody song;
  private Random random;

  public Monster(Gamete one, Gamete two) {
    this(new Genome(one, two));
  }

  public Monster(Genome genes) {
    this.genes = checkNotNull(genes);
    this.random = new Random();
    constructSong();
  }

  //TODO remove after real storage is built
  public Monster(Model.SimpleNote[] notes1, Model.SimpleNote[] notes2) {
    this(Gamete.builder().setMelody(makeMelodyBases(notes1)).build(),
        Gamete.builder().setMelody(makeMelodyBases(notes2)).build());
  }

  // Visible for testing
  void setRandom(Random random) {
    this.random = random;
  }

  public Melody getMelody() {
    return song;
  }

  private void constructSong() {
    List<Note> notes = new ArrayList<>();
    for (SimpleNote note : genes.getMelody()) {
      notes.add(new Note(note.pitch, WHOLE_NOTE / (double) note.fraction));
    }
    for (Transforms.Transform transform : genes.getTransforms()) {
      transform.transform(notes);
    }

    if (Integer.parseInt(getName()) % 2 == 0) {
      System.out.println("transforming " + getName());
      notes = new Transforms.DiatonicTranspose(0, 5).transform(notes);
      notes = new Transforms.Shift(true).transform(notes);
    }

    song = new Melody(getName(), notes);
  }
  public String getName() {
    return hashCode() + "";
  }

  @Override
  public String toString() {
    return genes.getMelody().toString();
  }

  public Gamete makeGamete() {
    return genes.getGamete(random);
  }

  public static Monster breed(Monster parent1, Monster parent2) {
    Gamete one = parent1.makeGamete();
    Gamete two = parent2.makeGamete();
    return new Monster(one, two);
  }

  private static MelodyBase makeMelodyBases(Model.SimpleNote[] notes) {
    MelodyBase last = null;
    for (int i = notes.length - 1; i >= 0; i--) {
      MelodyBase base = new MelodyBase(notes[i]);
      base.setNext(last);
      last = base;
    }
    return last;
  }
}
