import java.util.ArrayList;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    ArrayList<String[]> tokens;

    System.out.print("Ruta/archivo: ");
    String fileIn = sc.nextLine();

    System.out.print("Nombre del archivo/salida (sin extencion): ");
    String fileOut = sc.nextLine();

    Tokens token = new Tokens(fileIn, fileOut);

    tokens = token.getTokens();

    Tables tables = new Tables(tokens, fileIn,fileOut);

    System.out.print("Ejecutar Cargador [Y/N]: ");
    String option = sc.nextLine();

    if(option.equalsIgnoreCase("y")) {
      System.out.print("Memoria libre: ");
      String position = sc.nextLine();
      if(position.length() < 2) position = "0" + position;
      Loader loader = new Loader(tables.getMemoryPositions(), position, fileOut);
    } else {
      System.exit(0);
    }

  }
}
