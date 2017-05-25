package models;

import static jm.constants.Durations.SIXTEENTH_NOTE;
import static jm.constants.Pitches.*;
import static junit.framework.TestCase.assertEquals;
import static models.Constants.RELATIVE_MINOR_SCALE_OFFSET;

import jm.constants.Scales;
import jm.music.data.Note;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.List;

/** Unit tests for {@link Transforms}. */
@RunWith(JUnit4.class)
public class TransformsTest {

  @Test
  public void testDiatonicTranspose() {
    List<Note> original = Arrays.asList(new Note(C4, 1), new Note(E4, 0.5), new Note(G4, 2));
    List<Note> expected = Arrays.asList(new Note(A4, 1), new Note(C5, 0.5), new Note(E5, 2));

    assertListEquals(
        expected,
        new Transforms.DiatonicTranspose(C4, RELATIVE_MINOR_SCALE_OFFSET).transform(original));
  }

  @Test
  public void testTranspose() {
    List<Note> original = Arrays.asList(new Note(C4, 1), new Note(E4, 0.5), new Note(G4, 2));
    List<Note> expected = Arrays.asList(new Note(A4, 1), new Note(CS5, 0.5), new Note(E5, 2));

    assertListEquals(expected, new Transforms.Transpose(9).transform(original));
  }

  @Test
  public void testShift() {
    List<Note> original = Arrays.asList(new Note(C4, 1), new Note(E4, 0.5), new Note(G4, 2));
    List<Note> leftExpected =
        Arrays.asList(new Note(C4, 0.75), new Note(E4, 0.5), new Note(G4, 2), new Note(C4, 0.25));
    List<Note> rightExpected =
        Arrays.asList(new Note(G4, 0.5), new Note(C4, 1), new Note(E4, 0.5), new Note(G4, 1.5));

    assertListEquals(leftExpected, new Transforms.Shift(-SIXTEENTH_NOTE).transform(original));
    assertListEquals(rightExpected, new Transforms.Shift(2 * SIXTEENTH_NOTE).transform(original));
  }

  private void assertListEquals(List<Note> expected, List<Note> actual) {
    assertEquals(listToString(expected), listToString(actual));
  }

  private String listToString(List<Note> notes) {
    String result = "";
    for (Note note : notes) {
      result += note.toString() + "\n";
    }
    return result;
  }
  @Test
  public void testFullTranspose(){
    List<Note> CMajor = Arrays.asList(new Note(C4, 1), new Note(E4, 1), new Note(G4, 1));
    List<Note> CMinor = Arrays.asList(new Note(C4, 1), new Note(EF4, 1), new Note(G4, 1));
    List<Note> FSMajor = Arrays.asList(new Note(FS4, 1), new Note(AS4, 1), new Note(CS4, 1));

    assertListEquals(CMinor, new Transforms.FullTranspose(Scales.MAJOR_SCALE, C4,Scales.MINOR_SCALE, C4 ).transform(CMajor));
    assertListEquals(FSMajor, new Transforms.FullTranspose(Scales.MAJOR_SCALE, C4, Scales.MAJOR_SCALE,FS4).transform(CMajor));
    assertListEquals(FSMajor, new Transforms.FullTranspose(Scales.MINOR_SCALE, C4, Scales.MAJOR_SCALE,FS4).transform(CMinor));
    // fixme also test rests
  }
}
