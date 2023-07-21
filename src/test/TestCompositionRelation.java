//package test;
//
//import main.*;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//
//public class TestCompositionRelation {
//    static Relation F, M, B, G, P;
//    static PrimitiveSetObject source, target;
//    static  Basis basis;
//    static int sourceEl, targetEl;
//
//    @BeforeAll
//    static void createRelations(){
//        //load set object
//        source = (PrimitiveSetObject) SetObject.load("src/test/data/composition/primitive_set_object_test.xml");
//        target = source;
//
//        sourceEl = source.getNumElements();
//        targetEl = target.getNumElements();
//
//        //load heyting object to create basis
//        HeytingAlgebra heytingAlgebra = HeytingAlgebra.load("src/test/data/composition/BooleanHA.xml");
//        basis = new Basis(heytingAlgebra, new int[2][2]);
//
//        //create F relation
//        int[][] fMatrix = { {0,0,0,1,0,0,0,0,0},
//                {0,0,0,0,0,0,0,0,0},
//                {0,0,0,0,1,1,0,0,0},
//                {0,0,0,0,0,0,0,0,0},
//                {0,0,0,0,0,0,0,0,0},
//                {0,0,0,0,0,0,0,1,1},
//                {0,0,0,0,0,0,0,0,0},
//                {0,0,0,0,0,0,0,0,0},
//                {0,0,0,0,0,0,0,0,0} };
//        F = new Relation(source, target, basis, fMatrix);
//
//        //create M relation
//        int[][] mMatrix = { {0,0,0,0,0,0,0,0,0},
//                {0,0,0,0,0,0,0,0,0},
//                {0,0,0,0,0,0,0,0,0},
//                {0,0,0,0,0,0,0,1,1},
//                {0,0,0,0,0,0,1,0,0},
//                {0,0,0,0,0,0,0,0,0},
//                {0,0,0,0,0,0,0,0,0},
//                {0,0,0,0,0,0,0,0,0},
//                {0,0,0,0,0,0,0,0,0} };
//        M = new Relation(source, target, basis, mMatrix);
//
//        //create B relation
//        int[][] bMatrix = { {0,0,0,0,0,0,0,0,0},
//                {0,0,1,0,0,0,0,0,0},
//                {0,1,0,0,0,0,0,0,0},
//                {0,0,0,0,0,0,0,0,0},
//                {0,0,0,0,0,0,0,0,0},
//                {0,0,0,0,1,0,0,0,0},
//                {0,0,0,0,0,0,0,0,0},
//                {0,0,0,0,0,0,0,0,1},
//                {0,0,0,0,0,0,0,1,0} };
//        B = new Relation(source, target, basis, bMatrix);
//
//        //create G relation
//        int[][] gMatrix = { {0,0,0,0,0,1,0,0,0},
//                {0,0,0,0,1,1,0,0,0},
//                {0,0,0,0,0,0,0,0,0},
//                {0,0,0,0,0,0,0,0,0},
//                {0,0,0,0,0,0,0,0,0},
//                {0,0,0,0,0,0,1,0,0},
//                {0,0,0,0,0,0,0,0,0},
//                {0,0,0,0,0,0,0,0,0},
//                {0,0,0,0,0,0,0,0,0} };
//        G = new Relation(source, target, basis, gMatrix);
//
//        //create P Relation (F join M)
//        P = F.join(M);
//    }
//
//    @Test
//    void testBcomposedP(){
//        //B composed P result
//        int[][] bpMatrix = { {0,0,0,0,0,0,0,0,0},
//                {0,0,0,0,1,1,0,0,0},
//                {0,0,0,0,0,0,0,0,0},
//                {0,0,0,0,0,0,0,0,0},
//                {0,0,0,0,0,0,0,0,0},
//                {0,0,0,0,0,0,1,0,0},
//                {0,0,0,0,0,0,0,0,0},
//                {0,0,0,0,0,0,0,0,0},
//                {0,0,0,0,0,0,0,0,0} };
//
//        Relation BcomposeP = B.composition(P); //row1=true & row2=false
//        Relation result = new Relation(source, target, basis, bpMatrix);
//        System.out.println("Results Matrix: "+result);
//        System.out.println("Composed Matrix: "+BcomposeP);
//        Assertions.assertTrue(compareMatrices(BcomposeP.getMatrix(), result.getMatrix()));
//    }
//
//    @Test
//    void testConverseBcomposeGcomplement(){
//        //B converse Compose G complement result
//        int[][] bgMatrix = { {0,0,0,0,0,0,0,0,0},
//                {1,1,1,1,1,1,1,1,1},
//                {1,1,1,1,0,0,1,1,1},
//                {0,0,0,0,0,0,0,0,0},
//                {1,1,1,1,1,1,0,1,1},
//                {0,0,0,0,0,0,0,0,0},
//                {0,0,0,0,0,0,0,0,0},
//                {1,1,1,1,1,1,1,1,1},
//                {1,1,1,1,1,1,1,1,1} };
//
//        Relation converseBcomposeGcomplement = B.converse().composition(G.complement());
//        Relation result = new Relation(source, target, basis, bgMatrix);
//        System.out.println("Results Matrix: "+result);
//        System.out.println("Composed Matrix: "+converseBcomposeGcomplement);
//        Assertions.assertTrue(compareMatrices(converseBcomposeGcomplement.getMatrix(), result.getMatrix()));
//
//
//    }
//
//    static boolean compareMatrices(int[][] matrixA, int[][] matrixB){
//        boolean flag = true;
//
//        for (int i = 0; i < sourceEl; i++) {
//            for (int j = 0; j < targetEl; j++) {
//                if (matrixA[i][j] != matrixB[i][j]){
//                    flag = false;
//                    break;
//                }
//            }
//        }
//
//        return flag;
//    }
//}
