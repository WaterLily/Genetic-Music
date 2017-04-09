package music;

import jm.audio.Instrument;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Score;
import jm.music.rt.RTLine;

import javax.swing.JSlider;

/** Class to generate notes for playing/pausing music in real time. */
public class JRealTimeNotes extends RTLine {

  private final Part part;

  public JRealTimeNotes(Instrument instrument, Part part) {
    super(new Instrument[]{instrument});
    this.part = part;
  }

  @Override
  public Note getNextNote() {
    return new Note((int) (Math.random() * 60 + 30), 0.25, (int) (Math.random() * 100 + 27));
  }
}
