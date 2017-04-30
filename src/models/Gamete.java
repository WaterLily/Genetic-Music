package models;

import static utils.Utils.checkNotNull;

import java.util.HashMap;
import java.util.Map;

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
}
