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

  // Visible for testing
  Monster setRandom(Random random) {
    this.random = random;
    return this;
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
      notes = transform.transform(notes);
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
}
