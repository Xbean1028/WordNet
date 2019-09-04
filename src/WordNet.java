//实现一个不可变的数据类型——单词网络
import edu.princeton.cs.algs4.*;

import java.util.Iterator;


public class WordNet{
    //定义同义词的字典ST，一个词对应的集合ids,即多个点
    private ST<String,SET<Integer>> synsets;
    private ST<Integer, String> id_nouns;
    private ST<String,String> nouns_meaning;
    private Digraph hypernyms; //hypernyms的有向图
    private boolean[] outEdge;  //顶点是否有一条出度的边
    private int outSum;  //有出度的顶点总数
    private int idSum;
    private SAP sap;  //shortest ancestral path最短公共祖先路径



    //构造函数，传入两个输入文件的名字
    public WordNet(String synsets, String hypernyms){
        if(synsets == null||hypernyms == null){
            throw new IllegalArgumentException("argument to WordNet() is full");
        }
        readSynets(synsets);
        readHypernyms(hypernyms);
    }

    //读同义词集，文件中每一行是单词网中的一个点，先是序号，再是一个点包含n个同义词，最后是词义
    private void readSynets(String synsets) {
        this.synsets = new ST<String,SET<Integer>>();
        id_nouns = new ST<Integer, String>();
        nouns_meaning = new ST<String, String>();
        In synset = new In(synsets);
        while(synset.hasNextLine()){
            idSum++;
            String str = synset.readLine();
            String[] fields = str.split(",");
            int id = Integer.parseInt(fields[0]);
            id_nouns.put(id,fields[1]);
            nouns_meaning.put(fields[1],fields[2]);
            //将该结点中的单词按一定的序号存储
            String[] nouns = fields[1].split(" ");
            for(int i = 0;i<nouns.length;i++){
                if(this.synsets.contains(nouns[i]))
                    this.synsets.get(nouns[i]).add(id);
                else{
                    SET<Integer> ids=new SET<Integer>();
                    ids.add(id);
                    this.synsets.put(nouns[i],ids);
                }
            }
        }

    }

    //读上义词集，文件中每一行是单词的编号和它的上义词的编号，用无向图将它们的用边相连，并标记为有出度
    private void readHypernyms(String hypernyms) {
        this.hypernyms = new Digraph(idSum);
        In hypernym = new In(hypernyms);
        outEdge = new boolean[idSum];
        while (hypernym.hasNextLine()) {
            String str = hypernym.readLine();
            String[] fields = str.split(",");
            int v = Integer.parseInt(fields[0]);
            for (int i = 1; i < fields.length; i++) {
                int w = Integer.parseInt(fields[i]);
                this.hypernyms.addEdge(v, w);
            }
            if (!outEdge[v] && fields.length != 1) {
                outSum++;
            }
            outEdge[v] = true;
        }
        isRootedDAG();
        sap = new SAP(this.hypernyms);
    }

    //检测图有没有环
    private void isRootedDAG() {
        if (idSum - outSum != 1) {
            throw new IllegalArgumentException("is not a one root digraph");
        }
        Topological topo = new Topological(hypernyms);
        if (!topo.hasOrder()) {
            throw new IllegalArgumentException("is not a DAG");
        }
    }

    //返回所有单词网的名词
    public Iterable<String> nouns(){
        return synsets.keys();
    }

    //判断该单词是否是单词网络中的名词
    public boolean isNoun(String word){
        if(word==null){
            throw new IllegalArgumentException("arguments to isNoun() is null");
        }
        return synsets.contains(word);
    }

    //查找单词
    public String Search(String word){
        if(word==null){
            throw new IllegalArgumentException("arguments to isNoun() is null");
        }
        String n="";
        if(synsets.contains(word))
        {
            n="Meaning: "+nouns_meaning.get(word)+'\n';
            Iterator<Integer> it =synsets.get(word).iterator();
            n+="The synsets are:\n";
            while(it.hasNext()){
                String nouns = id_nouns.get(it.next());
                String[] sp = nouns.split(" ");
                for(int i = 0;i<sp.length;i++){
                    if(!sp[i].equals(word)){
                        n+=sp[i]+" ";
                    }
                }
            }
        }
        return n;
    }

    //名词A和名词B之间的距离
    public int distance(String nounA,String nounB){
        if (nounA == null || nounB == null) {
            throw new IllegalArgumentException("arguments to distance() is null");
        }
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException("arguments to distance() is not a WordNet noun");
        }
        SET<Integer> setA = synsets.get(nounA);
        SET<Integer> setB = synsets.get(nounB);
        if (setA.size() == 1 && setB.size() == 1) {
            return sap.length(setA.max(), setB.max());
        } else {
            return sap.length(setA, setB);
        }
    }

    //一个同义词，是A和B共同拥有的祖先
    //定义一个共同祖先的最短路径
    public String sap(String nounA,String nounB){
        if (nounA == null || nounB == null) {
            throw new IllegalArgumentException("arguments to sap() is null");
        }
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException("arguments to sap() is not a WordNet noun");
        }
        int id;
        SET<Integer> setA = synsets.get(nounA);
        SET<Integer> setB = synsets.get(nounB);
        if (setA.size() == 1 && setB.size() == 1) {
            id = sap.ancestor(setA.max(), setB.max());
        } else {
            id = sap.ancestor(setA, setB);
        }
        return id_nouns.get(id);
    }
    //做该类的单元测试
    public static void main(String[] args){
        WordNet wordnet = new WordNet("synsets.txt","hypernyms.txt");
        while (!StdIn.isEmpty()) {
            String n = StdIn.readString();
            wordnet.Search(n);
//            String v = StdIn.readString();
//            String w = StdIn.readString();
//            StdOut.printf(wordnet.sap(v,w)+"\n");

        }

    }
}

