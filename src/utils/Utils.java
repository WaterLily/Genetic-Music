package utils;

import javafx.util.Pair;
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

  public static <E, A> List<Pair<E, A>> zip(List<E> one, List<A> two, E default1, A default2) { // TODO test
    List<Pair<E, A>> result = new ArrayList<>();
    // TODO make copies first
    while (one.size() < two.size()) {
      one.add(default1);
    }
    while (two.size() < one.size()) {
      two.add(default2);
    }
    for (int i = 0; i < one.size(); i++) {
      result.add(new Pair<>(one.get(i), two.get(i)));
    }
    return result;
  }

  public static List<Note> deepClone(List<Note> notes) {
    List<Note> clone = new ArrayList<>();
    for (Note note : notes) {
      clone.add(note.copy());
    }
    return clone;
  }
}
