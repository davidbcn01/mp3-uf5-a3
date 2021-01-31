import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import model.Activitat;
import model.Entitat;
import model.GuiaEntitats;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

public class App {
    static final String pathXML = "http://justicia.gencat.cat/web/.content/tramits/entitats/llistatEntitats-federacions.xml";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int option = 0;
        System.out.println("App MP3-UF5-A3");
        System.out.println("-----------------");
        System.out.println("Elige la busqueda");
        System.out.println("1.Buscar las entidades que empiezan por la letra A");
        System.out.println("2.Buscar el numero de entidades que hay en la comarca del Barcelonès");
        System.out.println("3.Buscar totes les dates d'incripció(utilitzant map)");
        System.out.println("4.Buscar las entidades que son de la población Barcelona ordenados alfabeticamente");
        System.out.println("5.Buscar la entidad que fue inscrita primero");
        System.out.println("6.Buscar la entidad que fue inscrita más recientemente");
        System.out.println("7.Buscar los tipos de entidades que hay");
        option = sc.nextInt();

        switch(option) {
            case 1:
                stream1();
                break;
            case 2:
                stream2();
                break;
            case 3:
                stream3();
                break;
            case 4:
                stream4();
                break;
            case 5:
                stream5();
                break;
            case 6:
                stream6();
                break;
            case 7:
                stream7();
                break;
        }
    }


    private static void stream1() {
        URL url = null;
        try {
            url = new URL(pathXML);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        try {
            JAXBContext contextObj = JAXBContext.newInstance(GuiaEntitats.class);
            Unmarshaller unmarshallerObj = contextObj.createUnmarshaller();
            GuiaEntitats guiaEntitats = (GuiaEntitats) unmarshallerObj.unmarshal(url);
            guiaEntitats.getEntitats().getLlistaEntitats().stream().filter(entitat -> entitat.getNom().startsWith("A")).forEach(System.out::println);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
    private static void stream2() {
        URL url = null;
        Long count;
        try {
            url = new URL(pathXML);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        try {
            JAXBContext contextObj = JAXBContext.newInstance(GuiaEntitats.class);
            Unmarshaller unmarshallerObj = contextObj.createUnmarshaller();
            GuiaEntitats guiaEntitats = (GuiaEntitats) unmarshallerObj.unmarshal(url);
            count = guiaEntitats.getEntitats().getLlistaEntitats().stream().filter(entitat -> entitat.getComarca().equals("Barcelonès")).count();
            System.out.println("Numero d'entitats de la comarca del Barcelonès: "+count);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
    private static void stream3() {
        URL url = null;

        try {
            url = new URL(pathXML);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        try {
            JAXBContext contextObj = JAXBContext.newInstance(GuiaEntitats.class);
            Unmarshaller unmarshallerObj = contextObj.createUnmarshaller();
            GuiaEntitats guiaEntitats = (GuiaEntitats) unmarshallerObj.unmarshal(url);
            guiaEntitats.getEntitats().getLlistaEntitats().stream().map(entitat -> entitat.getDataInscripcio()).forEach(System.out::println);

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
    private static void stream4() {
        URL url = null;

        try {
            url = new URL(pathXML);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        try {
            JAXBContext contextObj = JAXBContext.newInstance(GuiaEntitats.class);
            Unmarshaller unmarshallerObj = contextObj.createUnmarshaller();
            GuiaEntitats guiaEntitats = (GuiaEntitats) unmarshallerObj.unmarshal(url);
            guiaEntitats.getEntitats().getLlistaEntitats().stream().filter(entitat -> entitat.getPoblacio().equals("Barcelona")).sorted(Entitat::compareTo).forEach(System.out::println);

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
    private static void stream5() {
        URL url = null;

        try {
            url = new URL(pathXML);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        try {
            JAXBContext contextObj = JAXBContext.newInstance(GuiaEntitats.class);
            Unmarshaller unmarshallerObj = contextObj.createUnmarshaller();
            GuiaEntitats guiaEntitats = (GuiaEntitats) unmarshallerObj.unmarshal(url);
            Optional<Entitat> a = guiaEntitats.getEntitats().getLlistaEntitats().stream().min(Comparator.comparing(Entitat::getDataInscripcio));
            System.out.println(a);

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
    private static void stream6() {
        URL url = null;

        try {
            url = new URL(pathXML);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        try {
            JAXBContext contextObj = JAXBContext.newInstance(GuiaEntitats.class);
            Unmarshaller unmarshallerObj = contextObj.createUnmarshaller();
            GuiaEntitats guiaEntitats = (GuiaEntitats) unmarshallerObj.unmarshal(url);
            Optional<Entitat> a = guiaEntitats.getEntitats().getLlistaEntitats().stream().max(Comparator.comparing(Entitat::getDataInscripcio));
            System.out.println(a);

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
    private static void stream7() {
        URL url = null;
        try {
            url = new URL(pathXML);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        try {
            JAXBContext contextObj = JAXBContext.newInstance(GuiaEntitats.class);
            Unmarshaller unmarshallerObj = contextObj.createUnmarshaller();
            GuiaEntitats guiaEntitats = (GuiaEntitats) unmarshallerObj.unmarshal(url);
            guiaEntitats.getEntitats().getLlistaEntitats().stream().filter(distinctByKey(entitat -> entitat.getTipus())).forEach(entitat -> System.out.println("Tipus: "+entitat.getTipus()));


        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    //Lo que hay a continuación es para poder hacer distinct sobre una varibale de un objeto
    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor)
    {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}

