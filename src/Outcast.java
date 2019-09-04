import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;

public class Outcast {
    private final WordNet wn;
    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        if (wordnet == null) {
            throw new IllegalArgumentException("arguments to Outcast() is null");
        }
        wn = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        if (nouns == null) {
            throw new IllegalArgumentException("arguments to outcast() is null");
        }
        int id = -1;
        int max = -1;
        int[] distSum = new int[nouns.length];
        for (int i = 0; i < nouns.length; i++) {
            for (int j = 0; j < nouns.length; j++) {
                distSum[i] += wn.distance(nouns[i], nouns[j]);
            }
            if (distSum[i] > max) {
                max = distSum[i];
                id = i;
            }
        }
        if (id == -1) {
            throw new IllegalArgumentException("error");
        }
        return nouns[id];
    }

    // test client
    public static void main(String[] args) {
        String[] args1 = {"synsets.txt", "hypernyms.txt", "outcast2.txt","outcast3.txt","outcast4.txt","outcast5.txt", "outcast8.txt",
                "outcast11.txt","outcast5.txt","outcast5.txt"};
        WordNet wordnet = new WordNet(args1[0], args1[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args1.length; t++) {
            In in = new In(args1[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args1[t] + ": " + outcast.outcast(nouns));
        }
    }

}