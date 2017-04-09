package music;

import jm.music.data.Note;
import models.Model;

public interface MusicService {

  void addPart(Model.Melody notes);

  void removePart(Model.Melody notes);

  void play();

  void pause();

  static Note transform(Model.Note note) {
    return new Note(note.pitch, 1.0 / (double) note.fraction);
  }
}