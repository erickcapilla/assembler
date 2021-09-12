import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

public class Files {
  private String fileIn, fileOut;
  private ArrayList<String> lines;
  private ArrayList<String[]> matrix;

  public Files(String fileIn, String fileOut) {
    this.fileIn = fileIn;
    this.fileOut = fileOut;
  }

  public void readFile() {
    String line;

    try {
      FileReader file = new FileReader(this.fileIn);
      BufferedReader reader = new BufferedReader(file);

      while ((line = reader.readLine()) != null) this.lines.add(line); //Guarda cada linea del archivo en un ArrayList

      reader.close();
    } catch (Exception e) { e.printStackTrace(); }
  }

  public void writeFile() {
    try {
      FileWriter file = new FileWriter(this.fileOut);
      PrintWriter writer = new PrintWriter(file);

      this.matrix.forEach(t -> writer.println(Arrays.toString(t) + ". Hay " + t.length + " tokens"));
      file.close();
    } catch (Exception e) { e.printStackTrace(); }
  }

  public ArrayList<String> getLines() {
    return lines;
  }

  public ArrayList<String[]> getMatrix() {
    return matrix;
  }

  public void setMatrix(ArrayList<String[]> matrix) {
    this.matrix = matrix;
  }
}
