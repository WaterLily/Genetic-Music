package models;

import static models.Constants.KEY_C;
import static models.Constants.RELATIVE_MINOR_SCALE_OFFSET;

/** Models a locus in a genome for the purpose of transformational genes. */
enum Locus {

  MODE {
    @Override
    Transforms.Transform getExpression(Allele a1, Allele a2) {
      if (a1 == Allele.DEFAULT || a2 == Allele.DEFAULT) {
        return new Transforms.Identity();
      } else if (a1 == Allele.MINOR && a2 == Allele.MINOR) {
        return new Transforms.DiatonicTranspose(KEY_C, RELATIVE_MINOR_SCALE_OFFSET);
      }
      throw new IllegalArgumentException();
    }
  },
  TIME_OFFSET  {
    @Override
    Transforms.Transform getExpression(Allele a1, Allele a2) {
      if (a1 instanceof Allele.DoubleAllele && a2 instanceof Allele.DoubleAllele) {
        return getExpression(((Allele.DoubleAllele) a1).d, ((Allele.DoubleAllele) a2).d);
      }
      throw new IllegalArgumentException();
    }

    private Transforms.Transform getExpression(double x, double y) {
      if (x * y <= 0) {
        return new Transforms.Identity();
      } else {
        return new Transforms.Shift(Math.abs(x) < Math.abs(y) ? x : y);
      }
    }
  },
  ARTICULATION {
    @Override
    Transforms.Transform getExpression(Allele a1, Allele a2) {
    if (a1 instanceof Allele.DoubleAllele && a2 instanceof Allele.DoubleAllele){
      return new Transforms.Articulation(.5*(((Allele.DoubleAllele) a1).d+((Allele.DoubleAllele) a2).d));
    }
    throw new IllegalArgumentException();

    }
  },
  MULTIPLICATION {
    @Override
    Transforms.Transform getExpression(Allele a1, Allele a2) {
      throw new UnsupportedOperationException();
    }
  },
  SLUR {
    @Override
    Transforms.Transform getExpression(Allele a1, Allele a2) {
      throw new UnsupportedOperationException();
    }
  },
  TRANSPOSE {
    @Override
    Transforms.Transform getExpression(Allele a1, Allele a2) {
      throw new UnsupportedOperationException();
    }
  };

  /** Calculates how a gene will be expressed based on the given alleles. */
  abstract Transforms.Transform getExpression(Allele a1, Allele a2);
}
