import java.io.IOException;
import java.nio.file.Files;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class Tree {

    public static HashMap<String, String> treeContent = new HashMap<>();
    public String sha = "";

    public Tree() throws Exception {
        checkIfTreeExists();
    }

    public static void checkIfTreeExists() throws Exception {
        Path treePath = Paths.get("Tree");
        if (!Files.exists(treePath))
            Files.createFile(treePath);
    }

    public void add(String addString) throws Exception {
        if (addString.substring(0, 4).equals("blob")) {
            checkIfTreeExists();
            String blobAndFileName = addString.substring(7);
            String fileName = blobAndFileName.substring(blobAndFileName.indexOf(":") + 2);
            treeContent.put(fileName, addString.substring(7));
        } else if (addString.substring(0, 4).equals("tree")) {
            checkIfTreeExists();
            treeContent.put("tree", addString.substring(7));
        }
        writeTree();
    }

    public void remove(String removeString) throws Exception {
        // Loops through the treeContent and remove the key that has the same value as
        // removeString This removes FILES
        for (String key : treeContent.keySet()) {
            if (key.equals(removeString)) {
                treeContent.remove(key);
                System.out.println("Removed FILE " + key);
                writeTree();
                return;
            }
        }
        // Loops through treeContent and removes the value that has the same value as
        // remove This removes TREES
        for (String value : treeContent.values()) {
            if (value.equals(removeString)) {
                treeContent.remove("tree", value);
                System.out.println("Removed TREE " + value);
                writeTree();
                return;
            }
        }
    }

    public void writeTree() throws Exception {
        // clears Tree file content
        Path index = Paths.get("Tree");
        Files.write(index, "".getBytes());

        int currentNum = 1;

        for (String key : treeContent.keySet()) {
            if (key.equals("tree")) {
                Utils.writeToFile("tree : " + treeContent.get(key), "Tree");
            } else {
                Utils.writeToFile("blob : " + treeContent.get(key), "Tree");
            }

            if (currentNum < treeContent.size()) {
                Utils.writeToFile("\n", "Tree");
            }
            currentNum++;
        }
        String treeContent = Utils.readFile("Tree");
        Utils.checkIfObjectsExists();
        String treePath = Utils.sha1(treeContent);
        Path treePath2 = Paths.get("./objects/" + treePath);
        Files.write(treePath2, "".getBytes());
        Utils.writeToFile(treeContent, "./objects/" + treePath);
    }

    public void addIndex(String currCommit) throws Exception {
        // reads content in index file
        String indexContent = Utils.readFile("index");
        if (indexContent.equals("")) {
            System.out.println("empty index");
            return;
        }
        String[] indexContentArray = indexContent.split("\n");
        for (int i = 0; i < indexContentArray.length; i++) {
            System.out.println("currCommit: " + currCommit);
            if ((indexContentArray[i].contains("*deleted*") || indexContentArray[i].contains("*edited*"))
                    && currCommit.equals("")) {
                throw new Exception("Cannot delete or edit because this is the first commit!");
            }
            if (indexContentArray[i].contains("*deleted*")) {
                String fileToBeDeleted = indexContentArray[i].substring(10);
                findDeletedFile(fileToBeDeleted, currCommit);
            } else if (indexContentArray[i].contains("*edited*")) {
                String fileToBeDeleted = indexContentArray[i].substring(9);
                findDeletedFile(fileToBeDeleted, currCommit);
            } else if (!indexContentArray[i].contains("*edited*") || !indexContentArray[i].contains("*deleted*")) {
                System.out.println("Adding " + indexContentArray[i] + " to tree");
                if (indexContentArray[i].substring(0, 4).equals("tree")) {
                    add(indexContentArray[i]);
                } else {
                    String blobSha = indexContentArray[i].substring(indexContentArray[i].indexOf(":") + 2);
                    String fileName = indexContentArray[i].substring(0, indexContentArray[i].indexOf(":") - 1);
                    System.out.println("blob : " + blobSha + " : " + fileName);
                    add("blob : " + blobSha + " : " + fileName);
                }
            }
        }
    }

    public String getSha() {
        String treeContent = Utils.readFile("Tree");
        return Utils.sha1(treeContent);
    }

    public String addDirectory(String directoryName) throws Exception {
        // Traverse every file in directoryName
        StringBuilder files = new StringBuilder();
        File directory = new File(directoryName);
        if (directory.exists() && directory.isDirectory()) {
            File[] filesList = directory.listFiles();
            if (files != null) {
                for (File file : filesList) {
                    if (file.isFile()) {
                        add("blob : " + Utils.sha1(Utils.readFile(file.getName())) + " : " + file.getName());
                    } else if (file.isDirectory()) {
                        addDirectory(file.getName());
                    }
                }
            }
        }
        Path treePath = Paths.get(directoryName + "/Tree");
        if (!Files.exists(treePath))
            Files.createFile(treePath);
        new Blob(directoryName + "/Tree");
        String treeSha = Utils.sha1(Utils.readFile(directoryName + "/Tree"));
        add("tree : " + treeSha + " : " + directoryName);
        return treeSha;
    }

    public String getContents() throws Exception {
        String treeContent = Utils.readFile("Tree");
        return treeContent;
    }

    // This method is gonna find the tree containg the file to be delted by getting
    // the currs commit tree, checking if the file is there, and if it isn't it
    // recurses on itself. After its found, it puts in the og trees files into the
    // new
    public void findDeletedFile(String fileToBeDeleted, String currCommit) throws Exception {
        String checkTree = Commit.getTreeFromShaOfCommit(currCommit);
        String checkTreeContent = Utils.readFile("./objects/" + checkTree);
        String[] checkTreeContentArray = checkTreeContent.split("\n");
        for (int i = 0; i < checkTreeContentArray.length; i++) {
            if (checkTreeContentArray[i].contains(fileToBeDeleted)) {
                for (int j = 0; j < checkTreeContentArray.length; j++) {
                    if (checkTreeContentArray[j].contains(fileToBeDeleted)) {
                        continue;
                    } else {
                        String[] checkTreeContentArray2 = checkTreeContentArray[j].split(" : ");
                        if (!checkTreeContentArray2[0].equals("") || !checkTreeContentArray2[1].equals("")) {
                            System.out.println("Adding " + checkTreeContentArray2[0] + " : " + checkTreeContentArray2[1]
                                    + " to tree");
                            add(checkTreeContentArray[j]);
                        }
                    }
                }
                return;
            }
            String currCommitContent = Utils.readFile("./objects/" + currCommit);
            String[] currCommitContentArray = currCommitContent.split("\n");
            String prevCommit = currCommitContentArray[1];
            if (prevCommit.equals("")) {
                throw new Exception("File not found, there was no instance of the file found through previous commits");
            }
            findDeletedFile(fileToBeDeleted, prevCommit);
        }
    }
}