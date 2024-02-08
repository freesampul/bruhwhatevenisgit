import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class Index {

    public static HashMap<String, String> files = new HashMap<>();

    public static void init() {
        try {
            Utils.checkIfIndexExists();
            Utils.checkEditOrDelete();
            Utils.checkIfObjectsExists();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addBlobGivenFileName(String fileName) throws Exception {
        Blob b = new Blob(fileName);
        files.put(fileName, b.getSha());
        // Write file and sha to index
        writeIndex();
    }

    public static void addDirGivenTree(Tree tree) throws Exception {
        String content = tree.getContents();
        files.put("tree", Utils.sha1(content));
        writeIndex();
    }

    public static void removeBlobGivenFileName(String fileName) throws Exception {
        files.remove(fileName);
        // deletes file with same name
        String content = Utils.readFile(fileName);
        Files.deleteIfExists(Paths.get("./objects/" + Utils.sha1(content)));
        writeIndex();
    }

    private static void writeIndex() throws Exception {
        // clears index file content
        Path index = Paths.get("index");
        Files.write(index, "".getBytes());

        for (String key : files.keySet()) {
            Utils.writeToFile(key + " : " + files.get(key) + "\n", "index");
        }
    }
}
