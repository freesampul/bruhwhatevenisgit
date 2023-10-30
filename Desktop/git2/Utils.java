import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

public class Utils {
    public static String deletedTreeInfo = "";
    public static boolean deleted = false;

    // Googled
    public static String sha1(String password) {
        String sha1 = "";
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(password.getBytes("UTF-8"));
            sha1 = byteToHex(crypt.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sha1;
    }

    public static String readFile(String fileName) {
        File file = new File(fileName);
        String currentString = "";

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while (reader.ready()) {
                char nextChar = (char) reader.read();
                currentString += nextChar;

            }
            reader.close();
        } catch (IOException ex) {
            System.out.println("couldn't read the file, loser");
            ex.printStackTrace();
        }
        return currentString;
    }

    public static String readFileFile(File fileName) {
        String currentString = "";

        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            while (reader.ready()) {
                char nextChar = (char) reader.read();
                currentString += nextChar;

            }
            reader.close();
        } catch (IOException ex) {
            System.out.println("couldn't read the file, loser");
            ex.printStackTrace();
        }
        return currentString;
    }

    public static void writeToFile(String content, String filename) {
        try {
            File file = new File(filename);

            if (!file.exists()) {
                file.createNewFile();
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                writer.write(content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void checkIfObjectsExists() throws Exception {
        // check if object exists
        String folder = "./objects";
        Path folderPath = Paths.get(folder);
        if (!Files.exists(folderPath)) {
            try {
                Files.createDirectory(folderPath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void checkIfIndexExists() throws Exception {
        // check if Index exists
        Path index = Paths.get("index");
        if (!Files.exists(index)) {
            try {
                Files.createFile(index);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    public static void checkEditOrDelete() throws Exception {
        String indexContent = readFile("index");
        String[] indexContentArray = indexContent.split("\n");
        for (int i = 0; i < indexContentArray.length; i++) {
            if (indexContentArray[i].contains("*deleted*")) {
                Index.files.put("*deleted*", indexContent.substring(10));
                System.out.println("YOOOO");
                // } else if (indexContentArray[i].contains("*edited*")) {
                // {
                // Index.files.put("*edited*", indexContent.substring(9));
                // }
                // }
            }
        }
    }

    public static String getDeletedTreeInfo() {
        return deletedTreeInfo;
    }

    public static void setDeletedTreeInfo() {
        deletedTreeInfo = "";
    }

    public static boolean getDeleted() {
        return deleted;
    }

    public static void setDeleted() {
        deleted = false;
    }
}