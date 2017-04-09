package music;

import jm.audio.Instrument;
import jm.audio.RTMixer;
import jm.music.data.Note;
import jm.music.rt.RTLine;
import models.Model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Implementation of {@link MusicService} that allows real-time interaction with the music. */
public class JMusicRealTimeService implements MusicService {

  private RTMixer mixer;
  private final Map<String, Line> lineMap;

  public JMusicRealTimeService() {
    this.lineMap = new HashMap<>();
  }

  @Override
  public void addPart(Model.Melody notes) {
    if (lineMap.containsKey(notes.name)) {
      lineMap.get(notes.name).sound();
    }
  }

  @Override
  public void removePart(Model.Melody notes) {
    if (lineMap.containsKey(notes.name)) {
      lineMap.get(notes.name).silence();
    }
  }

  @Override
  public void play(Set<Model.Melody> allSongs, Set<String> activeSongs) {
    lineMap.clear();
    Line[] lines = new Line[allSongs.size()];
    int i = 0;
    for (Model.Melody song : allSongs) {
      lines[i] = new Line(new Instrument[]{new SimpleInst()}, song.notes);
      lineMap.put(song.name, lines[i]);
      if (activeSongs.contains(song.name)) {
        lines[i].sound();
      }
      i++;
    }
    mixer = new RTMixer(lines);
    mixer.begin();
  }

  @Override
  public void pause() {
    mixer.stop();
  }

  private static class Line extends RTLine {

    final List<Model.Note> notes;
    int index;
    boolean sound;

    Line(Instrument[] instruments, List<Model.Note> notes) {
      super(instruments);
      this.notes = notes;
      this.index = 0;
    }

    @Override
    public synchronized Note getNextNote() {
      Note playNote = MusicService.transform(notes.get(index));
      if (!sound) {
        playNote.setPitch(Note.MIN_PITCH);
      }
      index = (index + 1) % notes.size();
      return playNote;
    }

    void silence() {
      sound = false;
    }

    void sound() {
      sound = true;
    }
  }
}
