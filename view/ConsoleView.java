package view;

import main.Controller;
import models.Model;
import models.Monster;
import view.View;

import java.util.Scanner;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public class ConsoleView implements View {
    
    private Scanner keyboard;
    private List<Monster> data;
    private Map<String, Runnable> options;
    private boolean done;
    
    public ConsoleView() {
        keyboard = new Scanner(System.in);
    }
    
    private Runnable displayOption = new Runnable() {
        public void run() {
            display(data);
        }
    };
    
    private Runnable breed = new Runnable() {
        public void run() {
            outl("Enter the indices of the parents.");
            int i = getInt();
            int j = getInt();
            
            Controller.breed(data.get(i), data.get(j));
        }
    };
    
    private int getInt() {
        while (true) {
            String input = keyboard.next();
            try {
                int i = Integer.parseInt(input);
                return i;
            } catch (NumberFormatException e) {
                outl("Try again.");
            }
        }
    }
    
    private Runnable quit = new Runnable() {
        public void run() {
            outl("ending program.");
            done = true;
            Controller.end();
        }
    };
    
    private Runnable help = new Runnable() {
        public void run() {
            outl("Here are your options:");
            for (String option : options.keySet()) {
                outl(option);
            }
        }
    };
    
    public void start() {
        outl("starting program.");
        options = new HashMap<>();
        options.put("display", displayOption);
        options.put("breed", breed);
        options.put("help", help);
        options.put("quit", quit);
        
        while(!done) {
            String input = getInput("What would you like to do?");
            if (options.containsKey(input)) {
                options.get(input).run();
            } else {
                outl("That is not an option.  Type \"help\" for options.");
            }
        }
    }
    
    private String getInput(String prompt) {
        outl(prompt);
        return keyboard.next();
    }
    
    public void display(Monster monster) {
        
        for (Model.Note note : monster.getMelody()) {
            out(note.toString() + " ");
        }
        
        outl("");
        
    }
    
    public void display(List<Monster> monsters){
        for (Monster monster : monsters) {
            display(monster);
        }
    }
    
    public void setData(List<Monster> monsters){
        this.data = monsters;
    }
    
    public void add(Monster monster){
        this.data.add(monster);
        outl("you added: ");
        display(monster);
    }
    
    private static void out(String output) {
        System.out.print(output);
    }
    private static void outl(String output) {
        System.out.println(output);
    }
}
