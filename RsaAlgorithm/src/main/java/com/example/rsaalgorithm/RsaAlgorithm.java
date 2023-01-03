package com.example.rsaalgorithm;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Base64;

/**
 * @author Abdusselam koç
 */

public class RsaAlgorithm extends Application {


    // this class keep tracks of keys and their names.
    public class KeyandName {
        String name;
        KeyGeneration key;

        public KeyandName(String name, KeyGeneration key) {
            this.name = name;
            this.key = key;
        }

    }

    // this list keeps the keys and the name of keys.
    public static ArrayList<KeyandName> listOfKeys = new ArrayList<KeyandName>();

    // This list keeps the dropdown items. dropdown shows the names of the keys.
    ObservableList<String> items = FXCollections.observableArrayList();
    // keeps the current key which will be used in the process.
    public static KeyGeneration CurrentKey = null;
    // keeps encrypted values list.
    public static ArrayList<BigInteger> EncryptedList = new ArrayList<>();

    // keeps the decrypted list.
    public static ArrayList<String> DecryptedList = new ArrayList<>();

    // shows the plain text in the end.
    public static String PlainText = "";


    // function to generate key whenever the user clicks the generate key button. it adds thee generated key to the list.
    public void GenerateKey() {
        KeyGeneration key = new KeyGeneration();
        int index = listOfKeys.size();
        KeyandName kk = new KeyandName("key" + index, key);
        listOfKeys.add(kk);
        items.add(kk.name);
        CurrentKey = kk.key;

    }


    // this function takes the plain text as an input and converts it to binary form with reqiured paddings.
    // it checks whether the length is odd or not. if it is odd it adds some extra zeros to the end.
    public static ArrayList<String> ConvertToBinaryString(String s) {

        ArrayList<String> list = new ArrayList<>();
        String str = "";
        for (int i = 0; i < s.length(); i++) {

            String binaryString = Integer.toBinaryString(s.charAt(i));
            if (binaryString.length() > 8) {
                throw new RuntimeException("you have used a special character");
            }
            while (binaryString.length() != 8) {
                binaryString = "0" + binaryString;
            }
            if (i % 2 == 1 || i == s.length() - 1) {
                str += binaryString;
                list.add(str);
                str = "";


            } else {
                str += binaryString;
            }
        }
        if (list.get(list.size() - 1).length() == 8) {
            String strs = list.get(list.size() - 1);
            strs += "00000000";
            list.remove(list.get(list.size() - 1));
            list.add(strs);
        }
        return list;

    }


    // this function Encrypts  given binary text.
    // it e,n and list of binary inputs.
    // after calculating the mode it adds into new bigInteger list and in the end returns it.

