package models;

import static jm.constants.Durations.DOTTED_HALF_NOTE;
import static jm.constants.Durations.EIGHTH_NOTE;
import static jm.constants.Durations.HALF_NOTE;
import static jm.constants.Durations.QUARTER_NOTE;
import static jm.constants.Durations.WHOLE_NOTE;
import static models.Constants.TONES_IN_SCALE;
import static utils.Utils.checkNotNull;

import jm.constants.Pitches;
import jm.constants.Scales;
import models.Model.SimpleNote;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Models a haploid monster cell.
 */
public class Gamete {
  final Map<Locus, Allele> transformAlleles;
  final List<SimpleNote> melodyAlleles;
  final List<Chord> chordAlleles;
  final List<Allele> variationAlleles;

  private Gamete(
      Map<Locus, Allele> transformAlleles,
      List<SimpleNote> melodyAlleles,
      List<Chord> chordAlleles,
      List<Allele> variationAlleles) {
    this.transformAlleles = transformAlleles;
    this.melodyAlleles = melodyAlleles;
    this.chordAlleles = chordAlleles;
    this.variationAlleles = variationAlleles;
  }

  static Builder builder() {
    return new Builder();
  }

  static final class Builder {
    private Map<Locus, Allele> transformAlleles;
    private List<SimpleNote> melodyAlleles;
    private List<Chord> chordAlleles;
    private List<Allele> variationAlleles;

    Builder setMelody(List<SimpleNote> melodyAlleles) {
      this.melodyAlleles = checkNotNull(melodyAlleles);
      return this;
    }

    Builder setMelody(SimpleNote[] melodyAlleles) {
      this.melodyAlleles = Arrays.asList(checkNotNull(melodyAlleles));
      return this;
    }

    Builder setChords(List<Chord> chordAlleles) {
      this.chordAlleles = checkNotNull(chordAlleles);
      return this;
    }

    Builder setChords(Chord[] chordAlleles) {
      this.chordAlleles = Arrays.asList(checkNotNull(chordAlleles));
      return this;
    }

    Builder addAllele(Locus locus, Allele allele) {
      transformAlleles.put(locus, allele);
      return this;
    }

    Builder setVariationAlleles(List<Allele> variationAlleles) {
      this.variationAlleles = checkNotNull(variationAlleles);
      return this;
    }

    Gamete build() {
      if (melodyAlleles == null) {
        throw new IllegalStateException("Missing required field: melody");
      }
      if (chordAlleles == null) {
        throw new IllegalStateException("Missing required field: chords");
      }
      if (variationAlleles == null) {
        // TODO consider setting a default
        throw new IllegalStateException("Missing required field: variations");
      }
      return new Gamete(transformAlleles, melodyAlleles, chordAlleles, variationAlleles);
    }

    private Builder() {
      this.transformAlleles = new HashMap<>();
    }
  }

  public static Gamete generate(Random random) { // TODO make more flexible
    Builder builder = builder();

    int measures = 1;
    int beatsPerMeasure = 3;
    List<SimpleNote> notes = new ArrayList<>();
    for (int i = 0; i < measures; i++) {
      notes.addAll(randomMeasure(beatsPerMeasure, random));
    }

    Chord[] chords = Chord.Progressions.values()[random.nextInt(Chord.Progressions.values().length)].chords;

    List<Allele> variations = new ArrayList<>();
    for (int i = 0; i < chords.length; i++) {
      switch(random.nextInt(4)) {
        case 0:
          variations.add(Allele.REVERSE_PITCHES);
          break;
        case 1:
          variations.add(Allele.REVERSE_RHYTHM);
          break;
        case 2:
          variations.add(new Allele.IntAllele(random.nextInt(7) + 1));
          break;
        case 3:
          // fall through
        default:
          variations.add(Allele.DEFAULT);
      }
    }

    return builder
        .setMelody(notes)
        .setChords(chords)
        .setVariationAlleles(variations)
        .build();
  }

  private static int getRandomPitch(Random random) {
    int[] pitchOptions = new int[8];
    pitchOptions[0] = Pitches.REST;
    int offset = 1;
    for (int i = 0; i < pitchOptions.length - offset; i++) {
      pitchOptions[i + offset] = Scales.MAJOR_SCALE[i % Scales.MAJOR_SCALE.length] + Pitches.C4 + ((i / Scales.MAJOR_SCALE.length) * TONES_IN_SCALE);
    }
    return pitchOptions[random.nextInt(pitchOptions.length)];
  }

  private static List<SimpleNote> randomMeasure(double beatsPerMeasure, Random random) {
    double length = 0;
    List<NoteChoice> noteTypes = new ArrayList<>();
    List<SimpleNote> notes = new ArrayList<>();
    while (length < beatsPerMeasure) {
      NoteChoice noteChoice = NoteChoice.getRandom(beatsPerMeasure - length, random);
      noteTypes.add(noteChoice);
      length += noteChoice.length * noteChoice.count;
    }
    while (noteTypes.size() > 0) {
      NoteChoice noteChoice = noteTypes.remove(random.nextInt(noteTypes.size()));
      for (int i = 0; i < noteChoice.count; i++) {
        int pitch = getRandomPitch(random);
        notes.add(new SimpleNote(pitch, noteChoice.length));
      }
    }
    return notes;
  }

  private enum NoteChoice {
    TWO_EIGHTHS(2, EIGHTH_NOTE), QUARTER(1, QUARTER_NOTE), HALF(1, HALF_NOTE), THREE_QUARTER(1, DOTTED_HALF_NOTE), WHOLE(1, WHOLE_NOTE);

    private final int count;
    private final double length;

    NoteChoice(int count, double length) {
      this.count = count;
      this.length = length;
    }

    static NoteChoice getRandom(double cap, Random random) {
      if (cap < TWO_EIGHTHS.length) {
        throw new IllegalArgumentException();
      }
      int capIndex = 0;
      for (; capIndex < values().length; capIndex++) {
        if (values()[capIndex].length * values()[capIndex].count > cap) {
          break;
        }
      }
      return values()[random.nextInt(capIndex)];
    }
  }
}
