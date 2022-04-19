package info.lostred.ruler.domain;

/**
 * 节点信息
 *
 * @author lostred
 */
public class NodeInfo {
    private Object node;
    private StringBuilder stringBuilder = new StringBuilder();

    public NodeInfo(Object node) {
        this.node = node;
    }

    public NodeInfo(Object node, StringBuilder stringBuilder) {
        this.node = node;
        this.stringBuilder = stringBuilder;
    }

    public StringBuilder getStringBuilder() {
        return stringBuilder;
    }

    public Object getNode() {
        return node;
    }

    public void setNode(Object node) {
        this.node = node;
    }

    public String getNodeTrace() {
        return this.stringBuilder.toString();
    }
}