    public static ArrayList<BigInteger> Encrypt(BigInteger e, BigInteger n, ArrayList<String> list) {
        ArrayList<BigInteger> BigList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            BigInteger m = new BigInteger(list.get(i), 2);
            BigInteger c = m.modPow(e, n);
            BigList.add(c);

        }
        return BigList;

    }

    // this function Decrypts given ciphertext.
    // it takes, d,n and ciphertext array.
    // it calculates the mod one by one.
    // in the end it returns a new String list which includes decrypted binary strings.
    public static ArrayList<String> Decrypt(BigInteger d, BigInteger e, BigInteger n, ArrayList<BigInteger> list) {

        ArrayList<String> BigList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            //BigInteger m = new BigInteger(list.get(i), 2);
            BigInteger c = list.get(i).modPow(d, n);
            String binaryString = c.toString(2);
            BigList.add(binaryString);

        }

        return BigList;
    }

    // this function reverts from binary strings.
    // it takes a binary string list as an input and returns the plain text as an output.
    public static String RevertFromBinaryString(ArrayList<String> list) {
        String str1 = "";
        String str2 = "";
        for (int i = 0; i < list.size(); i++) {
            str2 = RevertBinaryHelper(list.get(i));

            int charCode = Integer.parseInt(str2.substring(0, 8), 2);
            String str = String.valueOf((char) charCode);
            str1 += str;
            charCode = Integer.parseInt(str2.substring(8, 16), 2);
            if (charCode != 0) {
                str = String.valueOf((char) charCode);
                str1 += str;
            }
        }

        return str1;
    }


    // it is helper method to revertBinaryString method.
    // it takes a string as an input and returns padded string.
    // it calculates the length of the binary string an adds padding if required because the binary strings converts 0-7 and 8-15bits to back .
    private static String RevertBinaryHelper(String s) {
        // System.out.println(s.length());
        String str = s;
        while (str.length() < 16) {
            str = "0" + str;

        }
        return str;

    }

    // main function.
    public static void main(String[] args) {
        launch(args);
    }


    // the guı function that creates the interface and controls the application flow.
    @Override
    public void start(Stage primaryStage) throws Exception {


        // alert to alert when reuired
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("RSA");

        // components that been used for gui.
        Label user_id = new Label("Message to be encrypted");
        Label password = new Label("Message to be decrypted");
        Label selectedKey = new Label();
        Label ResultOfOperation = new Label("Result");
        // plain text area
        TextArea tf1 = new TextArea(); // 1st prime number
        tf1.setPrefSize(200, 100);
        tf1.setWrapText(true);
        // encrypted text area
        TextArea tf2 = new TextArea(); // 2nd prime number
        tf2.setPrefSize(200, 100);
        tf2.setWrapText(true);
        tf2.setEditable(false);

        // result area;
        TextArea tf3 = new TextArea();
        tf3.setPrefSize(200, 100);
        tf3.setWrapText(true);
        tf3.setEditable(false);

        // combobox for showing created keys.
        ComboBox comboBox = new ComboBox(items);
        comboBox.setPrefSize(100, 20);
        comboBox.setItems(items);


        Button CreateKeyPair = new Button("Create Key pair");
        Button CalculateEnc = new Button("Encrypt");
        Button CalculateDec = new Button("Decrypt");
        Button SelectKeyPair = new Button("Select");


        // this the action that Encrpyt button controls.
        // it checks for the string and if everything all right calls the encryption function.
        CalculateEnc.setOnAction(e -> {
            if (tf1.getText() == "" || tf1.getText() == null) {

                alert.setContentText("the input cannot be null.");
                alert.show();
                return;
            }

            if (CurrentKey == null) {
                alert.setContentText(" You must select a key from list.");
                alert.show();
                return;
            }


            // calling the encryption function and setting required text fields.
            ArrayList<String> list = ConvertToBinaryString(tf1.getText());
            EncryptedList = Encrypt(CurrentKey.getE(), CurrentKey.getN(), list);


            String str = "";
            for (int i = 0; i < EncryptedList.size(); i++) {
                str += EncryptedList.get(i);
            }
            Base64.Encoder encoder = Base64.getEncoder();
            BigInteger test = new BigInteger(str);
            byte[] bigIntegerBytes = test.toByteArray();
            String base64EncodedBigIntegerBytes = encoder.encodeToString(bigIntegerBytes);
            tf2.setText(base64EncodedBigIntegerBytes);

        });

        //this is the action that control the select key button.
        // it controls the values of the combobox , checks whether there is key or not and and sets the current key.

        SelectKeyPair.setOnAction(e -> {


            for (int i = 0; i < listOfKeys.size(); i++) {
                if (comboBox.getValue() != null && comboBox.getValue().equals(listOfKeys.get(i).name)) {
                    CurrentKey = listOfKeys.get(i).key;
                }
            }
        });

        //this is the action that control the Generate key pair  button.
        // it calls generate key function and sets required fields.
        CreateKeyPair.setOnAction(e -> {
            GenerateKey();

        });

        //this is the action that control the Decrypt key button.
        // it calls Decrypt function and reverts the string back then sets the text-field.
        CalculateDec.setOnAction(e -> {
            if (EncryptedList.size() == 0) {
                alert.setContentText("you must encrypt a text first.");
                alert.show();
                return;
            }
            DecryptedList = Decrypt(CurrentKey.getD(), CurrentKey.getE(), CurrentKey.getN(), EncryptedList);
            PlainText = RevertFromBinaryString(DecryptedList);
            tf3.setText(PlainText);


        });


        // pane to create the gui.
        // calling created gui components from here.
        GridPane root = new GridPane();
        root.setHgap(10); //horizontal gap in pixels => that's what you are asking for
        root.setVgap(10); //vertical gap in pixels
        root.addRow(0, user_id, tf1);
        root.addRow(0, CalculateEnc);
        root.addRow(2, password, tf2);
        root.addRow(2, CalculateDec);
        root.addRow(4, ResultOfOperation, tf3);

        root.addRow(6, comboBox);
        root.addRow(6, SelectKeyPair);
        root.addRow(6, selectedKey);

        root.addRow(8, CreateKeyPair);

        Scene scene = new Scene(root, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("RSA Algorithm");
        primaryStage.show();
    }


}