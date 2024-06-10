package org.example.server.dataobject.Story;

import lombok.Data;
import java.util.Map;

@Data
public class StoryMessage {
    private String content;
    private String sessionId;  // 添加sessionId字段
    private String role;  // 添加role字段
    private Map<String, String> queryParams;  // 确保在消息中包含查询参数
}