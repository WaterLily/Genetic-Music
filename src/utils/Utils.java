package utils;

/**
 * Created by Lily on 4/2/2017.
 */
public class Utils {

  public static <E extends Object> E checkNotNull(E object) {
    if (object == null) {
      throw new NullPointerException();
    }
    return object;
  }
}
