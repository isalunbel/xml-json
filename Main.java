package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;




public class Main {
    public static void main(String[] args) throws IOException {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";

        List<Employee> list1 = parseXML("data.xml");
        List<Employee> list = parseCSV(columnMapping, fileName);String json = listToJson(list);
        String json1 = listToJson(list1);writeString(json, "data.json");


        writeString(json1, "data2.json");

        for (Employee employee : list1) {
            System.out.println(employee);
        }
    }
    public static List<Employee> parseCSV(String[] columnMapping, String fileName) {List<Employee> list = null;
       try (Reader reader = new FileReader(fileName)) {
           ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
           strategy.setType(Employee.class);
           strategy.setColumnMapping(columnMapping);
           CsvToBean<Employee> csvToBean = new CsvToBeanBuilder<Employee>(reader)
                   .withMappingStrategy(strategy)
                   .build();
          list = csvToBean.parse();
       } catch (Exception e) {
           e.printStackTrace();
       }
       return list;}



    public static List<Employee> parseXML(String filename) {
        List<Employee> list1 = new ArrayList<>();

        try {
            File xmlFile = new File(filename);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);

            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("employee");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    int id = Integer.parseInt(element.getElementsByTagName("id").item(0).getTextContent());
                    String firstName = element.getElementsByTagName("firstName").item(0).getTextContent();
                    String lastName = element.getElementsByTagName("lastName").item(0).getTextContent();
                    String country = element.getElementsByTagName("country").item(0).getTextContent();
                    int age = Integer.parseInt(element.getElementsByTagName("age").item(0).getTextContent());

                    Employee employee = new Employee(id, firstName, lastName, country, age);
                    list1.add(employee);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list1;
    }





    public static String listToJson(List<Employee> list) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        Type listType = new TypeToken<List<Employee>>() {}.getType();
        String json = gson.toJson(list, listType);

        return json;
    }


    public static void writeString(String json, String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(json);
            System.out.println("JSON записан в файл " + fileName);
        } catch (IOException e) {
            System.out.println("Ошибка при записи JSON в файл.");
            e.printStackTrace();
        }
    }
}
