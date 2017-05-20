package models;

import static jm.constants.Chords.MAJOR;
import static jm.constants.Chords.MINOR;
import static jm.constants.Scales.MAJOR_SCALE;
import static jm.constants.Scales.MINOR_SCALE;

/** Models chords relative to a key. */
enum Chord { // TODO add 7s and possibly others
  I(0, 0, MAJOR, MAJOR_SCALE),
  ii(2, 1, MINOR, MINOR_SCALE),
  iii(4, 2, MINOR, MINOR_SCALE),
  IV(5, 3, MAJOR, MAJOR_SCALE),
  V(7, 4, MAJOR, MAJOR_SCALE),
  vi(9, 5, MINOR, MINOR_SCALE);

  final int semitones;
  final int scaleTones;
  final int[] notes;
  final int[] scale;

  Chord(int semitones, int scaleTones, int[] notes, int[] scale) {
    this.semitones = semitones;
    this.scaleTones = scaleTones;
    this.notes = notes;
    this.scale = scale;
  }

  public enum Progressions { // TODO make more extensive
    I_IV_V7_I(I, IV, V, I),
    I_ii_V_I(I, ii, V, I),
    I_IV_ii_V(I, IV, ii, I),
    IV_I_IV_V(IV, I, IV, V),
    IV_I_ii_V(IV, I, ii, V),
    IV_V(IV, V, IV, V),
    I_vi_ii_V(I, vi, ii, I);

    final Chord[] chords;

    Progressions(Chord... chords) {
      this.chords = chords;
    }
  }
}
