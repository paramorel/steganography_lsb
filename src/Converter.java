import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Converter {
    byte[] txtToByte(String input){
        byte [] arr = input.getBytes(StandardCharsets.UTF_8);
        return arr;
    }


}
