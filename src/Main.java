import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    ArrayList<String[]> tokens;
    ArrayList<String> busyMemory = new ArrayList<>();
    String op;

    do {
      System.out.print("Ruta/archivo: ");
      String fileIn = sc.nextLine();

      System.out.print("Nombre del archivo/salida (sin extencion): ");
      String fileOut = sc.nextLine();

      Tokens token = new Tokens(fileIn, fileOut);

      tokens = token.getTokens();

      Tables tables = new Tables(tokens, fileIn,fileOut);

      System.out.print("Ejecutar Cargador [Y/N]: ");
      String option = sc.nextLine();

      boolean load = false;
      do {
        boolean exe = true;
        if(option.equalsIgnoreCase("y")) {
          System.out.print("Memoria libre: ");
          String position = sc.nextLine();
          if(position.length() < 2) position = "0" + position;
          if(noAvailable(position, busyMemory)) {
            System.out.println("Memoria ocupada por el SO, Memoria disponible a partir de: " +
                    busyMemory.get(busyMemory.size() - 1));
            exe = false;
            load = true;
          }

          if(exe) {
            Loader loader = new Loader(tables.getMemoryPositions(), position, fileOut);

            load = false;
          }
        }
      } while (load);

      System.out.print("¿Volver a ejecutar programa? [Y/N]: ");
      op = sc.nextLine();
    } while (op.equalsIgnoreCase("y"));

  }

  public static boolean noAvailable(String position, ArrayList<String> busyMemory) {
    final String[] hex = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "A", "B", "C", "D", "E", "F"};

    int i = 0, j = 0, count = 0;
    boolean res = false;

    for(int k = 0; k < 20; k++) {
      busyMemory.add(hex[i]+hex[j]);
      if(position.equalsIgnoreCase(hex[i]+hex[j])) count++;
      j++;
      if(j > 15) {i++; j = 0;}
    }
    busyMemory.add(hex[i]+hex[j]);

    if(count != 0) res = true;

    return res;
  }
}
