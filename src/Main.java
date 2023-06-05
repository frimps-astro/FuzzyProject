import main.HeytingAlgebra;
import main.SetObject;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        SetObject setObject = SetObject.load("src/data/set_object.xml");
        System.out.println("SetObject-> "+setObject.toString());

        HeytingAlgebra heytingAlgebra = HeytingAlgebra.load("src/data/heyting.xml");
        System.out.println("Heyting-> "+heytingAlgebra.toString());

        try{
            setObject.save("src/data/set_object_out.xml");
            heytingAlgebra.save("src/data/heyting_out.xml");
        }catch (IOException ex){
            System.out.println(ex.getMessage());
        }
    }
}