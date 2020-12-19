import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
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
        System.out.println(plainText.length() + ":" + plainText);


        //ввод с клавиатуры:
        //Scanner scanner = new Scanner(System.in);
        //String plainText = scanner.nextLine();

        LSBAction action = new LSBAction();
        byte [] txtBytes = action.txtToByte(plainText);
        System.out.println(new String(txtBytes, StandardCharsets.UTF_8));
        System.out.println(Arrays.toString(txtBytes));
//        for (byte b : txtBytes) {
//            System.out.println(Integer.toBinaryString(b & 255 | 256).substring(1));
//        }
        System.out.println("text size: " + plainText.length());
        BufferedImage container = action.receiveContainer();
        action.hideTextInsideContainer(container, txtBytes);
    }
}
