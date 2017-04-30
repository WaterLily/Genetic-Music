package models;

import static utils.Utils.checkNotNull;

import models.Model.SimpleNote;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/** Stores the DNA of a single monster. */ //TODO: make serializable
class Genome { //fixme test
  private List<TransformChromosome> transformations;
  private PatternChromosome melodies;

  public Genome(Gamete one, Gamete two) {
    melodies = new PatternChromosome(one.melodyAlleles, two.melodyAlleles, new Random()); //fixme random
    transformations = new ArrayList<>();
    Set<Locus> loci = new HashSet<>();
    loci.addAll(one.transformAleles.keySet());
    loci.addAll(two.transformAleles.keySet());
    for (Locus locus : loci) { //TODO gracefully handle null alleles
      transformations.add(
          new TransformChromosome(
              locus, one.transformAleles.get(locus), one.transformAleles.get(locus)));
    }
  }

  Gamete getGamete(Random random) { //fixme test
    Gamete.Builder builder = Gamete.builder();
    for (TransformChromosome transformation : transformations) {
      builder.addAllele(transformation.locus, transformation.meiosis(random));
    }
    builder.setMelody(melodies.meiosis(random));
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

  private static abstract class Chromosome<E, A> { //fixme decide if this polymorphism is needed
    protected final E one;
    protected final E two;

    Chromosome(E one, E two) {
      this.one = one;
      this.two = two;
    }

    abstract E meiosis(Random random);
    abstract A present();
  }

  private static class PatternChromosome extends Chromosome<MelodyBase, List<SimpleNote>> {
    private final boolean swap;

    PatternChromosome(MelodyBase one, MelodyBase two, Random random) {
      super(one, two);
      swap = random.nextDouble() < 0.5;
    }

    @Override
    MelodyBase meiosis(Random random) {
      boolean r = random.nextDouble() < 0.5;
      MelodyBase first = r ? one : two;
      MelodyBase second = r ? two : one;
      MelodyBase fakeBase = new MelodyBase(new SimpleNote(-1, 8));
      addNotes(fakeBase, first, second, random);
      return fakeBase.next();
    }

    private void addNotes(MelodyBase base, MelodyBase current, MelodyBase other, Random random) {
      // TODO support differing lengths
      if (current == null) {
        return;
      } else {
        base.setNext(new MelodyBase(current.note));
      }
      boolean cross = random.nextDouble() < getCrossoverChance(current, other);
      MelodyBase first = cross ? other : current;
      MelodyBase second = cross ? current : other;
      addNotes(base.next(), first.next(), second.next(), random);
    }

    private double getCrossoverChance(MelodyBase one, MelodyBase two) {
      return 0.25;
    }

    @Override
    List<SimpleNote> present() {
      List<SimpleNote> notes = new ArrayList<>();
      MelodyBase first = swap ? one : two;
      MelodyBase second = swap ? two : one;
      for (MelodyBase base = first; base != null; base = base.next()) {
        notes.add(base.note);
      }
      for (MelodyBase base = second; base != null; base = base.next()) {
        notes.add(base.note);
      }
      return notes;
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

  static class MelodyBase { // TODO try to deprecate
    final SimpleNote note;
    private MelodyBase next;

    MelodyBase(SimpleNote note) {
      this.note = note;
    }

    MelodyBase setNext(MelodyBase next) {
      this.next = next;
      return this;
    }

    MelodyBase next() {
      return next;
    }

    public String toString() {
      String result = note.toString();
      if (next != null) {
        result += " " + next.toString();
      }
      return result;
    }
  }

  public String toString() {
    return melodies.one.toString() + " " + melodies.two.toString();
  }
}
