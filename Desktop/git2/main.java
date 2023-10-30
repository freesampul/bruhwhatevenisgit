import java.io.IOException;

public class main {

    public static void main(String[] args) {
        try {

            //HERES HOW WE TEST THE DELETE!
            //First set of code:
            // Index.init();
            // Index.addBlobGivenFileName("input.txt");
            // Index.addBlobGivenFileName("input2.txt");
            // Commit a = new Commit("", "Sam", "Testing initial commit, this has input 1 & 2");

            //Second set BE SURE TO TYPE ADD OR DELETE INTO THE INDEX!
            Index.init();
            Commit n = new Commit("591f9e8cc05f36f3eb8599d004e3a2c5c144b4ef", "Sam2", "Testing delete?");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}