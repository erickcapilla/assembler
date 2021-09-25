import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

public class Loader {
  ArrayList<String[]> memory;
  String positionFree;
  ArrayList<String[]> newMemory = new ArrayList<>();
  private String fileOut;
  private final String[] hex = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
          "A", "B", "C", "D", "E", "F"};

  public Loader(ArrayList<String[]> memory, String position, String fileOut) {
    this.memory = memory;
    this.positionFree = position;
    this.fileOut = fileOut;

    this.reMemory(position);
  }

  public void reMemory(String position) {
    ArrayList<String> hexa = new ArrayList<>(Arrays.asList(this.hex));
    int index1 = hexa.indexOf(String.valueOf(position.toUpperCase().charAt(0)));
    int index2 = hexa.indexOf(String.valueOf(position.toUpperCase().charAt(1)));

    for(int i = 0; i < this.memory.size(); i++) {
      String[] mem = { "", this.memory.get(i)[1] };
      this.newMemory.add(mem);
      this.newMemory.get(i)[0] = String.valueOf(position).toUpperCase();
      index2++;
      if(index1 >= 15 && index2 > 15) {
        this.jumps();
        this.newMemory.forEach(m -> System.out.println(Arrays.toString(m)));

        if(this.memory.size() == this.newMemory.size()) writeFile();
         else System.out.println("Memoria Insuficiente");

        System.exit(0);
      }
      if(index2 > 15) {index1++; index2 = 0;}
      if(index1 > 15) index1 = 0;
      position = generateMemory(index1, index2);
    }

    this.jumps();
    this.newMemory.forEach(m -> System.out.println(Arrays.toString(m)));
    writeFile();
  }

  public String generateMemory(int index1, int index2) {
    String num = this.hex[index1] + this.hex[index2];
    return num;
  }

  public void jumps() {
    for(int i = 0; i < this.memory.size(); i++) {
      if(this.memory.get(i)[2].contains("*")) {
        String content = this.memory.get(i+1)[1];
        this.newMemory.get(i+1)[1] = addHex(content);
      }
    }
  }

  public String addHex(String content) {
    ArrayList<String> hexa = new ArrayList<>(Arrays.asList(hex));
    int aux = hexa.indexOf(String.valueOf(this.positionFree.toUpperCase().charAt(0)));
    int aux2 = hexa.indexOf(String.valueOf(this.positionFree.toUpperCase().charAt(1)));
    int first = hexa.indexOf(String.valueOf(content.toUpperCase().charAt(0)));
    int second = hexa.indexOf(String.valueOf(content.toUpperCase().charAt(1)));
    String newHex = "";

    for (int i = 0; i < aux; i++) {
      first++;
    }
    for (int i = 0; i < aux2; i++) {
      second++;
      if(second >= 16) {first++; second = 0;}
    }
    if(first > 16)
      System.out.println("Memoria insuficiente");
    else
    newHex = hexa.get(first)+hexa.get(second);

    return newHex;
  }

  public void writeFile() {
    try {
      FileWriter file = new FileWriter(this.fileOut + ".rlz");
      PrintWriter writer = new PrintWriter(file);
      writer.println("[Memoria, Codigos]");

      this.newMemory.forEach(m -> writer.println(Arrays.toString(m)));

      file.close();
    } catch (Exception e) { e.printStackTrace(); }
  }
}
