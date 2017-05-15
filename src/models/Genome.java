package models;

import static utils.Utils.checkNotNull;

import models.Model.SimpleNote;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/** Stores the DNA of a single monster. */
class Genome implements Serializable { //fixme test
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
              locus, one.transformAleles.get(locus), two.transformAleles.get(locus)));
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

  private static class PatternChromosome extends Chromosome<List<SimpleNote>, List<SimpleNote>> {
    private final boolean swap;

    PatternChromosome(List<SimpleNote> one, List<SimpleNote> two, Random random) {
      super(one, two);
      swap = false; //random.nextDouble() < 0.5; //fixme
    }

    @Override
    List<SimpleNote> meiosis(Random random) { // TODO: recombination
      return one;
    }

    @Override
    List<SimpleNote> present() {
      List<SimpleNote> notes = new ArrayList<>();
      List<SimpleNote> first = swap ? two : one;
      List<SimpleNote> second = swap ? one : two;
      notes.addAll(first);
      notes.addAll(second);
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

  public String toString() {
    return melodies.one.toString() + " " + melodies.two.toString();
  }
}
