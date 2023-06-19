package test;

import main.Basis;
import main.HeytingAlgebra;
import main.Relation;
import main.SetObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class TestSymmetricQuotient {
    static Relation A, B;
    static SetObject source, target, intersection;
    static  Basis basis;

    @BeforeAll
    static void createRelations(){
        //load set object
        source = SetObject.load("src/test/data/syq/set_object_A_test.xml");
        target = SetObject.load("src/test/data/syq/set_object_B_test.xml");
        intersection = SetObject.load("src/test/data/syq/set_object_intersection_test.xml");

        //load heyting object to create basis
        HeytingAlgebra heytingAlgebra = HeytingAlgebra.load("src/test/data/composition/BooleanHA.xml");
        basis = new Basis(heytingAlgebra, new int[2][2]);

        //create A relation
        int[][] aMatrix = {
                {0,1,1,0,1,1},
                {0,0,0,1,0,0},
                {0,1,0,0,1,1},
                {0,0,1,0,0,0},
                {0,0,0,0,1,0}};

        A = new Relation(intersection, source, basis, aMatrix);

        //create B relation
        int[][] bMatrix = {
                {0,1,1,0,0,0,1,1,0},
                {1,0,0,0,1,0,0,0,0},
                {0,1,1,0,0,0,1,1,0},
                {0,0,0,0,0,0,0,0,0},
                {0,0,1,0,0,0,0,1,0}};

        B = new Relation(intersection, target, basis, bMatrix);

    }

    @Test
    void testASyqB(){
        int[][] resultMatrix = {
                {0,0,0,1,0,1,0,0,1},
                {0,1,0,0,0,0,1,0,0},
                {0,0,0,0,0,0,0,0,0},
                {1,0,0,0,1,0,0,0,0},
                {0,0,1,0,0,0,0,1,0},
                {0,1,0,0,0,0,1,0,0}};

        Relation AsyqB = A.syq(B);
        Relation result = new Relation(source, target, basis, resultMatrix);
        System.out.println("Results Matrix: "+result);
        System.out.println("Composed Matrix: "+AsyqB);
        Assertions.assertTrue(compareMatrices(AsyqB.getMatrix(), result.getMatrix()));
    }

    static boolean compareMatrices(int[][] matrixA, int[][] matrixB){
        boolean flag = true;

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 9; j++) {
                if (matrixA[i][j] != matrixB[i][j]){
                    flag = false;
                    break;
                }
            }
        }

        return flag;
    }
}
