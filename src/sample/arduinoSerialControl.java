package sample;

import com.fazecast.jSerialComm.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class arduinoSerialControl {

    public static void main(String[] args) throws IOException, InterruptedException {
        SerialPort[] sp = SerialPort.getCommPorts();

        SerialPort comPort = SerialPort.getCommPorts()[0];
        comPort.openPort();
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 100, 0);
        InputStream inputStreamForSerial = comPort.getInputStream();
        OutputStream outputStreamForSerial = comPort.getOutputStream();

        outputStreamForSerial.write("c".getBytes());
        System.out.println("running c");
        Thread.sleep(3000);
        outputStreamForSerial.write("e".getBytes());
        System.out.println("running e");
        Thread.sleep(3000);
        outputStreamForSerial.write("g".getBytes());
        System.out.println("running g");
        Thread.sleep(3000);
        outputStreamForSerial.write("l".getBytes());
        System.out.println("running l");
    }
}
