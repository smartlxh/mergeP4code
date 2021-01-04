package parser;
public class Field {
    private String name;
    private int offset = -1;//only valid when this.isvarlength is false
    int length = -1;//only valid when this.isvarlength is false
    boolean isvarlength;

    Header header;

    public Boolean getSigned() {
        return signed;
    }

    public void setSigned(Boolean signed) {
        this.signed = signed;
    }

    private Boolean signed;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLength() {
        return length;
    }

    public void setLength(String length) {
        if(length.equals(""))
            isvarlength = true;
        else
        {
            isvarlength = false;
            this.length = Integer.parseInt(length);
        }
    }

    public void setHeader(Header header){
        this.header = header;
    }

    public Header getHeader(){
        return header;
    }

}
