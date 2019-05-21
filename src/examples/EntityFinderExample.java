package examples;

import java.io.IOException;
import java.util.InvalidPropertiesFormatException;

import entity.Entity;
import entity.EntityFinder;

public class EntityFinderExample {

  public static void main(String[] args) {

    EntityFinder finder = null;
    try {
      finder = new EntityFinder();
    }
    catch (InvalidPropertiesFormatException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    String[] tokens = {"O", "rato", "roeu", "a", "rolha", "de", "a", "garrafa",
        "de", "rum", "de", "o", "Rei_da_Rússia", "."};

    Entity[] entities = finder.find(tokens);
    System.out.println(finder.annotate(tokens));
    for (Entity entity : entities) {
      System.out.println(entity);
    }
  }
}
