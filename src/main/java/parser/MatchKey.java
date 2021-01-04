package parser;

public class MatchKey{
    String matchType;
    String name;// full name ,for example:hdr.ipv4.dstAddr

    String HeaderVarName;

    String FieldName;



    Field matchField;


    public String getMatchType() {
        return matchType;
    }

    public void setMatchType(String matchType) {
        this.matchType = matchType;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }


    public String getHeaderVarName() {
        return HeaderVarName;
    }

    public void setHeaderVarName(String headerName) {
        HeaderVarName = headerName;
    }

    public String getFieldName() {
        return FieldName;
    }

    public void setFieldName(String fieldName) {
        FieldName = fieldName;
    }

    public Field getMatchField()
    {
        return matchField;
    }

    public void setMatchField(Field matchField){
        this.matchField = matchField;
    }


}
