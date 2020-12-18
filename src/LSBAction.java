import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class LSBAction {
    BufferedImage receiveContainer() throws IOException {
        File file = new File("2.png");
        BufferedImage container = ImageIO.read(file);
        return container;
    }

    public void hideTextInsideContainer(BufferedImage container, byte[] text) throws IOException {
        int x = 0;//  координаты левого верхнего пикселя (0, 0)
        int y = 0;

        for (byte b : text){//8 бит текста
            for (int binaryBit = 7; binaryBit >= 0; binaryBit--){
                Color color = new Color(container.getRGB(x, y));
                int red = color.getRed();
                int green = color.getGreen();
                byte blue = (byte)color.getBlue();//пишем в синий, т. к последний
                System.out.println("Blue : " + color.getBlue());
                System.out.println("Red Green Blue : " + red + " "+ green + " "+blue);
                System.out.println("Sum : "+ (red+green+(int)blue));

                int bitOfText = (b >> binaryBit) & 1;
                blue = (byte)((blue & 0xFE) | bitOfText);//заменили последний бит битового представления синего
                System.out.println("New Blue: " + (int)(blue & 0xFF) + " " + blue);
                System.out.println(x + " " + y);

                Color newColor = new Color(red, green, (blue & 0xFF));
                container.setRGB(x, y, newColor.getRGB());
                x++;
            }
            y++;
        }

        System.out.println("Text hidden successfully");
        createNewContainerFile(container);
        System.out.println("Extract text from container (Y or N)?");
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        switch (in.readLine().trim()){
            case "Y":
            case "y":{
                String extractText = extractText(text.length);
                FileOutputStream writer  = new FileOutputStream("output.txt");
                byte[] buffer = extractText.getBytes(StandardCharsets.UTF_8);
                writer.write(buffer, 0, buffer.length);
                System.out.println("Extract text is: " + extractText);
            }
            default:
                System.out.println("________END________");

        }
    }

    String extractText(int textLength) throws IOException {
        BufferedImage container = receiveNewContainer();
        byte[] msgBytes = extractBytes(container, textLength);
        if (msgBytes == null)
            return null;
        String message = new String(msgBytes);
        System.out.println(Arrays.toString(msgBytes));
        return message;
    }

    void createNewContainerFile(BufferedImage container) throws IOException {
        File file = new File("newContainer.png");
        ImageIO.write(container, "png", file);
    }

    BufferedImage receiveNewContainer() throws IOException {
        File file = new File("newContainer.png");
        BufferedImage container = ImageIO.read(file);
        return container;
    }

    byte[] extractBytes(BufferedImage container, int textLength){
        int y = 0;
        int x = 0;
        byte [] hiddenBytes = new byte[textLength];

        for(int l = 0; l < textLength; l++){
            for(int bit = 0 ; bit < 8 ; bit++){//1 символ - 8 бит
                Color color = new Color(container.getRGB(x, y));
                int red = color.getRed();
                int green = color.getGreen();
                byte blue = (byte)color.getBlue();
                System.out.println("Blue : " + blue);
                //System.out.println("Hidden byte<<1: "+(hiddenBytes[l]<<1));
                //System.out.println("blue&1 : "+(blue&1));
                System.out.println("(hiddenBytes[l]<<1)|(blue&1) : "+ ((hiddenBytes[l]<<1)|(blue&1)));
                hiddenBytes[l] = (byte) ((hiddenBytes[l] << 1)|(blue & 1));
                System.out.println("Hidden byte" + l + " : " + hiddenBytes[l]);
                System.out.println(x+" "+y);
                x++;
            }
            y++;
        }
        return hiddenBytes;
    }

}
