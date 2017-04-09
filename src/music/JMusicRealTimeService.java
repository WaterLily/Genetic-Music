package music;

import jm.audio.Instrument;
import jm.audio.RTMixer;
import jm.music.data.Note;
import jm.music.rt.RTLine;
import models.Model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Implementation of {@link MusicService} that allows real-time interaction with the music. */
public class JMusicRealTimeService implements MusicService {

  private RTMixer mixer;
  private final Map<String, RTLine> lines;

  public JMusicRealTimeService() {
    this.lines = new HashMap<>();
  }

  @Override
  public void addPart(Model.Melody notes) {
    if (!lines.containsKey(notes.name)) {
      System.out.println("adding line");
      lines.put(notes.name, new Line(new Instrument[]{new SimpleInst()}, notes.notes));
      if (mixer == null) {
        System.out.println("initializing mixer");
        mixer = new RTMixer(new RTLine[]{lines.get(notes.name)});
        mixer.begin();
      } else {
        System.out.println("adding to mixer");
        mixer.addLines(new RTLine[]{lines.get(notes.name)});
      }
    } else {
      System.out.println("unpausing line");
      lines.get(notes.name).unPause(); // fixme probably not what I want
    }
  }

  @Override
  public void removePart(Model.Melody notes) {
    lines.get(notes.name).pause(); // fixme probably not what I want
  }

  @Override
  public void play() {
    System.out.println("playing mixer");
    mixer.unPause();
  }

  @Override
  public void pause() {
    System.out.println("pausing mixer");
    mixer.pause();
  }

  private static class Line extends RTLine {

    final List<Model.Note> notes;
    int index;

    public Line(Instrument[] instruments, List<Model.Note> notes) {
      super(instruments);
      this.notes = notes;
      this.index = 0;
    }

    @Override
    public Note getNextNote() {
      Model.Note note = notes.get(index);
      index = (index + 1) % notes.size();
      return MusicService.transform(note);
    }
  }
}
