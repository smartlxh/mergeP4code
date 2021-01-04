package parser;

public class HeaderVar {
    private String name;
    private int id;
    private Header headerType;
    private boolean isMetadata;

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setHeaderType(Header headerType){
        this.headerType = headerType;
    }

    public void setIsMeadata(boolean isMeadata){
        this.isMetadata = isMeadata;
    }

    public String getName(){
        return this.name;
    }

    public int getId(){
        return this.id;
    }

    public Header getHeaderType(){
        return this.headerType;
    }

    public boolean isMetadata(){
        return this.isMetadata;
    }

}
