package sample;


import java.io.*;
import java.util.StringTokenizer;

public class SearchDictionary {


    public String search(String word) {
        StringBuilder sb = new StringBuilder();
        try {
            word = word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
            String startingLetter = word.substring(0, 1);
//            FileReader fileIn = new FileReader(getClass().getResource("/sample/res/DictionaryFiles/"+startingLetter+".txt").getPath());
//            FileReader fileIn = new FileReader((new File(".").getAbsolutePath()).replace(".","src/sample/res/DictionaryFiles/"+startingLetter+".txt"));
            BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/sample/res/DictionaryFiles/" + startingLetter + ".txt")));
            String line;
            StringTokenizer st;
            String stToken;
            boolean flag = false;
            while ((line = reader.readLine()) != null) {
                st = new StringTokenizer(line);
//                stToken = ;
                if (st.hasMoreElements() && (st.nextToken().equals(word))) {
                    System.out.println(line.substring(line.indexOf(")") + 1) + "\n");
//                    sb.append(line.substring(word.length() + 6)+"\n");
                    sb.append(line.substring(line.indexOf(")") + 1) + "\n");

                    flag = true;
                } else if (flag == true) {
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        return sb.toString();

    }

    public String searchSynonyms(String word) {
        FileReader fileIn = null;
        word = word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase(); //To convert into the from of keyvalues i.e starting letter is capital.
        StringBuilder sb = new StringBuilder();
        try {
//            fileIn = new FileReader((new File(".").getAbsolutePath()).replace(".","src/sample/res/DictionaryFiles/"+"SynAnt"+".txt"));
            BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/sample/res/DictionaryFiles/SynAnt.txt")));
            String line;
            StringTokenizer st;
            /*-->When the sentence starts with key, split it at ':' and tokenize the second part of the key
                 which has word we want to match with.upon next Token we would get the real Key value and
                 by checking it with our word, if are same we move on to appending entire text until it reaches
                 '=' to stringbuilder*/
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("KEY")) {
                    st = new StringTokenizer(line.split(":")[1]);
//                    System.out.println(line.split(":")[1]+(word+"."));
                    if ((word).equals(st.nextToken().replace(".", ""))) {
                        while ((line = reader.readLine()) != null && !line.equals("=")) {
                            if (line.startsWith("ANT")) {
                                sb.append("\n");
                            }
                            sb.append(line);
                        }

                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

}
