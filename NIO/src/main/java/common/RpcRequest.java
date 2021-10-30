package common;

import lombok.Data;

import java.io.Serializable;

@Data
public class RpcRequest implements Serializable {
    private String className;
    private String method;
    private Object[] params;
}
