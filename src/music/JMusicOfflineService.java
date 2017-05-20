package music;

import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import jm.util.Play;
import jm.util.View;
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
        Part jPart = new Part(makePhrase(part.name, part.notes));
        jPart.setTitle(part.name);
        score.add(jPart);
      }
    }

    score.setNumerator(3); //TODO base on creatures
    play(score);
    View.notate(score);
  }

  @Override
  public void pause() {
  }

  private Phrase makePhrase(String name, List<Note> notes) {
    Phrase phrase = new Phrase(notes.toArray(new Note[notes.size()]));
    phrase.setTitle(name);
    return phrase;
  }

  private void play(Score score) {
    Play.midi(score);
  }
}