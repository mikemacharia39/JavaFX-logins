package tests;
 
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;

import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
 
public class JavaFXColorPicker extends Application {
 
    MyRxTx myRxTx;
 
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("ColorPicker");
        final Scene scene = new Scene(new HBox(20), 400, 100);
        HBox box = (HBox) scene.getRoot();
        box.setPadding(new Insets(5, 5, 5, 5));
 
        final ColorPicker colorPicker = new ColorPicker();
        //colorPicker.setValue(Color.CORAL);
 
        colorPicker.setOnAction(new EventHandler() {
            public void handle(Event t) {
                scene.setFill(colorPicker.getValue());
                 
                Color color = colorPicker.getValue();
                byte byteR = (byte)(color.getRed() * 255);
                byte byteG = (byte)(color.getGreen() * 255);
                byte byteB = (byte)(color.getBlue() * 255);
                 
                try {
                    byte[] bytesToSent = 
                            new byte[]{byteB, byteG, byteR};
                    myRxTx.output.write(bytesToSent);
                     
                } catch (IOException ex) {
                    System.out.println(ex.toString());
                }
            }
        });
 
        box.getChildren().add(colorPicker);
 
        primaryStage.setScene(scene);
        primaryStage.show();
         
        myRxTx = new MyRxTx();
        myRxTx.initialize();
    }
 
    public static void main(String[] args) {
        launch(args);
    }
 
    class MyRxTx implements SerialPortEventListener {
 
        SerialPort serialPort;
        /**
         * The port we're normally going to use.
         */
        private final String PORT_NAMES[] = {
            "/dev/ttyACM0", //for Ubuntu
            "/dev/tty.usbserial-A9007UX1", // Mac OS X
            "/dev/ttyUSB0", // Linux
            "COM3", // Windows
        };
        private BufferedReader input;
        private OutputStream output;
        private static final int TIME_OUT = 2000;
        private static final int DATA_RATE = 9600;
 
        public void initialize() {
            CommPortIdentifier portId = null;
            Enumeration<?> portEnum = CommPortIdentifier.getPortIdentifiers();
 
            //First, Find an instance of serial port as set in PORT_NAMES.
            while (portEnum.hasMoreElements()) {
                CommPortIdentifier currPortId
                        = (CommPortIdentifier) portEnum.nextElement();
                for (String portName : PORT_NAMES) {
                    if (currPortId.getName().equals(portName)) {
                        portId = currPortId;
                        break;
                    }
                }
            }
            if (portId == null) {
                System.out.println("Could not find COM port.");
                return;
            } else {
                System.out.println("Port Name: " + portId.getName() + "\n"
                        + "Current Owner: " + portId.getCurrentOwner() + "\n"
                        + "Port Type: " + portId.getPortType());
            }
 
            try {
                // open serial port, and use class name for the appName.
                serialPort = (SerialPort) portId.open(this.getClass().getName(),
                        TIME_OUT);
 
                // set port parameters
                serialPort.setSerialPortParams(DATA_RATE,
                        SerialPort.DATABITS_8,
                        SerialPort.STOPBITS_1,
                        SerialPort.PARITY_NONE);
 
                // open the streams
                input = new BufferedReader(
                        new InputStreamReader(serialPort.getInputStream()));
                output = serialPort.getOutputStream();
 
                // add event listeners
                serialPort.addEventListener(this);
                serialPort.notifyOnDataAvailable(true);
            } catch (Exception e) {
                System.err.println(e.toString());
            }
        }
 
        @Override
        public void serialEvent(SerialPortEvent spe) {
            if (spe.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
                try {
                    final String inputLine = input.readLine();
                    System.out.println(inputLine);
                } catch (Exception e) {
                    System.err.println(e.toString());
                }
            }
        }
 
        /**
         * This should be called when you stop using the port. This will prevent
         * port locking on platforms like Linux.
         */
        public synchronized void close() {
            if (serialPort != null) {
                serialPort.removeEventListener();
                serialPort.close();
            }
        }
    }
 
}