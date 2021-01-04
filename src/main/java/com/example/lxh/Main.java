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

import java.sql.PseudoColumnUsage;
import java.util.*;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private final String COMMONHEADER = "ethernet_t";

    private List<P4JsonParser> parserList = new ArrayList<>();
    private HashMap<String,List<Header> > name2HeaderListMap = new HashMap<>();
    private String declareHeaderVar(Map<String,Header> headers){
        StringBuilder headerVars = new StringBuilder();
        for(String name : headers.keySet()){
            headerVars.append("header "+name+" "+name.split("_")[0]+"\n");
        }
        return headerVars.toString();
    }
    private String formatEthernetParsers(List<Header> firstHeaderList){
        StringBuilder keyValue = new StringBuilder();
        for(int i=0;i<firstHeaderList.size();i++){
            Map<Value,Header> map = firstHeaderList.get(i).getNextsMap();
            for(Map.Entry<Value,Header> item : map.entrySet()){
                keyValue.append("0x").append(item.getKey().toString().substring(0,4)+" : ").append("parse_").append(item.getValue().getName().split("_")[0]).append("\n");
            }
        }
       // logger.info(keyValue.toString());
        return keyValue.toString();

    }
    private String formartParserSelect(Header header){
        StringBuilder builder = new StringBuilder();
        Field field = header.getSelect();
        if(field != null){
            builder.append("return select(lastest.").append(field.getName().toString()).append(")").append(" {\n");
        }
        else{
            return "return ingress;\n";
        }
        for(Map.Entry<Value,Header> item : header.getNextsMap().entrySet()){
            builder.append("0x").append(item.getKey().toString().substring(0,4)+" : ").append("parse_").append(item.getValue().getName().split("_")[0]).append("\n");
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
    public void mergeCode(){
        Config config = new Config();
        List<String> modalityList = config.config();
        if(modalityList.size() == 1){
            logger.error("just one modality,not need to merge!");
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
            logger.info(Header.firestHeader.getName());

            Queue<Header> headerQueue= new LinkedList<Header>();
            headerQueue.add(Header.firestHeader);
            // wide search from the start header
            // for judge whether there are multi header of the same hadername
            while(!headerQueue.isEmpty()){
                Header temp = headerQueue.poll();
                String headerName = temp.getName().toString();
                if(name2HeaderListMap.containsKey(headerName)){
                    name2HeaderListMap.get(headerName).add(temp);
                }else{
                   List<Header> headerList = new ArrayList<>();
                   headerList.add(temp);
                   name2HeaderListMap.put(headerName,headerList);
                }

                Map<Value,Header> nextHeadersMap = temp.getNextsMap();
                for(Header header :nextHeadersMap.values()){
                    headerQueue.add(header);
                }
            }
        }


        //echo to a new P4 file
        StringBuilder P4Str = new StringBuilder();
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

                continue;
            }
            else{
                if(headers.size() > 1){ // this is the case that there are same name of multi headers;
                    for(int j=0;j<headers.size();j++){
                        headers.get(j).setName(headerName+"_"+String.valueOf(j));
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
        // for headers
        P4Str.append(declareHeaderVar(name2HeaderMap));
        //logger.info(P4Str.toString());
        // for parsers
        final String parserStart = new String("parser start {\n" +
                "    return parse_ethernet;\n" +
                "}");
        String parse_ethernet = new String("parser parse_ethernet {\n" +
                "    extract(ethernet);\n" +
                "    return select(latest.etherType) {\n" +
                formatEthernetParsers(firstHeaderList)+
                "        default: ingress;\n" +
                "    }\n" +
                "}\n");
        P4Str.append(parse_ethernet);
        //logger.info(parse_ethernet);
        for(int i=0;i<firstHeaderList.size();i++){
            Header header = firstHeaderList.get(i);
            for(Header head : header.getNextsMap().values()){
                formatParser(head,P4Str);
            }

        }
        logger.info(P4Str.toString());

    }

    public  static void main(String []args){
        new Main().mergeCode();

    }

}

