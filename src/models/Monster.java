package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static jm.constants.Durations.WHOLE_NOTE;
import static jm.constants.Pitches.C4;
import static utils.Utils.checkNotNull;
import static utils.Utils.deepClone;
import static utils.Utils.zip;

import javafx.util.Pair;
import jm.music.data.Note;
import models.Model.Melody;
import models.Model.SimpleNote;
import models.Transforms.DiatonicTranspose;

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
    song.name = getName();
    return song.clone();
  }

  private void constructSong() { //fixme test
    List<Note> motif = new ArrayList<>();
    for (SimpleNote note : genes.getMelody()) { //fixme keep motifs separate
      motif.add(new Note(note.pitch, note.length));
    }

    List<Note> notes = new ArrayList<>();
    for (Pair<Chord, Transforms.Transform> measureChanges : zip(genes.getChords(), genes.getVariations(), Chord.I, new Transforms.Identity())) {
      Chord chord = measureChanges.getKey();
      Transforms.Transform variation = measureChanges.getValue();
      List<Note> measure = new DiatonicTranspose(C4, chord.scaleTones).transform(deepClone(motif)); //fixme use a locus?
      measure = variation.transform(measure);
      notes.addAll(measure);
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
    return genes.getChords().toString() + "\t" + genes.getMelody().toString();
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
