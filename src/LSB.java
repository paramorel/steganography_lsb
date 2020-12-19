import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class LSB {
    public static void main(String[] args) throws IOException {
        FileReader text = new FileReader("input.txt");
        BufferedReader input = new BufferedReader(text);

        StringBuilder content = new StringBuilder();
        String plainText = "";

        int value;
        while((value = input.read()) != -1){
            content.append((char) value);
        }

        plainText = content.toString();


        //ввод с клавиатуры:
        //Scanner scanner = new Scanner(System.in);
        //String plainText = scanner.nextLine();

        LSBAction action = new LSBAction();
        byte [] txtBytes = action.txtToByte(plainText);
        System.out.println("text size: " + txtBytes.length);
        BufferedImage container = action.receiveContainer();
        action.hideTextInsideContainer(container, txtBytes);
    }
}
