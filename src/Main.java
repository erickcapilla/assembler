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

    //token.writeMacros(token.getMacros());
  }
}
