package com.example.lxh;

// for merge multi .p4 to be one .p4
// the first header must be ethernet
// distinguish the different modality by the type field of ethernet

import com.example.lxh.config.Config;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import datatype.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parser.Field;
import parser.Header;
import parser.P4JsonParser;
import parser.Table;

import java.io.*;
import java.sql.PseudoColumnUsage;
import java.util.*;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private final String COMMONHEADER = "ethernet_t";
    private final String FILEDIR = "./src/main/resources/";
    private Boolean Tag = false;

    private List<P4JsonParser> parserList = new ArrayList<>();
    private Map<Header,P4JsonParser> header2ParserMap = new HashMap<>();

    private final String TAGHEADER = "header_type tag_t {\n" +
            "    fields {\n" +
            "        outport : 48;\n" +
            "        etherType : 16;\n"+
            "    }\n" +
            "}\n";

    private final String TAGTABLE = "table tagIdentify {\n" +
            "    reads {\n" +
            "        tag.outport : ternary;\n" +
            "    }\n" +
            "    actions {\n" +
            "        tag_forward;\n" +
            "    }\n" +
            "    \n" +
            "}\n";

    private final String TAGPARSER = "parser parse_tag {\n" +
            "    extract(tag);\n" +
            "    return ingress;\n" +
            "}\n";

    private final String TAGACTION = "action tag_forward(egress_spec) {\n" +
            "    \n" +
            "    modify_field(ethernet.etherType,tag.etherType);\n" +
            "    modify_field(ig_intr_md_for_tm.ucast_egress_port, egress_spec);\n" +
            "    remove_header(tag);\n" +
            "}\n";


    private String getFileBetweenlines(int start,int end,String filename){
        int lines = 1;
        StringBuilder builder = new StringBuilder();
        File file = new File(filename);

        FileReader fileReader = null;
        try {
            fileReader = new FileReader(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        LineNumberReader reader = new LineNumberReader(fileReader);
        String line = "";

        while(true){
            try {
                if ((line=reader.readLine()) != null) {
                    lines++;
                    logger.info(String.valueOf(lines));
                    if(lines <= end && lines>=start){
                        builder.append(line.toString()).append("\n");
                        //logger.info(line.toString());
                    }
                    if(lines > end) break;
                }else{
                    break;
                }


            } catch (IOException e) {
                e.printStackTrace();

            }

        }

        return builder.toString();
    }
    private HashMap<String,List<Header> > name2HeaderListMap = new HashMap<>();
    private String declareHeaderVar(Map<String,Header> headers){
        StringBuilder headerVars = new StringBuilder();
        for(String name : headers.keySet()){
            headerVars.append("header "+name+" "+name.split("_")[0]+";"+"\n");
        }

        headerVars.append("header tag_t tag;\n");
        return headerVars.toString();
    }
    private String formatEthernetParsers(List<Header> firstHeaderList){
        StringBuilder keyValue = new StringBuilder();
        for(int i=0;i<firstHeaderList.size();i++){
            Map<Value,Header> map = firstHeaderList.get(i).getNextsMap();
            for(Map.Entry<Value,Header> item : map.entrySet()){
                keyValue.append("0x").append(item.getKey().toString().substring(0,item.getKey().getActualLen()/4)+" : ").append("parse_").append(item.getValue().getName().split("_")[0]).append(";").append("\n");
            }
        }
        // add tag -- etherType = 0x1111
        keyValue.append("0x1111 : parse_tag;\n");
       // logger.info(keyValue.toString());
        return keyValue.toString();

    }
    private String formartParserSelect(Header header){
        StringBuilder builder = new StringBuilder();
        Field field = header.getSelect();
        if(field != null){
            builder.append("return select(latest.").append(field.getName().toString()).append(")").append(" {\n");
        }
        else{
            return "return ingress;\n";
        }
        for(Map.Entry<Value,Header> item : header.getNextsMap().entrySet()){
            //logger.info(item.getKey().toString()+":"+item.getKey().getActualLen()+":");
            builder.append("0x").append(item.getKey().toString().substring(0,item.getKey().getActualLen()/4)+" : ").append("parse_").append(item.getValue().getName().split("_")[0]).append(";").append("\n");
        }
        builder.append("default: ingress;\n}");
        return builder.toString();
    }
    // format the parser for header
    private void formatParser(Header root,StringBuilder builder){
        String headerVar = root.getName().split("_")[0].toString();
        builder.append("parser ").append("parse_").append(headerVar).append(" {\n");
        builder.append("extract("+headerVar+");"+
                "\n"+
                formartParserSelect(root)+
                "}\n"
                );

        Map<Value,Header> map = root.getNextsMap();
        for(Map.Entry<Value,Header> item : map.entrySet()){
            Header header = item.getValue();
            formatParser(header,builder);
        }

    }

    private String formatTableApply(Header firstHeader){
        P4JsonParser parser = header2ParserMap.get(firstHeader);
        List<Table> tableList = parser.getTableList();
        StringBuilder builder = new StringBuilder();
        for(int i=0;i<tableList.size();i++){
            builder.append("apply(").append(tableList.get(i).getName()).append(");\n");
        }
        return builder.toString();
    }

    private String formatIngress(List<Header> firstHeaderList){
        StringBuilder builder = new StringBuilder();

        for(int i=0;i<firstHeaderList.size();i++){
            Map<Value,Header> map = firstHeaderList.get(i).getNextsMap();
           // builder.append("if(ethernet.etherType == %s)");
            int size = map.size();
            String str = "if(ethernet.etherType == %s";
            for(int j=0;j<size-1;j++){
                str+="||ethernet.etherType==%s";
            }
            str+=")";
            logger.info(str);
            List<String> values = new ArrayList<>();
            for(Map.Entry<Value,Header> item : map.entrySet()){
               // keyValue.append("0x").append(item.getKey().toString().substring(0,4)+" : ").append("parse_").append(item.getValue().getName().split("_")[0]).append("\n");
                String value = "0x"+item.getKey().toString().substring(0,4);// for ethernet.ethertype 4byte
                values.add(value);
            }
            //
            switch(values.size()){
                case 1:
                        builder.append(String.format(str,values.get(0)));

                        break;
                case 2:
                        builder.append(String.format(str,values.get(0),values.get(1)));
                        break;

            }
            builder.append("{\n");
            builder.append(formatTableApply(firstHeaderList.get(i)));
            builder.append("}\n");

        }
        return builder.toString();

    }
    private void writeStrtoFile(String str,String fileName){
        FileWriter writer;
        try {
            writer = new FileWriter(fileName);
            writer.write(str);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public void mergeCode(){
        Config config = new Config();
        List<String> modalityList = config.config();
        /*if(modalityList.size() == 1){
            logger.error("just one modality,not need to merge!");
            return ;
        }*/

        if(modalityList.size() == 0){
            logger.error("no modality,can not merge!");
            return ;
        }

        // for the Header.firstHeader is a static field
        // the list is for save the firstHeader of every P4
        List<Header> firstHeaderList = new ArrayList<>();
        for(int i=0;i<modalityList.size();i++){
            P4JsonParser parser = new P4JsonParser();
            parserList.add(parser);
            parser.configParser("./src/main/resources/"+modalityList.get(i));

            parser.parseHeaders();
            //parser.displayHeaders();
            parser.parserHeaderVar();
            //parser.displayHeaderVar();
            parser.parseActions();
            //parser.displayAcions();
            parser.parseTables();
            //parser.displayTables();
            parser.parseDAG(true);
            //parser.displayDAG(Header.firestHeader);
            //System.out.println(Header.firestHeader.getName().toString());
            firstHeaderList.add(Header.firestHeader);
            header2ParserMap.put(Header.firestHeader,parser);
           // logger.info(Header.firestHeader.getName());

            Queue<Header> headerQueue= new LinkedList<Header>();
            headerQueue.add(Header.firestHeader);
            // wide search from the start header
            // for judge whether there are multi header of the same hadername
            while(!headerQueue.isEmpty()){
                Header temp = headerQueue.poll();
                String headerName = temp.getName().toString();
                if(name2HeaderListMap.containsKey(headerName)){
                    name2HeaderListMap.get(headerName).add(temp);
                    //logger.info(headerName+"1");
                }else{
                   List<Header> headerList = new ArrayList<>();
                   headerList.add(temp);
                   name2HeaderListMap.put(headerName,headerList);
                    //logger.info(headerName+"2");
                }

                Map<Value,Header> nextHeadersMap = temp.getNextsMap();
                for(Header header :nextHeadersMap.values()){
                    headerQueue.add(header);
                }
            }
        }


        //echo to a new P4 file
        StringBuilder P4Str = new StringBuilder();
        String headerFile = "#include \"includes/intrinsic_metadata.p4\"\n" +
                "#include \"includes/constants.p4\"";
        P4Str.append(headerFile).append("\n");
        // save all the name of Header after changing the same name of headers
        HashMap<String,Header> name2HeaderMap = new HashMap<>();

        for(Map.Entry<String,List<Header>> item : name2HeaderListMap.entrySet()){
            String headerName = item.getKey();
            List<Header> headers = item.getValue();
            // especially for the header of ethernet
            // because the ethernet header is the common and start header of multi P4 as well as
            // the start header of new P4
            if(headerName.equals(COMMONHEADER)){
                P4Str.append(headers.get(0).toString()); // output any header between the headers of ethernet
                name2HeaderMap.put(headers.get(0).getName().toString(),headers.get(0));
                continue;
            }
            else{
                if(headers.size() > 1){ // this is the case that there are same name of multi headers;
                    for(int j=0;j<headers.size();j++){
                        headers.get(j).setName(headerName.split("_")[0]+String.valueOf(j)+"_t");
                        name2HeaderMap.put(headers.get(j).getName().toString(),headers.get(j));
                        P4Str.append(headers.get(j).toString());
                    }
                }
                else{
                    name2HeaderMap.put(headers.get(0).getName().toString(),headers.get(0));
                    P4Str.append(headers.get(0).toString());
                }
            }
        }
        P4Str.append(TAGHEADER);
        // for headers
        P4Str.append(declareHeaderVar(name2HeaderMap));

        //logger.info(P4Str.toString());
        // for parsers
        final String parserStart = new String("parser start {\n" +
                "    return parse_ethernet;\n" +
                "}\n");
        String parse_ethernet = new String("parser parse_ethernet {\n" +
                "    extract(ethernet);\n" +
                "    return select(latest.etherType) {\n" +
                formatEthernetParsers(firstHeaderList)+
                "        default: ingress;\n" +
                "    }\n" +
                "}\n");
        P4Str.append(parserStart);
        P4Str.append(parse_ethernet);
        P4Str.append(TAGPARSER);
        //logger.info(parse_ethernet);

        for(int i=0;i<firstHeaderList.size();i++){
            Header header = firstHeaderList.get(i);
            for(Header head : header.getNextsMap().values()){
                formatParser(head,P4Str);
            }

        }
        //logger.info(P4Str.toString());

        // for action and table
        // just copy the soruce code for line200 to line300.so the P4 code must set aside 200 to 300 lines for writing tables and actions
        // must not have the same name of action

        for(int i=0;i<modalityList.size();i++){
            String modalityName = modalityList.get(i).split("\\.")[0]+".p4";
            String tableAndAction = getFileBetweenlines(200,300,"./src/main/resources/"+modalityName);
            P4Str.append("\n");
            P4Str.append(tableAndAction);

        }
        P4Str.append(TAGACTION);
        P4Str.append(TAGTABLE);
        // for ingress
        String ingress = "control ingress { \n";
        P4Str.append(ingress);
        P4Str.append("if(ethernet.etherType == 0x1111){\n" +
                "apply(tagIdentify);\n" +
                "}\n");
        P4Str.append(formatIngress(firstHeaderList));
        P4Str.append("}\n");
        logger.info(P4Str.toString());
        writeStrtoFile(P4Str.toString(),"./src/main/resources/merge.p4");

    }

    public  static void main(String []args){
        new Main().mergeCode();

    }

}

