package models;

import static junit.framework.TestCase.assertEquals;
import static models.Constants.KEY_C;
import static models.Constants.RELATIVE_MINOR_SCALE_OFFSET;
import static models.Locus.MODE;
import static models.Locus.TIME_OFFSET;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** Unit tests for {@link Locus}. */
@RunWith(JUnit4.class)
public class LocusTest {
  // TODO: test handling illegal Allele arguments

  private static final Transforms.Transform IDENTITY = new Transforms.Identity();

  @Test
  public void testMode() {
    testExpression(
        MODE,
        Allele.MINOR,
        Allele.MINOR,
        new Transforms.DiatonicTranspose(KEY_C, RELATIVE_MINOR_SCALE_OFFSET));
    testExpression(MODE, Allele.DEFAULT, Allele.MINOR, IDENTITY);
    testExpression(MODE, Allele.DEFAULT, Allele.DEFAULT, IDENTITY);
  }

  @Test
  public void testTimeOffset() {
    Allele.DoubleAllele w = new Allele.DoubleAllele(2);
    Allele.DoubleAllele x = new Allele.DoubleAllele(3);
    Allele.DoubleAllele y = new Allele.DoubleAllele(-2);
    Allele.DoubleAllele z = new Allele.DoubleAllele(-3);
    Allele.DoubleAllele zero = new Allele.DoubleAllele(0);

    testExpression(TIME_OFFSET, w, x, new Transforms.Shift(2));
    testExpression(TIME_OFFSET, y, z, new Transforms.Shift(-2));
    testExpression(TIME_OFFSET, zero, x, IDENTITY);
    testExpression(TIME_OFFSET, zero, y, IDENTITY);
    testExpression(TIME_OFFSET, x, y, IDENTITY);
  }

  private static void testExpression(
      Locus locus, Allele allele1, Allele allele2, Transforms.Transform expected) {
    assertEquals(locus.getExpression(allele1, allele2), expected);
    if (allele1 != allele2) {
      assertEquals(locus.getExpression(allele2, allele1), expected);
    }
  }
}
