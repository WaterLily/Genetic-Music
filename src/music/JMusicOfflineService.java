package music;

import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import jm.util.Play;
import models.Model;

import java.util.List;
import java.util.Set;

public class JMusicOfflineService implements MusicService {

  public JMusicOfflineService() {}

  @Override
  public void addPart(Model.Melody notes) {}

  @Override
  public void removePart(Model.Melody notes) {}

  @Override
  public void play(Set<Model.Melody> allSongs, Set<String> activeSongs) {
    Score score = new Score();
    for (Model.Melody part : allSongs) {
      if (activeSongs.contains(part.name)) {
        score.add(new Part(makePhrase(part.notes)));
      }
    }
    play(score);
  }

  @Override
  public void pause() {
  }

  private Phrase makePhrase(List<Model.Note> notes) {
    Note[] noteArray = new Note[notes.size()];
    for (int i = 0; i < notes.size(); i++) {
      noteArray[i] = MusicService.transform(notes.get(i));
    }
    return new Phrase(noteArray);
  }

  private void play(Score score) {
    Play.midi(score);
  }
}