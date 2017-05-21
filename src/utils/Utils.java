package utils;

import jm.music.data.Note;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Lily on 4/2/2017.
 */
public class Utils {

  public static <E> E checkNotNull(E object) {
    if (object == null) {
      throw new NullPointerException();
    }
    return object;
  }

  public static List<Note> deepClone(List<Note> notes) {
    List<Note> clone = new ArrayList<>();
    for (Note note : notes) {
      clone.add(note.copy());
    }
    return clone;
  }
}
