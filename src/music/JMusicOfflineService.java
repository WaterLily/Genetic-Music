package music;

import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import jm.util.Play;
import models.Model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JMusicOfflineService implements MusicService {

  private final Map<String, Model.Melody> parts;

  public JMusicOfflineService() {
    parts = new HashMap<>();
  }

  @Override
  public void addPart(Model.Melody notes) {
    parts.put(notes.name, notes);
  }

  @Override
  public void removePart(Model.Melody notes) {
    parts.remove(notes.name);
  }

  @Override
  public void play() {
    Score score = new Score();
    for (Model.Melody part : parts.values()) {
      score.add(new Part(makePhrase(part.notes)));
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