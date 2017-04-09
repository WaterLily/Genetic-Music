package models;

import static utils.Utils.checkNotNull;

import java.util.List;

/** Contains classes to internally represent music elements and model logic. */
public class Model {

  static class MelodyBase {
    final Note note;
    private MelodyBase next;

    MelodyBase(Note note) {
      this.note = note;
    }

    MelodyBase setNext(MelodyBase next) {
      this.next = next;
      return this;
    }

    MelodyBase next() {
      return next;
    }

    public String toString() {
      String result = note.toString();
      if (next != null) {
        result += " " + next.toString();
      }
      return result;
    }
  }

  public static class Melody {
    public final String name;
    public final List<Note> notes;

    public Melody(String name, List<Note> notes) {
      this.notes = checkNotNull(notes);
      this.name = name;
    }
  }

  public static class Note {
    public final int pitch; // 60 = Middle C
    public final int fraction; // 4 = quarter note

    public Note(int pitch, int fraction) {
      this.pitch = pitch;
      this.fraction = fraction;
    }

    @Override
    public String toString() { //fixme move to musicservice?
      if (pitch < 0) return "--";

      int octave = (pitch / 12) - 1;
      int noteIndex = (pitch % 12);
      String note = noteString[noteIndex];
      return note + octave;
    }

    @Override
    public boolean equals(Object other) {
      return other instanceof Note
          && this.pitch == ((Note) other).pitch
          && this.fraction == ((Note) other).fraction;
    }
  }

  private static final String[] noteString =
      new String[]{"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};


  static class Gamete {
    final MelodyBase melodyChromosome;

    private Gamete(MelodyBase melodyChromosome) {
      this.melodyChromosome = melodyChromosome;
    }

    static Builder builder() {
      return new Builder();
    }

    static final class Builder {
      private MelodyBase melodyChromosome;

      Builder setMelody(MelodyBase melodyBase) { //fixme allow building melodybases?
        this.melodyChromosome = checkNotNull(melodyBase);
        return this;
      }

      Gamete build() {
        if (melodyChromosome == null) {
          throw new IllegalStateException("Missing required field: melody");
        }
        return new Gamete(melodyChromosome);
      }

      private Builder() {}
    }

    @Override
    public String toString() {
      return melodyChromosome.toString();
    }
  }

  static MelodyBase makeMelodyBases(Note[] notes) {
    MelodyBase last = null;
    for (int i = notes.length - 1; i >= 0; i--) {
      MelodyBase base = new MelodyBase(notes[i]);
      base.setNext(last);
      last = base;
    }
    return last;
  }

  public static Monster breed(Monster parent1, Monster parent2) {
    Gamete one = parent1.makeGamete();
    Gamete two = parent2.makeGamete();
    return new Monster(one, two);
  }

}
