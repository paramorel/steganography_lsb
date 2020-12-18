import java.awt.image.BufferedImage;
import java.io.*;

public class LSB {
    public static void main(String[] args) throws IOException {
        FileReader text = new FileReader("input.txt");
        BufferedReader input = new BufferedReader(text);

        String string = "";
        String plainText = "";

        while((string=input.readLine())!=null)
            plainText += string;

        Converter converter = new Converter();
        LSBAction action = new LSBAction();
        byte [] txtBytes = converter.txtToByte(plainText);
        System.out.println("text size: " + txtBytes.length);
        BufferedImage container = action.receiveContainer();
        action.hideTextInsideContainer(container, txtBytes);    }
}
