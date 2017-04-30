package models;

import static utils.Utils.checkNotNull;

import jm.music.data.Note;

import java.util.List;

/** Contains classes to internally represent music elements. */
public class Model {

  public static class Melody {
    public final String name;
    public final List<Note> notes;

    public Melody(String name, List<Note> notes) {
      this.notes = checkNotNull(notes);
      this.name = name;
    }

    public String toString() {
      return notes.toString();
    }
  }

  public static class SimpleNote {
    public final int pitch; // 60 = Middle C
    public final int fraction; // 4 = quarter note

    public SimpleNote(int pitch, int fraction) {
      this.pitch = pitch;
      this.fraction = fraction;
    }

    @Override
    public String toString() {
      if (pitch <= 0) return "--";

      int octave = (pitch / 12) - 1;
      int noteIndex = (pitch % 12);
      String note = noteString[noteIndex];
      return note + octave;
    }

    @Override
    public boolean equals(Object other) {
      return other instanceof SimpleNote
          && this.pitch == ((SimpleNote) other).pitch
          && this.fraction == ((SimpleNote) other).fraction;
    }
  }

  private static final String[] noteString =
      new String[]{"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
}
