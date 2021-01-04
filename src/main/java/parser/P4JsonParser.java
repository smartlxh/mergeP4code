package parser;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import com.googlecode.aviator.AviatorEvaluator;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.ReadContext;
import datatype.Value;
import net.minidev.json.JSONArray;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class P4JsonParser {

    String p4FilePath;
    private List<Header> Headers;
    private List<HeaderVar> headerVarList;
    private List<Action> actionsList;
    private List<Table> tableList;

    Map<String, Header> getHeaderByNameMap;
    Map<String,HeaderVar> getHeaderVarByNameMap;
    Map<String,Action> getActionByNameMap;
    Map<String,Table> getTableByNameMap;

    Map<Integer,Action> getActionByIdMap;

    ReadContext ctx;

    public List<Header> getHeaders()
    {
        return Headers;
    }

    public List<Action> getActionsList(){
        return actionsList;
    }

    public List<Table> getTableList(){
        return tableList;
    }

    public Header getHeaderbyName(String name)
    {
        return getHeaderByNameMap.get(name);
    }

    public Action getActionbyName(String name){
        return getActionByNameMap.get(name);
    }

    public Action getActionbyId(int id){
        return getActionByIdMap.get(id);
    }

    public Table getTablebyName(String name){
        return getTableByNameMap.get(name);
    }

    public HeaderVar getHeaderVarbyName(String name){
        return getHeaderVarByNameMap.get(name);
    }

    public String getJson(String fileName)  {

        String content = null;
        try {
            content = new String(Files.readAllBytes(Paths.get(fileName)));
            // System.out.println(content);

        }catch (Exception e){
            e.printStackTrace();
        }
        return content;
    }

    public P4JsonParser(){
        Headers = new ArrayList<Header>();
        actionsList = new ArrayList<>();
        tableList = new ArrayList<>();

        headerVarList = new ArrayList<>();

        getActionByNameMap = new HashMap<>();
        getHeaderByNameMap = new HashMap<>();
        getHeaderVarByNameMap = new HashMap<>();
        getTableByNameMap = new HashMap<>();

        getActionByIdMap = new HashMap<>();
    }

    public void configParser(String filename){
        p4FilePath = filename;
        String content = getJson(filename);
        // config  the returned value must be a List
        Configuration conf = Configuration.defaultConfiguration();
        Configuration conf2 = conf.addOptions(Option.ALWAYS_RETURN_LIST);

        ctx = JsonPath.using(conf2).parse(content);
    }
    public  void displayHeaders(){
        for(int i=0;i<Headers.size();i++){
            Header aHeader = Headers.get(i);
            System.out.println("Header Name : "+aHeader.getName()+" "+"Id : "+aHeader.getId());
            ArrayList<Field> fields = aHeader.getFields();
            for(int j=0;j<fields.size();j++){
                System.out.println("Field Name : "+fields.get(j).getName()+", "+"Offset : "+fields.get(j).getOffset()+", "+"Length : "+fields.get(j).getLength());
            }
        }
    }

    public void displayDAG(Header startHeader){

        System.out.println("\nHeader Name: "+startHeader.getName());

        Field selectField = startHeader.getSelect();
        if(selectField==null)
        {
            System.out.println("Select Field: null");
        }
        else
        {
            System.out.println("Select Field: "+selectField.getName());
        }

        Map<Value, Header> nextMap = startHeader.getNextsMap();
        if(nextMap==null)
        {
            System.out.println("Next HeaderMap: null");
        }
        else
        {
            System.out.println("Next HeaderMap: ");
            for(Map.Entry<Value, Header> entry: startHeader.getNextsMap().entrySet())
            {
                if(entry.getValue()==null)
                {
                    System.out.println("   "+entry.getKey()+":   null");
                }
                else
                {
                    System.out.println("   "+entry.getKey()+":   "+entry.getValue().getName());
                }
            }
        }


        Header tmp = startHeader.getDefaultNext();
        System.out.println("   default:   "+((tmp==null)?null:tmp.getName()));

        System.out.println("Is Varlength Header: "+startHeader.isVarLength());
        if(startHeader.isVarLength()){
            System.out.println("Length Field: "+startHeader.getLengthField().getName());
            System.out.println("Length Exper: "+startHeader.getExper());
            //System.out.println("Var Section: "+startHeader.getVarsection().getName());
        }
        System.out.println();


        for(Map.Entry<Value, Header> entry: startHeader.getNextsMap().entrySet())
        {
            Header nextHeader = entry.getValue();
            displayDAG(nextHeader);
        }

    }

    private  void printf(String str){
        System.out.println(str);
    }

    public void displayHeaderVar(){
        for(int i=0;i<headerVarList.size();i++)
        {
            HeaderVar aHeaderVar = headerVarList.get(i);
            System.out.println("\nheaderVar Name: "+aHeaderVar.getName()+
                               "\nid:             "+aHeaderVar.getId()+
                               "\nheader Type:    "+aHeaderVar.getHeaderType().getName()+
                               "\nis Metadata:    "+aHeaderVar.isMetadata());

        }
    }


    public void displayTables(){
        for(int i=0;i<tableList.size();i++){
            printf("tableName: " + tableList.get(i).getName()+", tableId: "+tableList.get(i).getId() );
            printf("tableSize: "+tableList.get(i).getMaxSize()+", matchType: "+tableList.get(i).getMatchType());
            List<MatchKey> keys = tableList.get(i).getMatchKeyList();
            for(int j=0;j<keys.size();j++){
                printf("matchType: "+keys.get(j).getMatchType() + ", "+
                        "matchFullName: "+keys.get(j).getName()+", "+
                        "Header: "+ keys.get(j).getHeaderVarName()+", Field: "+keys.get(j).getFieldName()+", "+
                        "HeaderType: "+ keys.get(j).getMatchField().getHeader().getName()+", Field: "+keys.get(j).getMatchField().getName()
                        );
            }
        }
    }

    public  void displayAcions(){
        System.out.println("\nActions List: ");
        for(int i=0;i<actionsList.size();i++){
            System.out.println("  "+actionsList.get(i).getName() +" "+actionsList.get(i).getId());
            System.out.print("  runtimeData: ");
            List<RuntimeData> list = actionsList.get(i).getRuntimeDataList();
            for(int j=0;j<list.size();j++){
                System.out.print("("+list.get(j).getName() + ","+ list.get(j).getBitwidth()+") ");
            }
            System.out.println();

            printf("  Primitves: ");
            List<Primitive> primitives = actionsList.get(i).getPrimitivesList();
            for(int k=0;k<primitives.size();k++){
                if(primitives.get(k).getType() == PrimitiveTypes.drop){
                    printf("      Primitive type: drop");
                }else if(primitives.get(k).getType() == PrimitiveTypes.assign){
                    printf("      Primitive type: assign");
                    List<OPParameter> parameters = primitives.get(k).getParametersList();
                    for(int x = 0;x<parameters.size();x++){
                        if(parameters.get(x).getType().equals("field")){
                            printf("        parameter type: field: ["+parameters.get(x).getHeaderName()+","+parameters.get(x).getFieldName()+"]");
                        }
                        else if(parameters.get(x).getType().equals("runtime_data")){
                            printf("        parameter type: runtimedata: "+String.valueOf(parameters.get(x).getRuntimeDataId()));
                        }
                        else if(parameters.get(x).getType().equals("hexstr")){
                            printf("        parameter type: hexstr: "+parameters.get(x).getValue());
                        }
                        else if(parameters.get(x).getType().equals("expression")){
                            printf("        parameter type: expression: "+parameters.get(x).getExpression());
                        }

                    }
                }else if(primitives.get(k).getType() == PrimitiveTypes.count){

                }else if(primitives.get(k).getType()== PrimitiveTypes.addHeader){

                }else if(primitives.get(k).getType()== PrimitiveTypes.removeHeader){

                }else{
                    printf("the type of the primitive is not support now");
                }
            }
            System.out.println();
        }
    }


    public void parseHeaders(){
        // the returned data type of the read,is different with the json data,maybe sting ,int ,jsonArray
        List<HashMap<String,Object>> headers = ctx.read("$.header_types.[*]");
        //System.out.println(headers.get(0).getClass().toString()+headers.size());
        //System.out.println(headers.get(1).get("fields").get(0));

        for (int i=0;i < headers.size();i++){
            String headerName = headers.get(i).get("name").toString();
            Header header = new Header(headerName);
            header.setId(Integer.parseInt(headers.get(i).get("id").toString()));
            getHeaderByNameMap.put(header.getName(),header);

            ArrayList<Field> fieldsData = new ArrayList<Field>();
            String fields = headers.get(i).get("fields").toString();
            String pattern = "\\\"(\\w+)\\\",(\\d*),?(\\w*)";
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(fields);
            int offset = 0;
            while(m.find()){
                //System.out.println(m.group(1)+" "+m.group(2)+" "+m.group(3));
                Field field = new Field();
                field.setName(m.group(1));
                field.setLength(m.group(2));
                if(m.group(3).equals("false")){
                    field.setSigned(false);
                }else if(m.group(3).equals("true")){
                    field.setSigned(true);
                }else{
                    field.setSigned(null);
                }
                field.setOffset(offset);
                offset = offset+field.getLength();
                fieldsData.add(field);
                field.setHeader(header);
            }
            header.setFields(fieldsData);
            Headers.add(header);

        }
    }

    public void parserHeaderVar(){//must be called after the parseHeaders
        List<HashMap<String,Object>> headerVar = ctx.read("$.headers.[*]");
        //System.out.println(headers.get(0).getClass().toString()+headers.size());
        int varNum = headerVar.size();
        for(int i=0;i<varNum;i++){
            HeaderVar aHeaderVar = new HeaderVar();

            String headervarName = headerVar.get(i).get("name").toString();
            int id = Integer.parseInt(headerVar.get(i).get("id").toString());
            String headerTypeStr = headerVar.get(i).get("header_type").toString();

            Header headerType = getHeaderByNameMap.get(headerTypeStr);
            boolean isMetadata = Boolean.parseBoolean(headerVar.get(i).get("metadata").toString());

            aHeaderVar.setName(headervarName);
            aHeaderVar.setId(id);
            aHeaderVar.setHeaderType(headerType);
            aHeaderVar.setIsMeadata(isMetadata);

            headerVarList.add(aHeaderVar);
            getHeaderVarByNameMap.put(headervarName,aHeaderVar);
        }
    }


    public void parseActions(){

        List<Map<String,Object>> actions = ctx.read("$.actions.[*]");
        for(int i=0;i<actions.size();i++){
            Action action = new Action();
            action.setName(actions.get(i).get("name").toString());
            action.setId(Integer.parseInt(actions.get(i).get("id").toString()));
            getActionByNameMap.put(action.getName(),action);
            getActionByIdMap.put(action.getId(),action);
            //System.out.println(action.getId()+action.getName());
            JSONArray runtimeData = (JSONArray)actions.get(i).get("runtime_data");
            List<RuntimeData> runtimeDataList = new ArrayList<>();
            action.setRuntimeDataList(runtimeDataList);

            // parse rumtimeDate
            for(int j=0;j<runtimeData.size();j++){

                String extractName = String.format("$.actions[%d].runtime_data[%d].name",i,j);
                List<String> fieldName = ctx.read(extractName);
                String extractWidth = String.format("$.actions[%d].runtime_data[%d].bitwidth",i,j);
                List<Integer> fieldWidth = ctx.read(extractWidth);

                RuntimeData runData = new RuntimeData();
                runData.setName(fieldName.get(0).toString());
                runData.setBitwidth(fieldWidth.get(0));
                runtimeDataList.add(runData);

            }

            // parse primirives
            JSONArray primitives = (JSONArray)actions.get(i).get("primitives");

            for( int k=0;k<primitives.size();k++){
                String extractOp = String.format("$.actions[%d].primitives[%d].op",i,k);
                List<String> opName = ctx.read(extractOp);
                Primitive primitive = new Primitive();

                switch (opName.get(0).toString()){
                    case "drop":
                        primitive.type = PrimitiveTypes.drop;
                        break;
                    case "assign":
                        primitive.type = PrimitiveTypes.assign;
                        String extractArgs = String.format("$.actions[%d].primitives[%d].parameters.[*]",i,k);
                        //JSONArray parameters = ctx.read(extractArgs);
                        List<Map<String,Object>> parameters = ctx.read(extractArgs);


                        for(int x=0;x<parameters.size();x++){
                            String type = parameters.get(x).get("type").toString();
                            OPParameter parameter = new OPParameter();
                            switch (type){
                                case "field":
                                    String value = parameters.get(x).get("value").toString();
                                    // printf(expr);
                                    String pattern = "\\[\"(\\w+)\",\"(\\w+)\"\\]";
                                    Pattern p = Pattern.compile(pattern);
                                    Matcher m = p.matcher(value);

                                    if(m.find()){
                                        // System.out.println(m.group(1)+" "+m.group(2));
                                        parameter.setType("field");
                                        parameter.setHeaderName(m.group(1));
                                        parameter.setFieldName(m.group(2));

                                    }
                                    break;
                                case "runtime_data":
                                    value = parameters.get(x).get("value").toString();
                                    parameter.setType("runtime_data");
                                    //printf(value+"runtime");
                                    parameter.setRuntimeDataId(Integer.parseInt(value));
                                    break;

                                case "hexstr":
                                    value = parameters.get(x).get("value").toString();
                                    // printf(value+"hex");
                                    parameter.setType("hexstr");
                                    parameter.setValue(value);
                                    break;

                                case "expression":
                                    ExperNode root = new ExperNode();
                                    String expression = getVLExpr(parameters.get(x),root);
                                    // printf(expression);
                                    parameter.setType("expression");
                                    parameter.setExpression(expression);
                                    parameter.setRoot(root);
                                    //root.postOrderTravel(root);
                                    //printf(temp.getName()+field.getName());
                                    break;
                            }

                        /*    String expr = parameters.get(x).toString();
                            //  System.out.println(expr);
                            String pattern = "type=(\\w+),\\s*value=\\[\\\"(\\w)+\\\",\\\"(\\w+)\\\"\\]";
                            Pattern p = Pattern.compile(pattern);
                            Matcher m = p.matcher(expr);
                            OPParameter parameter = new OPParameter();
                            if(m.find()){
                                // System.out.println(m.group(1)+m.group(2)+m.group(3));
                                parameter.setType("field");
                                parameter.setHeaderName(m.group(2));
                                parameter.setFieldName(m.group(3));
                            }else {
                                String patternRuntime = "type=(\\w+),\\s*value=(\\d+)";
                                p = Pattern.compile(patternRuntime);
                                m = p.matcher(expr);
                                if(m.find()){
                                    //System.out.println(m.group(1)+m.group(2));
                                    parameter.setType("runtime_data");
                                    parameter.setRuntimeDataId(Integer.parseInt(m.group(2)));
                                }
                            }*/
                            primitive.parametersList.add(parameter);

                        }

                        break;
                    default:
                        System.out.println("not support for this operation now : "+opName.get(0).toString());

                }
                action.getPrimitivesList().add(primitive);
            }
            action.ParseActon2POFIns(this);
            actionsList.add(action);
        }
    }


    public void parseTables(){  //must be called after the parserHeaderVar
        List<Map<String,Object>> tables = ctx.read("$.pipelines.[0].tables.[*]");
        for(int i=0;i<tables.size();i++){

            boolean hasFindHeader = false;

            Table table = new Table();
            tableList.add(table);
            // printf(tables.get(i).get("name").toString());
            String tableName = tables.get(i).get("name").toString();
            table.setName(tableName);
            getTableByNameMap.put(tableName,table);
            table.setId(Integer.parseInt(tables.get(i).get("id").toString()));
            table.setMaxSize(Integer.parseInt(tables.get(i).get("max_size").toString()));
            table.setMatchType(tables.get(i).get("match_type").toString());
            List<Map<String,Object>> keys = (List<Map<String,Object>>)tables.get(i).get("key");

            for(int j=0;j<keys.size();j++){
                MatchKey key = new MatchKey();
                key.setMatchType(keys.get(j).get("match_type").toString());
                key.setName(keys.get(j).get("name").toString());
                String target = keys.get(j).get("target").toString();

                //printf(target);
                String pattern = "\\\"(\\w+)\\\",\\s*\\\"(\\w+)\\\"";
                Pattern p = Pattern.compile(pattern);
                Matcher m = p.matcher(target);
                if(m.find()){
                    key.setHeaderVarName(m.group(1));

                    if(!hasFindHeader){
                        // Associate tables and headers
                        String aHeaderVarName = m.group(1);
                        HeaderVar aHeaderVar = getHeaderVarbyName(aHeaderVarName);
                        if(!aHeaderVar.isMetadata()){
                            Header aHeader = aHeaderVar.getHeaderType();
                            String aHeaderName = aHeader.getName();
                            if(aHeaderName!=null){
                                //User-defined metadata should also be considered in the future
                                aHeader.setTable(table);
                                table.setHeader(aHeader);
                                hasFindHeader=true;
                            }
                        }
                    }

                    key.setFieldName(m.group(2));

                    HeaderVar aHeaderVar = getHeaderVarByNameMap.get(m.group(1));
                    Header aHeader = aHeaderVar.getHeaderType();
                    Field aField = aHeader.findFieldByName(m.group(2));
                    key.setMatchField(aField);
                }
                table.getMatchKeyList().add(key);
            }





        }

    }
    private String getVLExpr(Object exper, Field lengthField, Header temp){// to get the expression string

        ReadContext ctx = JsonPath.parse(exper);


        // printf(exper.get("type").toString());
        String type = ctx.read("$.type");

        switch(type){
            case "expression":

                Map map = ctx.read("$.value");
                if(map.containsKey("op")){
                    String op = map.get("op").toString();
                    return "("+getVLExpr(map.get("left"),lengthField,temp) +" "+ op +" " + getVLExpr(map.get("right"),lengthField,temp)+")";
                }
                else{
                    return getVLExpr(map,lengthField,temp);
                }

            case "field"://This only applies if there is only one field in the expression,if there are more,error!
                // printf(ctx.read("$.value").toString());
                String field = ctx.read("$.value").toString();

                String pattern = "\\\"(\\w+)\\\",\\s*\\\"(\\w+)\\\"";
                Pattern p = Pattern.compile(pattern);
                Matcher m = p.matcher(field);
                if(m.find()){
                    //Header header  = getHeaderVarByNameMap.get(m.group(1)).getHeaderType();
                    temp.setName(m.group(1));
                    lengthField.setName(m.group(2));
                    //lengthField.setLength(String.valueOf(header.findFieldByName(m.group(2)).getLength()));
                    //getHeaderVarByNameMap.get(m.group(1)).getHeaderType().setLengthField(lengthField);
                    return "x";// to express the variable
                }


            case "hexstr":
                // printf(ctx.read("$.value").toString());
                return ctx.read("$.value").toString();
        }

        return "";
    }

    private String getVLExpr(Object exper,ExperNode node){// to get the expression string

        ReadContext ctx = JsonPath.parse(exper);


        // printf(exper.get("type").toString());
        String type = ctx.read("$.type");

        switch(type){
            case "expression":

                Map map = ctx.read("$.value");
                if(map.containsKey("op")){
                    String op = map.get("op").toString();
                    node.setValue(op);
                    node.setType(1);
                    ExperNode left = new ExperNode();
                    ExperNode right = new ExperNode();
                    node.left = left;
                    node.right = right;
                    return "("+getVLExpr(map.get("left"),left) +" "+ op +" " + getVLExpr(map.get("right"),right)+")";
                }
                else{
                    return getVLExpr(map,node);
                }

            case "field":
                // printf(ctx.read("$.value").toString());
                String field = ctx.read("$.value").toString();

                String pattern = "\\\"(\\w+)\\\",\\s*\\\"(\\w+)\\\"";
                Pattern p = Pattern.compile(pattern);
                Matcher m = p.matcher(field);
                if(m.find()){
                    Header header  = getHeaderVarByNameMap.get(m.group(1)).getHeaderType();
                    Field field1 = header.findFieldByName(m.group(2));

                    node.setType(3);
                    node.setHeader(header);
                    node.setField(field1);

                    return "x";// to express the variable
                }


            case "hexstr":
                // printf(ctx.read("$.value").toString());
                String value = ctx.read("$.value").toString();
                node.setType(2);
                node.setValue(value);
                return value;
        }

        return "";
    }

// to calcuate the string expression
    public int calcuateExper(String exper,int value){  // maybe the type of in not appropriate
        Map<String,Object> env = new HashMap<>();
        env.put("x",value);
        System.out.println(AviatorEvaluator.execute(exper,env));
        return  Integer.parseInt(AviatorEvaluator.execute(exper,env).toString());
    }
    public void parseDAG(){
        List<Map<String,Object>> parsers = ctx.read("$.parsers[0].parse_states.[*]");

        for(int i=0;i<parsers.size();i++) {

            String opPath = String.format("$.parsers[0].parse_states[%d].parser_ops[0].op",i);
            String op = ctx.read(opPath).toString();
            //printf(op);
            switch (op){
                case "[\"extract\"]":

                    String transitionKey = String.format("$.parsers[0].parse_states[%d].transition_key[0].value",i);
                    String transitions = String.format("$.parsers[0].parse_states[%d].transitions.[*]",i);
                    String matchkey = ctx.read(transitionKey).toString();

                    String pattern = "\\\"(\\w+)\\\",\\s*\\\"(\\w+)\\\"";
                    Pattern p = Pattern.compile(pattern);
                    Matcher m = p.matcher(matchkey);
                    if(m.find()){

                        Header header = getHeaderVarByNameMap.get(m.group(1)).getHeaderType();
                        if(header == null){
                            printf("the transistion_key not exist");
                            return;
                        }
                         Field select = new Field();
                         select.setName(m.group(2));//   only set the value,not calcuate the offset and length
                         header.setSelect(select);
                        // to extract the next header
                        List<Map<String,Object>> states = ctx.read(transitions);

                        for(int j=0 ;j<states.size();j++){

                            if(states.get(j).get("next_state") != null){ // the field of json is "null",the return is null pointer
                                String nextHeder = states.get(j).get("next_state").toString();
                                int index = nextHeder.indexOf('_');
                                nextHeder = nextHeder.substring(index+1);
                                header.addNext(states.get(i).get("value").toString(),getHeaderVarByNameMap.get(nextHeder).getHeaderType());
                            }
                        }
                    }
                    break;
                case "[\"set\"]": // for now ,distinguish the variable header by the field,may have some promlems to fix

                    String experPath = String.format("$.parsers[0].parse_states[%d].parser_ops[0].parameters[1]",i);
                    List<Map<String,Object>> exper = ctx.read(experPath);
                    //printf(exper.get(0).get("value").getClass().toString());
                    //printf(getVLExpr(exper.get(0)));

                    Field lengthField = new Field();// parameters to get the result
                    Header temp = new Header("temp") ;
                    String exp = getVLExpr(exper.get(0),lengthField,temp);
                    Header LenHeader = getHeaderVarByNameMap.get(temp.getName()).getHeaderType();
                    Field LenField = LenHeader.findFieldByName(lengthField.getName());



                    getHeaderVarByNameMap.get(temp.getName()).getHeaderType().setExper(exp);
                    printf(exp);
                    break;
            }


        }
        return;

    }

    public void parseDAG(boolean full){
        //in the .p4 file, the "extract" statement must come first if it exist .
        //then the "transition" is next. transition_key must be the field of Header, can't be the metadata or others

        Map<Header,Map<String,String>> header2Next = new HashMap<>();
        Map<String, Header> state2Header = new HashMap<>(); // stateName - header eg. parse_ipv4 - ipv4_t


        List<Map<String,Object>> parsers = ctx.read("$.parsers[0].parse_states.[*]");
        Boolean flag = false ; // to indicate whether the firstHeader is found  ,found - true; not found - false;

        for(int i=0;i<parsers.size();i++) {

            String stateName = parsers.get(i).get("name").toString();
            //String opPath = String.format("$.parsers[0].parse_states[%d].parser_ops[0].op",i);
            String opPath = String.format("$.parsers[0].parse_states[%d].parser_ops[*]", i);
            List<HashMap<String, Object>> opsList = ctx.read(opPath);

//            String op;
//            if (opsList.size() == 0)//parse_ipv4_next的op就是空。，因为该状态不需要提取任何数据
//                op = null;
//            else {
//                op = opsList.get(0).get("op").toString();
//            }

            // for tofino parser -hard code
            // when there are more than one parsr_ops,we think it is a variable length header

            if (opsList.size() == 1) { // distinguish the normal header and variable header by the size of op number,
                                        // the number of op = 1 indicates that the header is fiexed length.

                String extractParStr = String.format("$.parsers[0].parse_states[%d].parser_ops[0].parameters[*]", i);
                List<HashMap<String, Object>> extractPar = ctx.read(extractParStr);
                String HeaderVar = extractPar.get(0).get("value").toString(); // this state is represent this header

                Header aHeader = getHeaderVarByNameMap.get(HeaderVar).getHeaderType();
//                if (stateName.equals("start"))
//                    Header.firestHeader = aHeader;
                if(flag == false){
                    Header.firestHeader = aHeader;
                    flag = true;
                }
                state2Header.put(stateName, aHeader);

                String transitionKeyListStr = String.format("$.parsers[0].parse_states[%d].transition_key[*]", i);
                List<Map<String, Object>> transkeyList = ctx.read(transitionKeyListStr);
                if (transkeyList.size() != 0) {
                    //String transitionKey = String.format("$.parsers[0].parse_states[%d].transition_key[0].value",i);
                    //String matchkey = ctx.read(transitionKey).toString();
                    String matchkey = transkeyList.get(0).get("value").toString();
                    String pattern = "\\\"(\\w+)\\\",\\s*\\\"(\\w+)\\\"";
                    Pattern p = Pattern.compile(pattern);
                    Matcher m = p.matcher(matchkey);
                    if (m.find()) {//if find, set the "next" field value
                        Header headerOfKey = getHeaderVarByNameMap.get(m.group(1)).getHeaderType();
                        if (headerOfKey == null) {
                            printf("the transistion_key not exist");
                            return;
                        }
                        Field fieldOfKey = headerOfKey.findFieldByName(m.group(2));
                        aHeader.setSelect(fieldOfKey);
                    }
                }

                String transitions = String.format("$.parsers[0].parse_states[%d].transitions[*]", i);
                Map<String, String> tmp = new HashMap<>();
                List<Map<String, Object>> nextstates = ctx.read(transitions);
                for (int j = 0; j < nextstates.size(); j++) {
                    Map<String, Object> aNextState = nextstates.get(j);
                    if (aNextState.get("value").equals("default"))
                        if (aNextState.get("next_state") == null) {
                            aHeader.setDefaultNext(null);
                        } else {
                            tmp.put("default", aNextState.get("next_state").toString());
                        }
                    else {
                        Object a = aNextState.get("next_state");
                        tmp.put(aNextState.get("value").toString(), a != null ? a.toString() : null);
                    }
                }
                header2Next.put(aHeader, tmp);
            } else {
                // the variable length header
                for (int x = 0; x < opsList.size(); x++) {
                    String op = opsList.get(x).get("op").toString();
                    if (op.equals("set")) {
//                        String op1Path = String.format("$.parsers[0].parse_states[%d].parser_ops[%d].parameters[0]",i,x);
//                        List<Map<String,Object>> op1 = ctx.read(op1Path);
//                        String optionHeaderStr = op1.get(0).get("value").toString();
//                        Header optionHeader = getHeaderVarByNameMap.get(optionHeaderStr).getHeaderType();
                        // for parse expersion the length
                        String experPath = String.format("$.parsers[0].parse_states[%d].parser_ops[%d].parameters[1]", i, x);
                        List<Map<String, Object>> exper = ctx.read(experPath);

                        Field lengthField = new Field();// parameters to get the result
                        Header temp = new Header("temp");
                        String exp = getVLExpr(exper.get(0), lengthField, temp);
                       // System.out.println(exp);

                        String variableHeader = stateName.split("\\_")[1];
                        Header variHeader = getHeaderVarByNameMap.get(variableHeader).getHeaderType();
                        if(flag == false){
                            printf(stateName);
                            Header.firestHeader = variHeader;
                            flag = true;
                        }
                        state2Header.put(stateName, variHeader);
                        variHeader.setExper(exp);
                        variHeader.setIsVarLength(true);
                        variHeader.setLengthField(variHeader.findFieldByName(lengthField.getName()));
                        // for transaction

                        //printf(variableHeader);

                        String transitionKeyListStr = String.format("$.parsers[0].parse_states[%d].transition_key[*]", i);
                        List<Map<String, Object>> transkeyList = ctx.read(transitionKeyListStr);
                        if (transkeyList.size() != 0) {
                            //String transitionKey = String.format("$.parsers[0].parse_states[%d].transition_key[0].value",i);
                            //String matchkey = ctx.read(transitionKey).toString();
                            String matchkey = transkeyList.get(0).get("value").toString();
                            String pattern = "\\\"(\\w+)\\\",\\s*\\\"(\\w+)\\\"";
                            Pattern p = Pattern.compile(pattern);
                            Matcher m = p.matcher(matchkey);
                            if (m.find()) {//if find, set the "next" field value
                                Header headerOfKey = getHeaderVarByNameMap.get(m.group(1)).getHeaderType();
                                if (headerOfKey == null) {
                                    printf("the transistion_key not exist");
                                    return;
                                }
                                Field fieldOfKey = headerOfKey.findFieldByName(m.group(2));
                                variHeader.setSelect(fieldOfKey);
                            }
                        }

                        String transitions = String.format("$.parsers[0].parse_states[%d].transitions[*]", i);
                        Map<String, String> tmp = new HashMap<>();
                        List<Map<String, Object>> nextstates = ctx.read(transitions);
                        for (int j = 0; j < nextstates.size(); j++) {
                            Map<String, Object> aNextState = nextstates.get(j);
                            if (aNextState.get("value").equals("default"))
                                if (aNextState.get("next_state") == null) {
                                    variHeader.setDefaultNext(null);
                                } else {
                                    tmp.put("default", aNextState.get("next_state").toString());
                                }
                            else {
                                Object a = aNextState.get("next_state");
                                tmp.put(aNextState.get("value").toString(), a != null ? a.toString() : null);
                            }
                        }
                        header2Next.put(variHeader, tmp);

                        break;
                    }
                }

            }
        }
            //printf(op);
//            if(op==null)//parse_ipv4_next的op就是空。，因为该状态不需要提取任何数据
//            {   // the previous state is parse the varlength field
//                //this state doesn't have the "extract", only have the "transtion"
//                String transPathStr =  String.format("$.parsers[0].parse_states[%d].transition_key[0].value",i);
//                String trans = ctx.read(transPathStr).toString();
//                String pattern = "\\\"(\\w+)\\\",\\s*\\\"(\\w+)\\\"";
//                Pattern p = Pattern.compile(pattern);
//                Matcher m = p.matcher(trans);
//                if(m.find())
//                {
//                    Header headerOfKey = getHeaderVarByNameMap.get(m.group(1)).getHeaderType();
//                    if(headerOfKey == null){
//                        printf("the transistion_key not exist");
//                        return;
//                    }
//                    Field fieldOfKey = headerOfKey.findFieldByName(m.group(2));
//                    headerOfKey.setSelect(fieldOfKey);
//
//                    String transitions = String.format("$.parsers[0].parse_states[%d].transitions.[*]",i);
//                    List<Map<String,Object>> nextstates = ctx.read(transitions);
//                    Map<String,String> tmp = new HashMap<>();
//                    for(int k=0;k<nextstates.size();k++){
//
//                        Map<String,Object> aTransKeyMap = nextstates.get(k);
//                        if(aTransKeyMap.get("value").toString().equals("default"))
//                            if(aTransKeyMap.get("next_state")==null)
//                            {
//                                headerOfKey.setDefaultNext(null);
//                            }
//                            else
//                            {
//                                tmp.put("default",aTransKeyMap.get("next_state").toString());
//                            }
//                        else
//                        {
//                            Object a = aTransKeyMap.get("next_state");
//                            tmp.put(aTransKeyMap.get("value").toString(),(a!=null?a.toString():null));
//                        }
//
//                    }
//                    header2Next.put(headerOfKey,tmp);
//                }
//
//            }
//            else
//            switch (op)
//            {
//                case "extract":
//
//                    String extractParStr = String.format("$.parsers[0].parse_states[%d].parser_ops[0].parameters[*]",i);
//                    List<HashMap<String,Object>> extractPar = ctx.read(extractParStr);
//                    String HeaderVar = extractPar.get(0).get("value").toString(); // this state is represent this header
//
//                    Header aHeader = getHeaderVarByNameMap.get(HeaderVar).getHeaderType();
//                    if(stateName.equals("start"))
//                        Header.firestHeader = aHeader;
//                    state2Header.put(stateName,aHeader);
//
//                    String transitionKeyListStr = String.format("$.parsers[0].parse_states[%d].transition_key[*]",i);
//                    List<Map<String,Object>> transkeyList = ctx.read(transitionKeyListStr);
//                    if(transkeyList.size()!=0){
//                        //String transitionKey = String.format("$.parsers[0].parse_states[%d].transition_key[0].value",i);
//                        //String matchkey = ctx.read(transitionKey).toString();
//                        String matchkey = transkeyList.get(0).get("value").toString();
//                        String pattern = "\\\"(\\w+)\\\",\\s*\\\"(\\w+)\\\"";
//                        Pattern p = Pattern.compile(pattern);
//                        Matcher m = p.matcher(matchkey);
//                        if(m.find()){//if find, set the "next" field value
//                            Header headerOfKey = getHeaderVarByNameMap.get(m.group(1)).getHeaderType();
//                            if(headerOfKey == null){
//                                printf("the transistion_key not exist");
//                                return;
//                            }
//                            Field fieldOfKey = headerOfKey.findFieldByName(m.group(2));
//                            aHeader.setSelect(fieldOfKey);
//                        }
//                    }
//
//
//                    String transitions = String.format("$.parsers[0].parse_states[%d].transitions[*]",i);
//                    Map<String,String> tmp = new HashMap<>();
//                    List<Map<String,Object>> nextstates = ctx.read(transitions);
//                    for(int j=0;j<nextstates.size();j++){
//                        Map<String,Object> aNextState = nextstates.get(j);
//                        if(aNextState.get("value").equals("default"))
//                            if(aNextState.get("next_state")==null)
//                            {
//                                aHeader.setDefaultNext(null);
//                            }
//                            else
//                            {
//                                tmp.put("default",aNextState.get("next_state").toString());
//                            }
//                        else
//                        {
//                            Object a = aNextState.get("next_state");
//                            tmp.put(aNextState.get("value").toString(),a!=null?a.toString():null);
//                        }
//                    }
//                    header2Next.put(aHeader,tmp);
//                    break;
//
//                case "set": // for now ,distinguish the variable header by the field,may have some promlems to fix
//
//                    String op1Path = String.format("$.parsers[0].parse_states[%d].parser_ops[1].parameters[0]",i);
//                    List<Map<String,Object>> op1 = ctx.read(op1Path);
//                    String optionHeaderStr = op1.get(0).get("value").toString();
//                    Header optionHeader = getHeaderVarByNameMap.get(optionHeaderStr).getHeaderType();
//
//                    String experPath = String.format("$.parsers[0].parse_states[%d].parser_ops[0].parameters[1]",i);
//                    List<Map<String,Object>> exper = ctx.read(experPath);
//                    //printf(exper.get(0).get("value").getClass().toString());
//                    //printf(getVLExpr(exper.get(0)));
//
//
//                    Field lengthField = new Field();// parameters to get the result
//                    Header temp = new Header("temp") ;
//                    String exp = getVLExpr(exper.get(0),lengthField,temp);
//
//                    Header varHeader=getHeaderVarByNameMap.get(temp.getName()).getHeaderType();
//                    varHeader.setExper(exp);
//                    varHeader.setIsVarLength(true);
//                    varHeader.setLengthField(varHeader.findFieldByName(lengthField.getName()));
//                    varHeader.setVarsection(optionHeader);
//
//                    varHeader.setSelect(null);
//                    varHeader.setDefaultNext(null);
//                    header2Next.put(varHeader,null);
//
//
//                    String transPathStr =  String.format("$.parsers[0].parse_states[%d].transitions.[*]",i);
//                    List<Map<String,Object>> transPath = ctx.read(transPathStr);
//                    //String nextStateStr = transPath.get(0).get("next_state").toString();
//
//                    break;
//            }
//        }

        //Map<Header,Map<String,String>> header2Next = new HashMap<>();
        //Map<String,Header> state2Header = new HashMap<>();
        //Map<String,String> varState2nextState = new HashMap<>();
        for (Map.Entry<Header, Map<String,String>> entry : header2Next.entrySet()) {
            Header aHeader = entry.getKey();
            Map<String,String> str2state = entry.getValue();
            if(str2state!=null)
            {
                for(Map.Entry<String,String> entry1 :  str2state.entrySet()){
                    String valueStr = entry1.getKey();
                    String stateName = entry1.getValue();

                    if(stateName!=null)
                    {
                        Header nextHeader = state2Header.get(stateName);
                        if(nextHeader!=null){
                            if(valueStr.equals("default")){
                                aHeader.setDefaultNext(nextHeader);
                            }
                            else
                                aHeader.addNext(valueStr,nextHeader);
                        }
                    }
                    else
                    {
                        if(valueStr.equals("default")){
                            aHeader.setDefaultNext(null);
                        }
                        else
                            aHeader.addNext(valueStr,null);
                    }
                }
            }
        }

    }

    public void register(Kryo kryo){
        kryo.register(HashMap.class);

        kryo.register(Header.class);
        kryo.register(HeaderVar.class);
        kryo.register(Action.class);
        kryo.register(Table.class);

        kryo.register(OPParameter.class);
        kryo.register(Primitive.class);
        kryo.register(RuntimeData.class);
        kryo.register(tmpSpaceCount.class);

        kryo.register(ExperNode.class);
        kryo.register(Field.class);
        kryo.register(MatchKey.class);
        kryo.register(POFInsRuntimeDataRec.class);
        kryo.register(Primitive.class);

    }

    public void saveResult(){
        Kryo kryo = new Kryo();
        register(kryo);

        try
        {
            Output output = new Output(new FileOutputStream(p4FilePath.substring(0,p4FilePath.length()-4)+"map"));
            kryo.writeObject(output, Header.firestHeader);
            kryo.writeObject(output, getHeaderByNameMap);
            kryo.writeObject(output, getHeaderVarByNameMap);
            kryo.writeObject(output, getActionByNameMap);
            kryo.writeObject(output, getTableByNameMap);
            kryo.writeObject(output, getActionByIdMap);
            output.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }



    }


    public static void main(String[] args){

        P4JsonParser parser = new P4JsonParser();
        parser.configParser("../p4_controller/simple_ipv4forword.json");

        parser.parseHeaders();
        parser.displayHeaders();
        parser.parserHeaderVar();
        parser.displayHeaderVar();
        parser.parseActions();
        parser.displayAcions();
        parser.parseTables();
        parser.displayTables();
        parser.parseDAG(true);
        parser.displayDAG(Header.firestHeader);
        parser.calcuateExper(parser.getHeaderByNameMap.get("ipv4_t").getExper(),6);
    }
}

