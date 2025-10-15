package org.nika.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChatJsonStore {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ChatRecord {
        public String msg;
        public String date;

        public ChatRecord() {}
        public ChatRecord(String msg, String date) {
            this.msg = msg;
            this.date = date;
        }
        @Override public String toString() {
            return "ChatRecord{msg='" + msg + "', date='" + date + "'}";
        }
    }

    public static class Store {
        private final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        private final Map<String, ChatRecord> data = new ConcurrentHashMap<>();
        private final Path file = Path.of("chats.json");

        public Store() throws IOException {
            if (Files.exists(file)) load(file);
        }

        public void createTemplate(String... chatNames) throws IOException {
            for (String name : chatNames) data.putIfAbsent(name, new ChatRecord("<msg>", "<date>"));
            save(file);
        }

        public void putOrUpdate(String chatName, String msg, String date) throws IOException {
            data.put(chatName, new ChatRecord(msg, date));
            save(file);
        }

        public String toJson() throws IOException {
            return mapper.writeValueAsString(data);
        }

        public void save(Path path) throws IOException {
            if (path.getParent() != null) Files.createDirectories(path.getParent());
            mapper.writeValue(path.toFile(), data);
        }

        public void load(Path path) throws IOException {
            Map<String, ChatRecord> loaded = mapper.readValue(
                    path.toFile(),
                    new TypeReference<Map<String, ChatRecord>>(){}
            );
            data.clear();
            data.putAll(loaded);
        }

        public Map<String, ChatRecord> view() {
            return Map.copyOf(data);
        }
    }
}
