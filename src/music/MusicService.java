package music;

import jm.music.data.Note;
import models.Model;

import java.util.Set;

public interface MusicService {

  void addPart(Model.Melody notes);

  void removePart(Model.Melody notes);

  void play(Set<Model.Melody> allSongs, Set<String> activeSongs);

  void pause();

  static Note transform(Model.SimpleNote modelNote) {
    return new Note(modelNote.pitch, 1.0 / (double) modelNote.fraction);
  }
}