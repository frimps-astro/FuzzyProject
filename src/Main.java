import main.*;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        SetObject source1 =  SetObject.load("src/test/data/composition/primitive_set_object_test.xml");
        System.out.println(source1.toString());

        HeytingAlgebra hey = HeytingAlgebra.load("src/test/data/composition/BooleanHA.xml");
        Basis basis = new Basis(hey,new int[2][2]);

        int[][] bmatrix = { {0,0,0,0,0,0,0,0,0},
                {0,0,1,0,0,0,0,0,0},
                {0,1,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,1,0,0,0,0},
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,1},
                {0,0,0,0,0,0,0,1,0} };
        Relation b = new Relation(source1,source1,basis,bmatrix);
        int[][] fmatrix = { {0,0,0,1,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,1,1,0,0,0},
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,1,1},
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0} };
        Relation f = new Relation(source1,source1,basis,fmatrix);
        int[][] mmatrix = { {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,1,1},
                {0,0,0,0,0,0,1,0,0},
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0} };
        Relation m = new Relation(source1,source1,basis,mmatrix);
        int[][] gmatrix = { {0,0,0,0,0,1,0,0,0},
                {0,0,0,0,1,1,0,0,0},
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,1,0,0},
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0} };
        Relation g = new Relation(source1,source1,basis,gmatrix);
        Relation p = f.join(m);
        Relation result1 = b.composition(p);
        System.out.println(result1);
        Relation convB = b.converse();
        Relation notG = g.impl(Relation.bot(source1, source1, basis));
        Relation result2 = convB.composition(notG);
        System.out.println(result2);

        SetObject source2 = SetObject.load("src/test/data/bounds/set_object_r_test.xml");
        SetObject unit =  SetObject.load("src/test/data/bounds/set_object_t_test.xml");
        int[][] rmatrix = { {1,0,0,0,0,0,0},
                {1,1,0,0,0,0,0},
                {1,1,1,0,0,0,0},
                {1,1,1,1,0,0,0},
                {1,1,0,1,1,1,0},
                {0,0,0,0,0,1,0},
                {0,0,1,1,1,0,1} };
        Relation r = new Relation(source2,source2,basis,rmatrix);
        int[][] tmatrix = { {0},
                {0},
                {1},
                {1},
                {1},
                {0},
                {0} };
        Relation t = new Relation(source2,unit,basis,tmatrix);
        Relation result3 = r.converse().rightResidual(t.converse()).converse(); // or t.leftResidual(r).converse();
        Relation result4 = r.rightResidual(t.converse()).converse(); // or t.leftResidual(r.converse()).converse();
        System.out.println(result3);
        System.out.println(result4);

        //Syq(A, B)
        SetObject sourceSyq =  SetObject.load("src/test/data/syq/set_object_A_test.xml");
        SetObject targetSyq= SetObject.load("src/test/data/syq/set_object_B_test.xml");
        SetObject intersection = SetObject.load("src/test/data/syq/set_object_intersection_test.xml");

        //create A relation
        int[][] aMatrix = {
                {0,1,1,0,1,1},
                {0,0,0,1,0,0},
                {0,1,0,0,1,1},
                {0,0,1,0,0,0},
                {0,0,0,0,1,0}};
        Relation A = new Relation(intersection, sourceSyq, basis, aMatrix);

        //create B relation
        int[][] bMatrix = {
                {0,1,1,0,0,0,1,1,0},
                {1,0,0,0,1,0,0,0,0},
                {0,1,1,0,0,0,1,1,0},
                {0,0,0,0,0,0,0,0,0},
                {0,0,1,0,0,0,0,1,0}};
        Relation B = new Relation(intersection, targetSyq, basis, bMatrix);

        Relation resultSyq = A.syq(B);
        System.out.println(resultSyq);

        PrimitiveSetObject setObject = (PrimitiveSetObject) SetObject.load("src/data/primitive_set_object.xml");
        System.out.println("PrimitiveSetObject-> "+setObject.toString());

        HeytingAlgebra heytingAlgebra = HeytingAlgebra.load("src/data/heyting.xml");
        System.out.println("Heyting-> "+heytingAlgebra.toString());

        //product set objects
        ProductSetObject productSetObject = (ProductSetObject) SetObject.load("src/data/product_object.xml");
        System.out.println("\nProductObject: "+productSetObject.toString());

        //test PI Relation
        Relation piRelation = Relation.pi(productSetObject, basis, 0);
        System.out.println("\nPi: "+piRelation.piToString());

        //test RHO Relation
        Relation rhoRelation = Relation.rho(productSetObject, basis, 0);
        System.out.println("\nRho: "+rhoRelation.rhoToString());

        try{
            setObject.save("src/data/primitive_set_object_out.xml");
            heytingAlgebra.save("src/data/heyting_out.xml");
            productSetObject.save("src/data/product_object_out.xml");
        }catch (IOException ex){
            System.out.println(ex.getMessage());
        }
    }
}