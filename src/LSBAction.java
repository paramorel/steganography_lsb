import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class LSBAction {
    byte[] txtToByte(String input){
        byte [] arr = input.getBytes(StandardCharsets.UTF_8);
        return arr;
    }

    BufferedImage receiveContainer() throws IOException {
        File file = new File("2.png");
        BufferedImage container = ImageIO.read(file);
        return container;
    }

    public void hideTextInsideContainer(BufferedImage container, byte[] text) throws IOException {
        int x = 0;//  координаты левого верхнего пикселя (0, 0)
        int y = 0;
        int height = container.getHeight();
        int weight = container.getWidth();
        System.out.println("height: " + height);

        for (byte b : text){//8 бит текста. Проходим по всем байтам
            for (int binaryBit = 7; binaryBit >= 0; binaryBit--){//проходим по разрядам
                if (y >= height){
                    x++;
                    y = 0;
                    continue;
                }

                Color color = new Color(container.getRGB(x, y));
                int red = color.getRed();
                int green = color.getGreen();
                byte blue = (byte)color.getBlue();//пишем в синий, т.к он последний

                int bitOfText = (b >> binaryBit) & 1;//двигаем байты:
                //например, самая первая итерация такова: берем первый бит текста и двигаем его в конец байта
                blue = (byte)((blue & 0xFE) | bitOfText);//заменили(сложили) последний бит битового представления синего
//                System.out.println("New Blue : " + color.getBlue());
//                System.out.println(x + " " + y);

                Color newColor = new Color(red, green, (blue & 0xFF));
                container.setRGB(x, y, newColor.getRGB());
                y++;
            }
//            System.out.println(x + " " + y + ":" + Integer.toBinaryString(b & 255 | 256).substring(1));
        }

        System.out.println("Text hidden successfully");
        createNewContainerFile(container);
        System.out.println("Extract text from container (Y or N)?");
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        var extractText = extractText(text.length);
        String aaa = new String(extractText, StandardCharsets.UTF_8);
        var writer  = new OutputStreamWriter(new FileOutputStream("output.txt"), StandardCharsets.UTF_8);
        var writer2 = new FileOutputStream("output.txt");
//        byte[] buffer = extractText.getBytes(StandardCharsets.UTF_8);
//        System.out.println(buffer.length);
//        writer.write(aaa, 0, aaa.length());
//        writer.write(buffer, 0, buffer.length);
//        writer.close();
        writer2.write(extractText);
        writer2.close();

//        switch (in.readLine().trim()){
//            case "Y":
//            case "y":{
//                String extractText = extractText(text.length);
//                FileOutputStream writer  = new FileOutputStream("output.txt");
//                byte[] buffer = extractText.getBytes(StandardCharsets.UTF_8);
//                writer.write(buffer, 0, buffer.length);
//                //System.out.println("Extract text is: " + extractText);
//            }
//            default:
//                System.out.println("________END________");
//
//        }
    }

    byte[] extractText(int textLength) throws IOException {
        BufferedImage container = receiveNewContainer();
        byte[] messageBytes = extractBytes(container, textLength);
        if (messageBytes == null)
            return null;
        String message = new String(messageBytes, StandardCharsets.UTF_8);
        System.out.println(Arrays.toString(messageBytes));
        //System.out.println(message);
        return messageBytes;
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
        int height = container.getHeight();

        for(int l = 0; l < textLength; l++){
            for(int bit = 0 ; bit < 8 ; bit++){//1 символ - 8 бит
                if (y >= height){
                    x++;
                    y = 0;
                    continue;
                }
                Color color = new Color(container.getRGB(x, y));
                byte blue = (byte)color.getBlue();
//                System.out.println("Blue : " + blue);
                hiddenBytes[l] = (byte) ((hiddenBytes[l] << 1)|(blue & 1));

//                System.out.println(x + " " + y);
                y++;
            }
//            System.out.println(x + " " + y + ":" + Integer.toBinaryString(hiddenBytes[l] & 255 | 256).substring(1));
        }
//        for (byte b : hiddenBytes) {
//            System.out.println(Integer.toBinaryString(b & 255 | 256).substring(1));
//        }
        var a = hiddenBytes.toString();
        var enother = a.getBytes(StandardCharsets.UTF_8);
        System.out.println(enother.toString());
        return hiddenBytes;
    }

}
