package info.lostred.ruler.domain;

/**
 * 节点信息
 *
 * @author lostred
 */
public class NodeInfo {
    /**
     * 节点
     */
    private Object node;
    /**
     * 链路建造者
     * <p>记录父节点字段名</p>
     */
    private StringBuilder tracer = new StringBuilder();

    public NodeInfo(Object node) {
        this.node = node;
    }

    public NodeInfo(Object node, StringBuilder tracer) {
        this.node = node;
        this.tracer = tracer;
    }

    public StringBuilder getTracer() {
        return tracer;
    }

    public Object getNode() {
        return node;
    }

    public void setNode(Object node) {
        this.node = node;
    }

    public String getTrace() {
        return this.tracer.toString();
    }
}
