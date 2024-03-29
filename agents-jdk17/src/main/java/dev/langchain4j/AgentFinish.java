package dev.langchain4j;

import java.util.Map;

record AgentFinish  (Map<String,Object> returnValues, String log ) {

}