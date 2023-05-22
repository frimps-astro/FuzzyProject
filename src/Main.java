import java.io.IOException;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        SetObject setObject = SetObject.load("src/data/set_object.xml");
        System.out.println("SetObject-> "+setObject.toXMLString());

        HeytingAlgebra heytingAlgebra = HeytingAlgebra.load("src/data/heyting.xml");
        System.out.println("Heyting-> "+heytingAlgebra.toXMLString());

        try{
            setObject.save("src/data/set_object_out.xml");
            heytingAlgebra.save("src/data/heyting_out.xml");
        }catch (IOException ex){
            System.out.println(ex.getMessage());
        }


    }
}