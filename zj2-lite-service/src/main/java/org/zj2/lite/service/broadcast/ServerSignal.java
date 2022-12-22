package org.zj2.lite.service.broadcast;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 *  ServerMsg
 *
 * @author peijie.ye
 * @date 2022/12/12 16:06
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServerSignal implements Serializable {
    private static final long serialVersionUID = 20221212160609L;
    private String serverId;
    private long timestamp;
    private String tag;
    private String signal;
}
