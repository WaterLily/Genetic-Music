package main;

import models.Model;
import models.Monster;
import storage.RandomStorage;
import storage.StorageService;
import view.ConsoleView;
import view.View;

import java.util.List;

public class Controller {
    
    private static final View view = new ConsoleView();
    private static final StorageService storageService = new RandomStorage();

     public static void main(String[] args){
         // get existing models
         // initialize view with them
         
         testConcepts();
     }
     
     public static void end() {
         System.out.println("Type Ctrl-C to quit.");
     }
     
     public static void breed(Monster parent1, Monster parent2) {
         Monster child = Model.breed(parent1, parent2);
         view.add(child);
     }
     
     
     private static void testConcepts() {
         List<Monster> monsters = storageService.loadMonsters();
         view.setData(monsters);
         view.start();
     }

}