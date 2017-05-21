package models;

import static jm.constants.Durations.EIGHTH_NOTE;
import static jm.constants.Pitches.REST;
import static utils.Utils.checkNotNull;

import models.Model.SimpleNote;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/** Stores the DNA of a single monster. */
class Genome implements Serializable {
  private final PatternChromosome melodies;
  private final ChordChromosome chords;
  private final List<TransformChromosome> transformations;

  public Genome(Gamete one, Gamete two) {
    melodies = new PatternChromosome(one.melodyAlleles, two.melodyAlleles, new Random()); //fixme random
    chords = new ChordChromosome(one.chordAlleles, two.chordAlleles, new Random());
    transformations = new ArrayList<>();
    Set<Locus> loci = new HashSet<>();
    loci.addAll(one.transformAleles.keySet());
    loci.addAll(two.transformAleles.keySet());
    for (Locus locus : loci) { //TODO gracefully handle null alleles
      transformations.add(
          new TransformChromosome(
              locus, one.transformAleles.get(locus), two.transformAleles.get(locus)));
    }
  }

  Gamete getGamete(Random random) { //fixme test
    Gamete.Builder builder = Gamete.builder();
    for (TransformChromosome transformation : transformations) {
      builder.addAllele(transformation.locus, transformation.meiosis(random));
    }
    builder.setMelody(melodies.meiosis(random));
    builder.setChords(chords.meiosis(random));
    return builder.build();
  }

  List<Transforms.Transform> getTransforms() {
    List<Transforms.Transform> transforms = new ArrayList<>();
    for (TransformChromosome gene : transformations) {
      transforms.add(gene.present());
    }
    return transforms;
  }

  List<SimpleNote> getMelody() {
    return melodies.present();
  }

  List<Chord> getChords() {
    return chords.present();
  }

  private static abstract class Chromosome<E, A> implements Serializable { //fixme decide if this polymorphism is needed
    protected final E one;
    protected final E two;

    Chromosome(E one, E two) {
      this.one = one;
      this.two = two;
    }

    abstract E meiosis(Random random);
    abstract A present();
  }

  private static class ChordChromosome extends Chromosome<List<Chord>, List<Chord>> {
    private final boolean swap;

    ChordChromosome(List<Chord> one, List<Chord> two, Random random) {
      super(one, two);
      swap = random.nextBoolean();
    }

    @Override
    List<Chord> meiosis(Random random) { // TODO recombination
      return random.nextBoolean() ? one : two;
    }

    @Override
    List<Chord> present() { // TODO combine?
      return swap ? one : two;
    }
  }

  private static class PatternChromosome extends Chromosome<List<SimpleNote>, List<SimpleNote>> {
    private final boolean swap;

    PatternChromosome(List<SimpleNote> one, List<SimpleNote> two, Random random) {
      super(one, two);
      swap = random.nextBoolean();
    }

    @Override
    List<SimpleNote> meiosis(Random random) { // TODO test
      double firstLength = length(one);
      double secondLength = length(two);
      int numEights = numEights(swap ? firstLength : secondLength);
      int[] firstPitches = pitches(one, numEights);
      int[] secondPitches = pitches(two, numEights);
      System.out.println(numEights);
      System.out.println(firstPitches[0]);
      List<SimpleNote> childNotes = new ArrayList<>();
      List<Integer> breakPointers = mergedBreakPoints(one, two, random);
      if (!breakPointers.contains(numEights)) {
        breakPointers.add(numEights);
      }
      System.out.println(breakPointers);
      for (int i = 0; i < breakPointers.size() - 1; i++) {
        List<Integer> pitchChoices = new ArrayList<>();
        for (int j = breakPointers.get(i); j < breakPointers.get(i+1); j++) {
          pitchChoices.add(firstPitches[j]);
          pitchChoices.add(secondPitches[j]);
        }
        childNotes.add(
            new SimpleNote(
                pitchChoices.get(random.nextInt(pitchChoices.size())),
                (breakPointers.get(i+1) - breakPointers.get(i)) * EIGHTH_NOTE));
      }
      System.out.println(childNotes);
      return childNotes;
    }

    private List<Integer> mergedBreakPoints(
        List<SimpleNote> notes1, List<SimpleNote> notes2, Random random) {
      Set<Integer> breaks1 = breakPoints(notes1);
      Set<Integer> breaks2 = breakPoints(notes2);
      List<Integer> finalBreaks = new ArrayList<>();
      for (int point : breaks1) {
        if (breaks2.contains(point) || random.nextBoolean()) {
          finalBreaks.add(point);
        }
      }
      for (int point : breaks2) {
        if (!breaks1.contains(point) && random.nextBoolean()) {
          finalBreaks.add(point);
        }
      }
      Collections.sort(finalBreaks);
      return finalBreaks;
    }

    private Set<Integer> breakPoints(List<SimpleNote> notes) {
      Set<Integer> breaks = new HashSet<>();
      breaks.add(0);
      int progress = 0;
      for (SimpleNote note : notes) {
        progress += numEights(note.length);
        breaks.add(progress);
      }
      return breaks;
    }

    private int[] pitches(List<SimpleNote> notes, int arrayLength) {
      int[] array = new int[arrayLength];
      int progress = 0;
      for (SimpleNote note : notes) {
        int bound = progress + numEights(note.length);
        for (; progress < bound; progress++) {
          array[progress] = note.pitch;
        }
      }
      for (int i = progress; i < array.length; i++) {
        array[i] = REST;
      }
      return array;
    }

    private int numEights(double length) {
      return (int) ((length + 0.01) / EIGHTH_NOTE);
    }

    @Override
    List<SimpleNote> present() {
      List<SimpleNote> notes = new ArrayList<>();
      notes.addAll(swap ? two : one);
      notes.addAll(swap ? one : two);
      return notes;
    }

    private double length(List<SimpleNote> notes) {
      double total = 0;
      for (SimpleNote note : notes) {
        total += note.length;
      }
      return total;
    }
  }

  private static class TransformChromosome extends Chromosome<Allele, Transforms.Transform> {
    private final Locus locus;

    TransformChromosome(Locus locus, Allele one, Allele two) {
      super(one, two);
      this.locus = checkNotNull(locus);
    }

    @Override
    Allele meiosis(Random random) {
      return random.nextDouble() < 0.5 ? one : two;
    }

    @Override
    Transforms.Transform present() {
      return locus.getExpression(one, two);
    }
  }

  public String toString() {
    return melodies.one.toString() + " " + melodies.two.toString();
  }
}
