import main.*;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        PrimitiveSetObject primitiveSetObject = new PrimitiveSetObject();
        HeytingAlgebra heytingAlgebra = new HeytingAlgebra();
        ProductObject productObject = new ProductObject();

        PrimitiveSetObject source1 = primitiveSetObject.load("src/test/data/composition/set_object_test.xml", "primitive");
        HeytingAlgebra ha = heytingAlgebra.load("src/test/data/composition/BooleanHA.xml", "heyting");
        Basis basis = new Basis(ha,new int[2][2]);


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

        PrimitiveSetObject source2 = primitiveSetObject.load("src/test/data/bounds/set_object_r_test.xml", "primitive");
        PrimitiveSetObject unit = primitiveSetObject.load("src/test/data/bounds/set_object_t_test.xml", "primitive");
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
        PrimitiveSetObject sourceSyq = primitiveSetObject.load("src/test/data/syq/set_object_A_test.xml", "primitive");
        PrimitiveSetObject targetSyq= primitiveSetObject.load("src/test/data/syq/set_object_B_test.xml", "primitive");
        PrimitiveSetObject intersection = primitiveSetObject.load("src/test/data/syq/set_object_intersection_test.xml", "primitive");

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

        PrimitiveSetObject setObject = primitiveSetObject.load("src/data/set_object.xml", "primitive");
        System.out.println("PrimitiveSetObject-> "+setObject.toString());

        HeytingAlgebra hey = heytingAlgebra.load("src/data/heyting.xml", "heyting");
        System.out.println("Heyting-> "+hey.toString());

        //product set objects
        ProductObject product = productObject.load("src/data/product_object.xml", "product");
        System.out.println("\nProductObject: "+product.toString());

        //test PI Relation
        Relation piRelation = Relation.pi(product, basis, 0);
        System.out.println("\nPi: "+piRelation.piToString());

        //test RHO Relation
        Relation rhoRelation = Relation.rho(product, basis, 0);
        System.out.println("\nRho: "+rhoRelation.rhoToString());

        try{
            setObject.save("src/data/set_object_out.xml");
            hey.save("src/data/heyting_out.xml");
            product.save("src/data/product_object_out.xml");
        }catch (IOException ex){
            System.out.println(ex.getMessage());
        }
    }
}