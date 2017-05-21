package music;

import models.Model;

import java.util.Set;

public interface MusicService {

  void addPart(Model.Melody notes);

  void removePart(Model.Melody notes);

  void play(Set<Model.Melody> allSongs, Set<String> activeSongs);

  void pause();
}