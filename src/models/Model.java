package models;

import static utils.Utils.checkNotNull;

import jm.music.data.Note;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/** Stores game state and contains classes to internally represent music elements. */
public class Model {

  private static List<Monster> creatures = new ArrayList<>();

  public static List<Monster> creatures() {
    return new ArrayList<>(creatures);
  }

  public static void addCreature(Monster creature) {
    creatures.add(creature);
  }

  public static void setCreatures(List<Monster> newCreatures) {
    creatures.clear();
    creatures.addAll(newCreatures);
  }

  public static class Melody implements Serializable {
    public String name; // fixme make setter/getter
    public final List<Note> notes;

    public Melody(String name, List<Note> notes) {
      this.notes = checkNotNull(notes);
      this.name = name;
    }

    public String toString() {
      return notes.toString();
    }
  }

  static class SimpleNote implements Serializable {
    final int pitch; // 60 = Middle C
    final double length; // 1 = quarter note

    public SimpleNote(int pitch, double length) {
      this.pitch = pitch;
      this.length = length;
    }

    @Override
    public String toString() {
      String note;
      if (pitch <= 0) {
        note = "--";
      } else {
        int octave = (pitch / 12) - 1;
        int noteIndex = (pitch % 12);
        note = noteString[noteIndex] + octave;
      }
      return note + "(" + length + ")";
    }

    @Override
    public boolean equals(Object other) {
      return other instanceof SimpleNote
          && this.pitch == ((SimpleNote) other).pitch
          && Double.compare(this.length, ((SimpleNote) other).length) == 0;
    }
  }

  private static final String[] noteString =
      new String[]{"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
}
