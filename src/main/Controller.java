package main;

import models.Model;
import models.Monster;
import music.JMusicOfflineService;
import music.JMusicRealTimeService;
import music.MusicService;
import storage.RandomStorage;
import storage.StorageService;
import view.View;
import view.WindowView;

import java.util.*;

public class Controller {

  private static final View view = new WindowView();
  private static final StorageService storageService = new RandomStorage();
  private static final MusicService musicService = new JMusicRealTimeService();

  public static void main(String[] args) {
    List<Monster> monsters = storageService.loadMonsters();
    view.setData(monsters);
    view.start();
  }

  public static void end() {

  }

  public static void breed(Monster parent1, Monster parent2) {
    Monster child = Model.breed(parent1, parent2);
    view.add(child);
  }

  public static void play(Monster... singers) {
    play(Arrays.asList(singers));
  }

  public static void play(Collection<Monster> singers) {
    for (Monster singer : singers) {
      musicService.addPart(new Model.Melody(singer.toString(), singer.getMelody()));
    }
    musicService.play();
  }

  public static void pause() {
    musicService.pause();
  }

  public 
}