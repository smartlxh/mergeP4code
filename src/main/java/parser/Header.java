package parser;

import com.googlecode.aviator.AviatorEvaluator;
import datatype.Value;
import org.apache.commons.codec.binary.Hex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//import controllerApi.FlowTable;

public class Header {
    public static Header firestHeader;

    private String name;
    private ArrayList<Field> fields;
    private int id;

    private Field select = null;
    private Map<Value, Header> nexts;
    private Header defaultNext = null;

    private boolean isVarLength = false;
    private Field lengthField; // to calcuate the actual length of header with the value of the field
    private String Exper;
    private Header varsection;// only valid when the isVarLength is true;

    private Table table = null;//p4 defined table. sometime is null when this header not defined table
    //private FlowTable poftabale = null;//table is for POF. always is not null when runtime

    public void setTable(Table table) {
        this.table = table;
    }

    public Table getTable(){
        return table;
    }

    //public void setPoftabale(FlowTable poftabale) {
    //    this.poftabale = poftabale;
    //}

    //public FlowTable getPoftabale() {
    //    return poftabale;
    //}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Field> getFields() {
        return fields;
    }

    public void setFields(ArrayList<Field> fields) {
        this.fields = fields;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Header (String headerName){
        this.name = headerName;
        fields = new ArrayList<>();
        nexts = new HashMap<>();
    }

    public Field getSelect() {
        return select;
    }

    public void setSelect(Field select) {
        this.select = select;
    }

    public Map<Value, Header> getNextsMap() {
        return nexts;
    }

    public void setNextsMap(Map<Value, Header> nexts) {
        this.nexts = nexts;
    }

    public void addNext(String value, Header header)
    {
        String no0xStr = value.substring(2);
        try {
            byte[] bytes = Hex.decodeHex(no0xStr);
            Value aValue = Value.valueOf(bytes);
            this.nexts.put(aValue,header);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void addNext(Value value, Header header)
    {
        this.nexts.put(value,header);
    }

    public Header getNextbyValue(String value)
    {
        return this.nexts.get(value);
    }

    public String getExper() {
        return Exper;
    }

    public void setExper(String exper) {
        Exper = exper;
    }

    public Field getLengthField() {
        return lengthField;
    }

    public void setLengthField(Field lengthField) {
        this.lengthField = lengthField;
    }

    public boolean isVarLength() {
        return isVarLength;
    }

    public void setIsVarLength(boolean varLength) {
        isVarLength = varLength;
    }

    public void setDefaultNext(Header defaultNext){
        this.defaultNext = defaultNext;
    }

    public Header getDefaultNext(){
        return this.defaultNext;
    }

    public void setVarsection(Header varsection) {
        this.varsection = varsection;
    }

    public Header getVarsection(){
        return this.varsection;
    }

    public Field findFieldByName(String fieldName){
        int len = fields.size();
        for(int i=0;i<len;i++)
        {
            Field afield = fields.get(i);
            if(afield.getName().equals(fieldName))
            {
                return afield;
            }
        }
        return null;
    }

    public int getFixedLen()
    {
        Field lastField = fields.get(fields.size()-1);
        return lastField.getOffset()+lastField.length;
    }

    public int getVarLen(byte [] data, int CurrDataOff){
        if(isVarLength==false)
            return 0;

        int lenFieldOff = lengthField.getOffset();
        int lenFieldLen = lengthField.getLength();

        Value tmp = Value.valueOf(data,CurrDataOff+lenFieldOff,lenFieldLen);
        int lenFieldVal = tmp.intValue();

        Map<String,Object> env = new HashMap<>();
        env.put("x",lenFieldVal);
        //System.out.println(AviatorEvaluator.execute(exper,env));
        return Integer.parseInt(AviatorEvaluator.execute(Exper,env).toString());
    }

    // for echo to the new P4
    private String formatFields(){
        StringBuilder str = new StringBuilder();
        for(int i=0;i<fields.size();i++){
            str.append(fields.get(i).getName()).append(" : ").append(fields.get(i).length).append(";").append("\n");
        }
        return str.toString();
    }
    @Override
    public String toString() {
        return "header_type " +name+" {\n" +
                "fields { \n"+formatFields()+
                "}"+"\n"+
               "}"+"\n";
    }
}
