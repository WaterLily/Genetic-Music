package music;

import jm.audio.io.*;
import jm.audio.synth.*;
import jm.audio.AOException;

public final class SimpleInst extends jm.audio.Instrument{
  /** The number of channels */
  private int channels;
  private int sampleRate;

  public SimpleInst(){
    this.sampleRate = 44100;
    this.channels = 1;
  }

  public void createChain()throws AOException{
    Oscillator osc = new Oscillator(this, Oscillator.SINE_WAVE,
        this.sampleRate, this.channels);
    Envelope env = new Envelope(osc,
        new double[] {0.0, 0.0, 0.1, 1.0, 1.0, 0.0});
//    SampleOut sout = new SampleOut(env);
  }
}



