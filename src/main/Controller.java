package main;

import models.Model;
import models.Monster;
import music.JMusicOfflineService;
import music.JMusicRealTimeService;
import music.MusicService;
import storage.FileStorage;
import storage.StorageService;
import view.View;
import view.WindowView;

import java.io.File;
import java.util.*;
import javax.swing.JFileChooser;

public class Controller {

  private static final View view = new WindowView();
  private static StorageService storageService;
  private static MusicService musicService;

  public static void main(String[] args) {
    storageService = new FileStorage(getFile());
    List<Monster> monsters = storageService.loadMonsters();
    Model.setCreatures(monsters);
    musicService = args.length == 0
        ? new JMusicOfflineService()
        : new JMusicRealTimeService();
    view.setData(monsters);
    view.start();
  }

  private static File getFile() { // TODO move picking to View's responsibility
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
    int result = fileChooser.showOpenDialog(null);
    if (result == JFileChooser.APPROVE_OPTION) {
      File selectedFile = fileChooser.getSelectedFile();
      System.out.println("Selected file: " + selectedFile.getAbsolutePath());
      return selectedFile;
    }
    return null;
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
    storageService.save(Model.creatures());
  }

  public static void breed(Monster parent1, Monster parent2) {
    Monster child = Monster.breed(parent1, parent2);
    Model.addCreature(child);
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