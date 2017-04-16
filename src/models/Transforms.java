package models;

import static jm.constants.Durations.SIXTEENTH_NOTE;

import jm.constants.Scales;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.tools.Mod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/** Transformations of musical phrases. */
public class Transforms {

  static abstract class Transform {
    abstract List<Note> transform(List<Note> phrase);

    void checkLength(List<Note> original, List<Note> result) {
      if (length(original) != length(result)) {
        throw new RuntimeException("The transformation changed the length.");
      }
    }
  }

  static class DiatonicTranspose extends Transform { //fixme test
    private final int startKey;
    private final int semitones;

    DiatonicTranspose(int startKey, int semitones){
      this.startKey = startKey;
      this.semitones = semitones;
    }

    @Override
    List<Note> transform(List<Note> phrase) {
      Phrase temp = new Phrase(phrase.toArray(new Note[phrase.size()]));
      Mod.transpose(temp, semitones, Scales.MAJOR_SCALE, startKey);
      return Arrays.asList(temp.getNoteArray());
    }
  }

  static class Transpose extends Transform { //fixme test
    private final int semitones;

    Transpose(int semitones){
      this.semitones = semitones;
    }

    @Override
    List<Note> transform(List<Note> phrase) {
      Phrase temp = new Phrase(phrase.toArray(new Note[phrase.size()]));
      Mod.transpose(temp, semitones);
      return Arrays.asList(temp.getNoteArray());
    }
  }

  static class Shift extends Transform { //fixme test
    private final boolean shiftLeft;

    Shift(boolean shiftLeft) {
      this.shiftLeft = shiftLeft;
    }

    @Override
    List<Note> transform(List<Note> notes) {
      List<Note> result = new ArrayList<>(notes);
      // Assume shifting left by 1/16th note for now
      Note cut = result.remove(0);
      Note paste = new Note(cut.getPitch(), SIXTEENTH_NOTE);
      result.add(result.size(), paste);
      double leftover = cut.getRhythmValue() - SIXTEENTH_NOTE;
      if (leftover > 0) {
        Note remainder = new Note(cut.getPitch(), leftover);
        result.add(0, remainder);
      }
      checkLength(notes, result);
      return result;
    }
  }

  private static double length(Collection<Note> notes) {
    double length = 0;
    for (Note note : notes) {
      length += note.getRhythmValue();
    }
    return length;
  }
}
