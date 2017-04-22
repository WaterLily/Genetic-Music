package models;

import static models.Constants.KEY_C;
import static models.Constants.RELATIVE_MINOR_SCALE_OFFSET;

/** Models a locus in a genome. */
abstract class Locus {

  static final Locus MODE = new Locus() {
    @Override
    Transforms.Transform getExpression(Allele a1, Allele a2) {
      if (a1 == Allele.DEFAULT || a2 == Allele.DEFAULT) {
        return new Transforms.Identity();
      } else if (a1 == Allele.MINOR && a2 == Allele.MINOR) {
        return new Transforms.DiatonicTranspose(KEY_C, RELATIVE_MINOR_SCALE_OFFSET);
      }
      throw new IllegalArgumentException();
    }
  };

  static final Locus TIME_OFFSET = new Locus() {
    @Override
    Transforms.Transform getExpression(Allele a1, Allele a2) {
      if (a1 instanceof Allele.IntAllele && a2 instanceof Allele.IntAllele) {
        return getExpression(((Allele.IntAllele) a1).i, ((Allele.IntAllele) a2).i);
      }
      throw new IllegalArgumentException();
    }

    private Transforms.Transform getExpression(int x, int y) {
      if (x * y <= 0) {
        return new Transforms.Identity();
      } else {
        return new Transforms.Shift(Math.abs(x) < Math.abs(y) ? x : y);
      }
    }
  };

  static final Locus ARTICULATION = new Locus() {
    @Override
    Transforms.Transform getExpression(Allele a1, Allele a2) {
      throw new UnsupportedOperationException();
    }
  };

  static final Locus MULTIPLICATION = new Locus() {
    @Override
    Transforms.Transform getExpression(Allele a1, Allele a2) {
      throw new UnsupportedOperationException();
    }
  };

  static final Locus SLUR = new Locus() {
    @Override
    Transforms.Transform getExpression(Allele a1, Allele a2) {
      throw new UnsupportedOperationException();
    }
  };

  /** Calculates how a gene will be expressed based on the given alleles. */
  abstract Transforms.Transform getExpression(Allele a1, Allele a2);
}
