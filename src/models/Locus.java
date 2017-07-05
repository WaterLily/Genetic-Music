package models;

import static models.Allele.DEFAULT;
import static models.Allele.REVERSE_PITCHES;
import static models.Allele.REVERSE_RHYTHM;
import static models.Constants.KEY_C;
import static models.Constants.RELATIVE_MINOR_SCALE_OFFSET;

import java.util.HashSet;
import java.util.Set;

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
  },
  VARIATION {
    @Override
    Transforms.Transform getExpression(Allele a1, Allele a2) {
      checkValidAlleleType(a1);
      checkValidAlleleType(a2);
      Set<Allele> alleles = new HashSet<>();
      alleles.add(a1);
      alleles.add(a2);
      //fixme do cycling
      return new Transforms.Reverse(
          alleles.contains(REVERSE_RHYTHM), alleles.contains(REVERSE_PITCHES));
    }

    void checkValidAlleleType(Allele a) {
      if (!(a == DEFAULT || a == REVERSE_PITCHES || a ==  REVERSE_RHYTHM || a instanceof Allele.IntAllele)) {
        throw new IllegalArgumentException();
      }
    }
  };

  /** Calculates how a gene will be expressed based on the given alleles. */
  abstract Transforms.Transform getExpression(Allele a1, Allele a2);
}
