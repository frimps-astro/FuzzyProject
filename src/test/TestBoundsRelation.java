//package test;
//
//import main.*;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//
//public class TestBoundsRelation {
//    static Relation R, T;
//    static PrimitiveSetObject source, target;
//    static  Basis truthR, truthT;
//    static int sourceEl, targetEl;
//
//    @BeforeAll
//    static void createRelations(){
//        //load set object
//        source = (PrimitiveSetObject) SetObject.load("src/test/data/bounds/set_object_r_test.xml");
//        target = (PrimitiveSetObject) SetObject.load("src/test/data/bounds/set_object_t_test.xml");
//
//        sourceEl = source.getNumElements();
//        targetEl = target.getNumElements();
//
//        //load heyting object to create basis
//        HeytingAlgebra heytingAlgebraT = HeytingAlgebra.load("src/test/data/composition/BooleanHA.xml");
//        truthT = new Basis(heytingAlgebraT, new int[2][2]);
//
//        //create R relation
//        int[][] rMatrix = { {1,0,0,0,0,0,0},
//                {1,1,0,0,0,0,0},
//                {1,1,1,0,0,0,0},
//                {1,1,1,1,0,0,0},
//                {1,1,0,1,1,1,0},
//                {0,0,0,0,0,1,0},
//                {0,0,1,1,1,0,1} };
//        R = new Relation(source, source, truthT, rMatrix);
//
//        //create T relation
//        int[][] tMatrix = { {0},
//                {0},
//                {1},
//                {1},
//                {1},
//                {0},
//                {0} };
//        T = new Relation(source, target, truthT, tMatrix);
//
//    }
//
//    @Test
//    void testUpperBound(){
//        //Upper Bound result
//        int[][] ubdMatrix= { {1},
//                {1},
//                {0},
//                {0},
//                {0},
//                {0},
//                {0} };
//
//        Relation upperBound = T.leftResidual(R).converse(); //row1=true & row2=false
//        Relation result = new Relation(source, target, truthT, ubdMatrix);
//        System.out.println("Results Matrix: "+result);
//        System.out.println("UpperBound Matrix: "+upperBound);
//        Assertions.assertTrue(compareMatrices(upperBound.getMatrix(), result.getMatrix()));
//    }
//
//    @Test
//    void testLowerBound(){
//        //Lower Bound result
//        int[][] lbdMatrix= { {0},
//                {0},
//                {0},
//                {0},
//                {0},
//                {0},
//                {1} };
//
//        Relation lowerBound = T.leftResidual(R.converse()).converse(); //row1=true & row2=false
//        Relation result = new Relation(source, target, truthT, lbdMatrix);
//        System.out.println("Results Matrix: "+result);
//        System.out.println("LowerBound Matrix: "+lowerBound);
//        Assertions.assertTrue(compareMatrices(lowerBound.getMatrix(), result.getMatrix()));
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
