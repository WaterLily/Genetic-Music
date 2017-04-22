package models;

/** Models an allele in some DNA. */
public class Allele {

  static final Allele DEFAULT = new Allele();
  static final Allele MINOR = new Allele();
  static final Allele DOUBLE = new Allele();
  static final Allele HALF = new Allele();
  static final Allele UP = new Allele();
  static final Allele DOWN = new Allele();

  static class IntAllele extends Allele {
    final int i;

    IntAllele(int i) {
      this.i = i;
    }
  }

  static class DoubleAllele extends Allele {
    final double d;

    DoubleAllele(double d) {
      this.d = d;
    }
  }

  protected Allele() {}

  // TODO: add methods for serializing/deserializing
}
