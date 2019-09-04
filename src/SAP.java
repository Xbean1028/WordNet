import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Digraph;

public class SAP{
    private int length;      //公共祖先的最短路径的值
    private int ancestor;    //最近的祖先
    private Digraph copyG;   //保存关联图的副本
    private int[] distTo1;   //两个给定结点到图中任意节点的长度
    private int[] distTo2;
    private boolean[] marked1; //两个定点到图中其他节点是否有边
    private boolean[] marked2;
    private Stack<Integer> stack1;  //辅助矩阵存储变化
    private Stack<Integer> stack2;

    //构造函数初始化实例变量
    public SAP(Digraph G){
        if(G == null){
            throw new IllegalArgumentException("argument to SAP() is null");
        }
        copyG = new Digraph(G);
        distTo1 = new int[G.V()];  //V()返回顶点数 数组
        distTo2 = new int[G.V()];
        marked1 = new boolean[G.V()];
        marked2 = new boolean[G.V()];
        stack1 = new Stack<Integer>();
        stack2 = new Stack<Integer>();
    }

    //v和w之间的
    public int length(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        compute(v, w);
        return length;
    }
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        validateVertices(v);
        validateVertices(w);
        compute(v, w);
        return length;
    }
    private void validateVertex(int v) {
        int V = marked1.length;
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
    }

    private void validateVertices(Iterable<Integer> vertices) {
        if (vertices == null) {
            throw new IllegalArgumentException("argument is null");
        }
        int V = marked1.length;
        for (int v : vertices) {
            if (v < 0 || v >= V) {
                throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
            }
        }
    }


    private void compute(int v, int w) {
        length = -1;
        ancestor = -1;
        distTo1[v] = 0;
        distTo2[w] = 0;
        marked1[v] = true;
        marked2[w] = true;
        stack1.push(v);
        stack2.push(w);
        Queue<Integer> q1 = new Queue<Integer>();
        Queue<Integer> q2 = new Queue<Integer>();
        q1.enqueue(v);
        q2.enqueue(w);
        bfs(q1, q2);
    }

    private void compute(Iterable<Integer> v, Iterable<Integer> w) {
        length = -1;
        ancestor = -1;
        Queue<Integer> q1 = new Queue<Integer>();
        Queue<Integer> q2 = new Queue<Integer>();
        for (int v1 : v) {
            marked1[v1] = true;
            stack1.push(v1);
            distTo1[v1] = 0;
            q1.enqueue(v1);
        }
        for (int w1 : w) {
            marked2[w1] = true;
            stack2.push(w1);
            distTo2[w1] = 0;
            q2.enqueue(w1);
        }
        bfs(q1, q2);
    }

    //用广度优先遍历搜索策略，交替用BFS扩展新的结点，扩展到第一个被两个单独的点都访问过的点就是最近的公共祖先
    private void bfs(Queue<Integer> q1,Queue<Integer> q2){
        while(!q1.isEmpty()||!q2.isEmpty()){
            if (!q1.isEmpty()) {
                int v = q1.dequeue();
                if (marked2[v]) {
                    if (distTo1[v] + distTo2[v] < length || length == -1) {
                        ancestor = v;
                        length = distTo1[v] + distTo2[v];
                    }
                }
                // stop adding new vertex to queue if the distance exceeds the length
                if (distTo1[v] < length || length == -1) {
                    for (int w : copyG.adj(v)) {                   //adj返回相邻节点
                        if (!marked1[w]) {
                            distTo1[w] = distTo1[v] + 1;
                            marked1[w] = true;
                            stack1.push(w);
                            q1.enqueue(w);

                            // StdOut.println("push " + w + " into q1");
                        }
                    }
                }
            }
            if (!q2.isEmpty()) {
                int v = q2.dequeue();
                if (marked1[v]) {
                    if (distTo1[v] + distTo2[v] < length || length == -1) {
                        ancestor = v;
                        length = distTo1[v] + distTo2[v];
                    }
                }
                // stop adding new vertex to queue if the distance exceeds the length
                if (distTo2[v] < length || length == -1) {
                    for (int w : copyG.adj(v)) {
                        if (!marked2[w]) {
                            distTo2[w] = distTo2[v] + 1;
                            marked2[w] = true;
                            stack2.push(w);
                            q2.enqueue(w);

                            // StdOut.println("push " + w + " into q2");
                        }
                    }
                }
            }

            }
        init();
    }

    private void init() {
        while (!stack1.isEmpty()) {
            int v = stack1.pop();
            marked1[v] = false;
        }
        while (!stack2.isEmpty()) {
            int v = stack2.pop();
            marked2[v] = false;
        }
    }

    public int ancestor(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        compute(v, w);
        return ancestor;
    }

    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        validateVertices(v);
        validateVertices(w);
        compute(v, w);
        return ancestor;
    }


    public static void main(String[] args) {
        In in = new In("digraph1.txt");
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}

