import java.io.IOException;

public class main {

    public static void main(String[] args) {
        try {

            // First set of code:
            Index.init();
            Commit a = new Commit("a7c7189ad24b64263c569df9c5a6c3b837c6ed63", "Sam2", "testing if i addedd dirs right");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}