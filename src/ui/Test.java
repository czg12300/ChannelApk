package ui;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Test {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        try {
            List<String> list=findJar("F:/apkchannel/class.txt");
            for(String str:list){
                System.out.println(str);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
public static List<String> findJar(String textPath) throws IOException{
    List<String> list=new ArrayList<>();
    List<String> temp=  Files.readAllLines(Paths.get(textPath),Charset.forName("GBK"));
    for(String s:temp){
        if(s.contains("apkchannel")&&!list.contains(s.split("apkchannel")[1])){
            list.add(s.split("apkchannel")[1]);
        }
    }
    
    return list;
    
}
}
