import java.io.IOException;

public class main {

    public static void main(String[] args) {
        try {

            // First set of code:
            Index.init();
            Commit a = new Commit("17a4a020e52f8f9c9e92f426f8d8a19ea1b1b065", "Sam2", "Delete inpit");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}