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
  private static MusicService musicService;

  public static void main(String[] args) {
    List<Monster> monsters = storageService.loadMonsters();
    musicService = args.length == 0
        ? new JMusicOfflineService()
        : new JMusicRealTimeService();
    view.setData(monsters);
    view.start();
  }

  private static Set<Model.Melody> getSongs(Collection<Monster> singers) {
    Set<Model.Melody> songs = new HashSet<>(singers.size());
    for (Monster singer : singers) {
      songs.add(singer.getMelody());
    }
    return songs;
  }

  public static void end() {
    musicService.pause();
  }

  public static void breed(Monster parent1, Monster parent2) {
    Monster child = Model.breed(parent1, parent2);
    view.add(child);
  }

  public static void play(Set<Monster> singers, Set<String> activeSingers) {
    Set<Model.Melody> melodies = getSongs(singers);
    musicService.play(melodies, activeSingers);
  }

  public static void pause() {
    musicService.pause();
  }

  public static void activateSinger(Monster singer) {
    System.out.println("activating " + singer.getName());
    musicService.addPart(singer.getMelody());
  }

  public static void deactivateSinger(Monster singer) {
    System.out.println("de-activating " + singer.getName());
    musicService.removePart(singer.getMelody());
  }

//  public
}