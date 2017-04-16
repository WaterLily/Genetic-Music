package models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import static jm.constants.Durations.WHOLE_NOTE;
import static utils.Utils.checkNotNull;

import jm.music.data.Note;

/**
 * Models a singing creature.
 */
public class Monster {
  private Model.MelodyBase melodyChromosome1;
  private Model.MelodyBase melodyChromosome2;
  private List<Transforms.Transform> genes; //todo populate from genes during creation
  private Random random;

  public Monster(Model.Gamete gamete1, Model.Gamete gamete2) {
    this.melodyChromosome1 = checkNotNull(gamete1).melodyChromosome;
    this.melodyChromosome2 = checkNotNull(gamete2).melodyChromosome;
    this.random = new Random();
  }

  public Monster(Model.SimpleNote[] notes1, Model.SimpleNote[] notes2) { //fixme shouldn't exist
    this.melodyChromosome1 = checkNotNull(Model.makeMelodyBases(notes1));
    this.melodyChromosome2 = checkNotNull(Model.makeMelodyBases(notes2));
    this.random = new Random();
  }

  // Visible for testing
  void setRandom(Random random) {
    this.random = random;
  }

  public Model.Melody getMelody() {
    List<Note> notes = new ArrayList<>();
    for (Model.MelodyBase base = melodyChromosome1; base != null; base = base.next()) {
      notes.add(new Note(base.note.pitch, WHOLE_NOTE / (double) base.note.fraction));
    }
    for (Model.MelodyBase base = melodyChromosome2; base != null; base = base.next()) {
      notes.add(new Note(base.note.pitch, WHOLE_NOTE / (double) base.note.fraction));
    }
    // todo: apply all genes
    if (Integer.parseInt(getName()) % 2 == 0) {
      System.out.println("transforming " + getName());
      notes = new Transforms.DiatonicTranspose(0, 5).transform(notes);
      notes = new Transforms.Shift(true).transform(notes);
    }
    return new Model.Melody(getName(), notes);
  }

  public Model.Gamete makeGamete() {
    Model.Gamete.Builder g = Model.Gamete.builder();
    boolean r = random.nextDouble() < 0.5;
    Model.MelodyBase first = r ? melodyChromosome1 : melodyChromosome2;
    Model.MelodyBase second = r ? melodyChromosome2 : melodyChromosome1;
    Model.MelodyBase fakeBase = new Model.MelodyBase(new Model.SimpleNote(-1, 8));
    addNotes(fakeBase, first, second);
    // todo add other genes
    return g.setMelody(fakeBase.next()).build();
  }

  private void addNotes(Model.MelodyBase base, Model.MelodyBase current, Model.MelodyBase other) {
    if (current == null) {
      return;
    } else {
      base.setNext(new Model.MelodyBase(current.note));
    }
    boolean cross = random.nextDouble() < getCrossoverChance(current, other);
    Model.MelodyBase first = cross ? other : current;
    Model.MelodyBase second = cross ? current : other;
    addNotes(base.next(), first.next(), second.next());
  }

  private double getCrossoverChance(Model.MelodyBase one, Model.MelodyBase two) {
    return 0.25;
  }

  public String getName() {
    return hashCode() + "";
  }

  @Override
  public String toString() {
    return melodyChromosome1.toString() + " " + melodyChromosome2.toString();
  }
}
