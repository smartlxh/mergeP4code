package parser;

import java.util.ArrayList;
import java.util.List;

public class Table {
    private String name;
    private int id;
    private List<MatchKey> keyList;//Match field must be in the same header
    private int maxSize;
    private String matchType;

    private Header header;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<MatchKey> getMatchKeyList() {
        return keyList;
    }

    public void setMatchKeyList(List<MatchKey> list) {
        this.keyList = list;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public Table(){
        keyList = new ArrayList<>();
    }

    public void setMatchType(String matchType) {
        this.matchType = matchType;
    }

    public String getMatchType(){
        return matchType;
    }
}


