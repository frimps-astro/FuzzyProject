package test;

import main.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


public class TestPiRhoRelations {
    static ProductObject productObject;
    static Basis basis;
    @BeforeAll
    static void setUp(){
        HeytingAlgebra heyting = new HeytingAlgebra();
        HeytingAlgebra heytingAlgebra = heyting.load("src/test/data/composition/BooleanHA.xml", "heyting");
        basis = new Basis(heytingAlgebra, new int[2][2]);
        ProductObject product = new ProductObject();

        productObject = product.load("src/test/data/product_object.xml", "product");
    }

    @Test
    void testPiRelation(){
        int[][] piMatrix = {
                {1,0,},
                {1,0},
                {1,0},
                {0,1},
                {0,1},
                {0,1},
        };

        Relation piRelation = Relation.pi(productObject, basis, 0);

        System.out.println("Pi: "+piRelation.piToString());

        Assertions.assertTrue(comparePiMatrices(piRelation.getMatrix(), piMatrix, piRelation));
    }

    @Test
    void testRhoRelation(){
        int[][] rhoMatrix ={
                {1,0,0},
                {0,1,0},
                {0,0,1},
                {1,0,0},
                {0,1,0},
                {0,0,1},
        };

        Relation rhoRelation = Relation.rho(productObject, basis, 0);

        System.out.println("Rho: "+rhoRelation.rhoToString());

        Assertions.assertTrue(compareRhoMatrices(rhoRelation.getMatrix(), rhoMatrix, rhoRelation));
    }

     private boolean comparePiMatrices(int[][] matrixA, int[][] matrixB, Relation relation) {
        boolean flag = true;

        for (int i = 0; i < relation.getLeftNum() * relation.getRightNum(); i++) {
            for (int j = 0; j < relation.getLeftNum(); j++) {
                if (matrixA[i][j] != matrixB[i][j]) {
                    flag = false;
                    break;
                }
            }
        }

        return flag;
    }

    private boolean compareRhoMatrices(int[][] matrixA, int[][] matrixB, Relation relation) {
        boolean flag = true;

        for (int i = 0; i < relation.getLeftNum() * relation.getRightNum(); i++) {
            for (int j = 0; j < relation.getRightNum(); j++) {
                if (matrixA[i][j] != matrixB[i][j]) {
                    flag = false;
                    break;
                }
            }
        }

        return flag;
    }
}
