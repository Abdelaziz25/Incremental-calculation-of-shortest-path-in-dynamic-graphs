package Server;

public class DummyGraph implements IGraph{

    public void DummyGraph(){
    }

    @Override
    public void add(int u, int v) {

    }

    @Override
    public void delete(int u, int v) {

    }

    @Override
    public int shortestPath(int u, int v, String algorithm) {
        return 0;
    }

    @Override
    public int getGraphInitialSize() {
        return 0;
    }
}
