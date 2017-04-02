package models;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/** Unit tests for {@link Model}. */
public class ModelTest {

  @Test
  public void makeMelodyBases() throws Exception {
    Model.Note[] notes = {new Model.Note(60, 8), new Model.Note(62, 8), new Model.Note(64, 8)};

    Model.MelodyBase base = Model.makeMelodyBases(notes);

    assertEquals(base.toString(), getNotes(base), Arrays.asList(notes));
  }

  private List<Model.Note> getNotes(Model.MelodyBase base) {
    List<Model.Note> notes = new ArrayList<>();
    while (base != null) {
      notes.add(base.note);
      base = base.next();
    }
    return notes;
  }

}