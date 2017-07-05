package models;

import jm.constants.Scales;
import jm.music.data.Note;
import jm.music.data.Phrase;
import jm.music.tools.Mod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/** Transformations of musical phrases. */
class Transforms {

  static abstract class Transform {
    abstract List<Note> transform(List<Note> phrase);

    @Override
    public abstract boolean equals(Object other);

    static void checkLength(List<Note> original, List<Note> result) {
      if (length(original) != length(result)) {
        throw new RuntimeException("The transformation changed the length.");
      }
    }
  }

  final static class Identity extends Transform {
    @Override
    List<Note> transform(List<Note> phrase) {
      return new ArrayList<>(phrase);
    }

    @Override
    public boolean equals(Object other) { //fixme test (and others)
      return other instanceof Identity;
    }
  }

  final static class DiatonicTranspose extends Transform {
    private final int startKey;
    private final int scaleTones;

    DiatonicTranspose(int startKey, int scaleTones) {
      this.startKey = startKey;
      this.scaleTones = scaleTones;
    }

    @Override
    List<Note> transform(List<Note> phrase) {
      Phrase temp = new Phrase(phrase.toArray(new Note[phrase.size()]));
      Mod.transpose(temp, scaleTones, Scales.MAJOR_SCALE, startKey);
      return Arrays.asList(temp.getNoteArray());
    }

    @Override
    public boolean equals(Object other) {
      if (!(other instanceof DiatonicTranspose)) {
        return false;
      }
      DiatonicTranspose cast = (DiatonicTranspose) other;
      return cast.startKey == this.startKey && cast.scaleTones == this.scaleTones;
    }

    @Override
    public String toString() {
      return "DiatonicTranspose(" + startKey + ", " + scaleTones + ")";
    }
  }

  final static class Transpose extends Transform {
    private final int semitones;

    Transpose(int semitones) {
      this.semitones = semitones;
    }

    /*
    TODO: Clone notes (don't clown notes, that is a different thing to cloning).
     */
    @Override
    List<Note> transform(List<Note> phrase) {
      Phrase temp = new Phrase(phrase.toArray(new Note[phrase.size()]));
      Mod.transpose(temp, semitones);
      return Arrays.asList(temp.getNoteArray());
    }

    @Override
    public boolean equals(Object other) {
      return other instanceof Transpose && ((Transpose) other).semitones == this.semitones;
    }
  }

  final static class Shift extends Transform {
    private final double shift;
    private final boolean right;

    /**
     * @param shift The rhythm-value to shift.  Positive shifts to the right, Negative
     *              to the left.
     */
    Shift(double shift) {
      this.right = shift > 0;
      this.shift = Math.abs(shift);
    }

    @Override
    List<Note> transform(List<Note> input) {
      List<Note> result = new ArrayList<>(input);
      double cutLength = 0;
      while (cutLength < shift) {
        cutLength += getFromSource(result).getRhythmValue();
        addToDest(result, removeFromSource(result));
      }
      double overShoot = cutLength - shift;
      if (overShoot > 0) { // Put part of the last moved note back where it came from
        Note split = removeFromDest(result);
        Note forDest = new Note(split.getPitch(), split.getRhythmValue() - overShoot);
        addToDest(result, forDest);
        Note forSource = new Note(split.getPitch(), overShoot);
        addToSource(result, forSource);
      }
      checkLength(input, result);
      return result;
    }

    private Note getFromSource(List<Note> notes) {
      return notes.get(right ? notes.size() - 1 : 0);
    }

    private Note removeFromDest(List<Note> notes) {
      return notes.remove(right ? 0 : notes.size() - 1);
    }

    private Note removeFromSource(List<Note> notes) {
      return notes.remove(right ? notes.size() - 1 : 0);
    }

    private void addToSource(List<Note> notes, Note note) {
      notes.add(right ? notes.size() : 0, note);
    }

    private void addToDest(List<Note> notes, Note note) {
      notes.add(right ? 0 : notes.size(), note);
    }

    @Override
    public boolean equals(Object other) {
      return other instanceof Shift && ((Shift) other).shift == this.shift;
    }
  }

  public static class Articulation extends Transform {
    private final double lengthFactor;
    private final static double NEIGHBORHOOD = 0.01;

    public Articulation(double d) {
      this.lengthFactor = d;
    }

    @Override
    List<Note> transform(List<Note> phrase) {
      Phrase temp = new Phrase(phrase.toArray(new Note[phrase.size()]));

      return null;
    }

    @Override
    public boolean equals(Object other) {
      return other instanceof Articulation && Math.abs(this.lengthFactor - ((Articulation) other).lengthFactor) < NEIGHBORHOOD;
    }
  }

  final static class FullTranspose extends Transform {
    private final int[] sourceScale;
    private final int tonic;
    private final int[] targetScale;
    private final int targetTonic;

    FullTranspose(int[] sourceScale, int tonic, int[] targetScale, int targetTonic) {
      this.sourceScale = sourceScale;
      this.tonic = tonic;
      this.targetScale = targetScale;
      this.targetTonic = targetTonic;
    }

    /*
    TODO: Clone notes (don't clown notes, that is a different thing to cloning).
     */
    @Override
    List<Note> transform(List<Note> phrase) {
      return phrase;
    }

    @Override
    public boolean equals(Object other) {
      return true; //fixme implement
    }
  }

  final static class Reverse extends Transform {
    private final boolean rhythms;
    private final boolean pitches;

    public Reverse(boolean rhythms, boolean pitches) {
      this.rhythms = rhythms;
      this.pitches = pitches;
    }

    @Override
    List<Note> transform(List<Note> phrase) {
      List<Note> backwards = new ArrayList<>();
      for (Note note : phrase) {
        backwards.add(0, note.copy());
      }
      for (int i = 0; i < backwards.size(); i++) {
        // Swap 'em back
        if (!rhythms) {
          backwards.get(i).setRhythmValue(phrase.get(i).getRhythmValue(), true);
        }
        if (!pitches) {
          backwards.get(i).setPitch(phrase.get(i).getPitch());
        }
      }
      return backwards;
    }

    @Override
    public boolean equals(Object other) {
      throw new UnsupportedOperationException();
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
