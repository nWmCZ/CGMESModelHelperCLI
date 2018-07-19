package eu.sn.model;

import java.util.Optional;

public class IdBox {

    private Optional<String> prefix = Optional.empty();
    private Optional<String> postfix = Optional.empty();

    public Optional<String> getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = Optional.of(prefix);
    }

    public Optional<String> getPostfix() {
        return postfix;
    }

    public void setPostfix(String postfix) {
        this.postfix = Optional.of(postfix);
    }

    @Override
    public String toString() {
        return "IdBox{" +
                "prefix=" + prefix +
                ", postfix=" + postfix +
                '}';
    }
}
