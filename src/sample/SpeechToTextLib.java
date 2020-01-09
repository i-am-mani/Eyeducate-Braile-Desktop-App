package sample;


import org.json.JSONObject;

import javax.sound.sampled.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;

public class SpeechToTextLib {

    public static final String BEARER_KEY = "Bearer <<Add your wit.ai key>>";
    final String fileName = "record.wav";
    boolean shiftPressed = false;
    static int temp;

    public static void main(String[] args) {
        SpeechToTextLib s1 = new SpeechToTextLib();
        try {
            s1.sendRequest();
            speak("record.wav");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String ConvertToText() {


        try {
//            AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 4410, 16, 2, 4, 44100, false);
            AudioFormat format = new AudioFormat(44100, 16, 2, true, true);
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);//4410

            Mixer.Info[] MixerInfo = AudioSystem.getMixerInfo();

            if (!AudioSystem.isLineSupported(info)) {
                System.err.println("line not supported");
            }

            final TargetDataLine targetLine = (TargetDataLine) AudioSystem.getLine(info);


            targetLine.open();
            System.out.println("Starting Recodring...");
            targetLine.start();
            for (Mixer.Info i : MixerInfo) {

                if (i.toString().contains("Speakers")) {
                    AudioSystem.getMixer(i).open();
                }
//                System.out.println(i.toString() + AudioSystem.getMixer(i).isOpen());

            }
            ;
            Thread thread = new Thread(() -> {
                AudioInputStream audioStream = new AudioInputStream(targetLine);
                File audioFile = new File(fileName);

                try {
                    AudioSystem.write(audioStream, AudioFileFormat.Type.WAVE, audioFile);


                } catch (Exception e) {

                    e.printStackTrace();
                }
                System.out.println("Stopping recording");
            });
            thread.start();
            while (shiftPressed) {
                Thread.sleep(10);
            }

            targetLine.stop();
            targetLine.close();
            return "1";

        } catch (Exception e) {
            System.out.println("An error occurred");
        }
        return "0";

    }


    public String sendRequest() throws IOException {
        String requestURL = "https://api.wit.ai/speech?v=20160526";
        URL url = new URL(requestURL);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setUseCaches(false);
        httpConn.setDoOutput(true); // indicates POST method
        httpConn.setDoInput(true);

        httpConn.setRequestMethod("POST");
        httpConn.setRequestProperty("Connection", "Keep-Alive");
        httpConn.setRequestProperty("Cache-Control", "no-cache");
        httpConn.setRequestProperty("Authorization", BEARER_KEY);

        httpConn.setRequestProperty("Content-Type", "audio/wav");
        ;
        File waveFile = new File(fileName);
        byte[] bytes = Files.readAllBytes(waveFile.toPath());
        DataOutputStream request = new DataOutputStream(httpConn.getOutputStream());
        request.write(bytes);
        request.flush();
        request.close();

        String response = "";
// checks server's status code first
        int status = httpConn.getResponseCode();
        if (status == HttpURLConnection.HTTP_OK) {

            InputStream responseStream = new BufferedInputStream(httpConn.getInputStream());

            BufferedReader responseStreamReader
                    = new BufferedReader(new InputStreamReader(responseStream));

            String line = "";
            StringBuilder stringBuilder = new StringBuilder();

            while ((line = responseStreamReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            responseStreamReader.close();

            response = stringBuilder.toString();
            httpConn.disconnect();
            return extractContent(response);
        } else {

            throw new IOException("Server returned non-OK status: " + status);

        }
    }

    private String extractContent(String text) {
        JSONObject json = new JSONObject(text);
        String jsonArray = json.getString("_text");
        return jsonArray;
    }

    public static void speak(String SAY) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DataLine.Info dataInfo = new DataLine.Info(Clip.class, null);
                Mixer.Info[] mixerArray = AudioSystem.getMixerInfo();
                Mixer mixer = AudioSystem.getMixer(mixerArray[0]);
                Clip clip;
                try {
                    clip = ((Clip) mixer.getLine(dataInfo));
//                    System.out.println("Inside 1 "+new File(getClass().getResource("/sample/res/closeApplication.WAV").getPath()).getAbsoluteFile().length());
//                    AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(getClass().getResource("/res/srcFileNotSelected.WAV").toURI()));//new File("src/sample.res/"+SAY).getAbsoluteFile()
//                    temp= (int) new File(getClass().getResource("/sample/res/" +SAY).toURI()).length()/1000;
                    System.out.println(SAY + ".WAV");
                    AudioInputStream audioStream = AudioSystem.getAudioInputStream(getClass().getResource("/sample/res/" + SAY + ".WAV").toURI().toURL());

                    clip.open(audioStream);
                    clip.start();
                    do {
                        try {
                            Thread.sleep(1);
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("Error in Thread");
                        }

                    } while (clip.isRunning());
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Error Occured in Running");
                }
            }
        }).start();

    }

}





