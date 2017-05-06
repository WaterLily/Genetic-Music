package models;

import static jm.constants.Durations.SIXTEENTH_NOTE;
import static models.Constants.TONES_IN_SCALE;
import static utils.Utils.checkNotNull;

import jm.constants.Pitches;
import jm.constants.Scales;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/** Models a haploid monster cell. */
public class Gamete {
  final Map<Locus, Allele> transformAleles;
  final Genome.MelodyBase melodyAlleles;

  private Gamete(Map<Locus, Allele> transformAleles, Genome.MelodyBase melodyAlleles) {
    this.transformAleles = transformAleles;
    this.melodyAlleles = melodyAlleles;
  }

  static Builder builder() {
    return new Builder();
  }

  static final class Builder {
    private Map<Locus, Allele> transformAlleles;
    private Genome.MelodyBase melodyAlleles;

    Builder setMelody(Genome.MelodyBase melodyAlleles) {
      this.melodyAlleles = checkNotNull(melodyAlleles);
      return this;
    }

    Builder addAllele(Locus locus, Allele allele) {
      transformAlleles.put(locus, allele);
      return this;
    }

    Gamete build() {
      if (melodyAlleles == null) {
        throw new IllegalStateException("Missing required field: melody");
      }
      return new Gamete(transformAlleles, melodyAlleles);
    }

    private Builder() {
      this.transformAlleles = new HashMap<>();
    }
  }

  public static Gamete generate(Random random) { // TODO make more flexible
    Builder builder = builder();
    int[] pitchOptions = new int[12];
    pitchOptions[0] = pitchOptions[1] = Pitches.REST;
    int offset = 2;
    for (int i = 0; i < pitchOptions.length; i++) {
      pitchOptions[i + offset] =
          Scales.MAJOR_SCALE[i % Scales.MAJOR_SCALE.length]
              + Pitches.C4
              + ((i/Scales.MAJOR_SCALE.length) * TONES_IN_SCALE);
    }

    int MCL = 4; // measure length
    int LENGTH = 4; // quarter notes
    Model.SimpleNote[] notes = new Model.SimpleNote[MCL];

    for (int i = 0; i < MCL; i++) {
      notes[i] =
          new Model.SimpleNote(pitchOptions[(int) (Math.random() * pitchOptions.length)], LENGTH);
    }
    builder.setMelody(makeMelodyBases(notes));
    builder.addAllele(
        Locus.TIME_OFFSET,
        new Allele.DoubleAllele(random.nextDouble() < 0.5 ? -SIXTEENTH_NOTE : 0));

    return builder.build();
  }

  static Genome.MelodyBase makeMelodyBases(Model.SimpleNote[] notes) {
    Genome.MelodyBase last = null;
    for (int i = notes.length - 1; i >= 0; i--) {
      Genome.MelodyBase base = new Genome.MelodyBase(notes[i]);
      base.setNext(last);
      last = base;
    }
    return last;
  }
}
