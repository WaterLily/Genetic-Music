package models;

import static jm.constants.Pitches.A4;
import static jm.constants.Pitches.C4;
import static jm.constants.Pitches.C5;
import static jm.constants.Pitches.CS5;
import static jm.constants.Pitches.E4;
import static jm.constants.Pitches.E5;
import static jm.constants.Pitches.G4;
import static junit.framework.TestCase.assertEquals;

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

    assertListEquals(new Transforms.DiatonicTranspose(C4, 5).transform(original), expected);
  }

  @Test
  public void testTranspose() {
    List<Note> original = Arrays.asList(new Note(C4, 1), new Note(E4, 0.5), new Note(G4, 2));
    List<Note> expected = Arrays.asList(new Note(A4, 1), new Note(CS5, 0.5), new Note(E5, 2));

    assertListEquals(new Transforms.Transpose(9).transform(original), expected);
  }

  @Test
  public void testShift() {
    List<Note> original = Arrays.asList(new Note(C4, 1), new Note(E4, 0.5), new Note(G4, 2));
    List<Note> expected =
        Arrays.asList(new Note(C4, 0.75), new Note(E4, 0.5), new Note(G4, 2), new Note(C4, 0.25));

    assertListEquals(new Transforms.Shift(true).transform(original), expected);
    // todo test other direction
  }

  private void assertListEquals(List<Note> actual, List<Note> expected) {
    assertEquals(listToString(actual), listToString(expected));
  }

  private String listToString(List<Note> notes) {
    String result = "";
    for (Note note : notes) {
      result += note.toString() + "\n";
    }
    return result;
  }
}
