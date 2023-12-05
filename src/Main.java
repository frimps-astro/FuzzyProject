import exceptions.TypingException;
import relterm.Relterm;
import relterm.ReltermParser;
import relterm.RelVariable;
import relterm.Converse;
import relterm.LeftResidual;
import relterm.Meet;
import relterm.leaves.Epsi;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public class Main {
    public static void main(String[] args) {
        PrintStream out;
        try {
            out = new PrintStream(System.out,true,"UTF-8");
        } catch (UnsupportedEncodingException ex) {
            out = System.out;
        }

        Epsi epsi = new Epsi();
        Relterm cR = new Meet(new LeftResidual(epsi,new Converse(new RelVariable("R"))),new Converse(epsi));
        out.println(cR);

        ReltermParser reltermParser = ReltermParser.getTypeParser();
        Relterm choiceOfR = reltermParser.parse("ε\\R˘⊓ε˘");

        out.println(choiceOfR);

        try {
            cR.type();
        } catch (TypingException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
        out.println("RelationType of Result: "+cR.getType());
        out.println("Typed Variables: " + cR.getTypedVars());

//        VariableGenerator gen = new VariableGenerator();
//
//        int genCounter = 10;
//        for (int i = 0; i < genCounter; i++) {
//            System.out.println(new TypeVariable(gen.newVarName()));
//        }


//        UserInterface userInterface = UserInterface.getInstance();
//        userInterface.createUI();
//
//        //SETUP USER project
//        Project project = Project.getInstance();
//        project.setName("user");
//        project.setProject();
//
//        //LOAD and DISPLAY the RHO RELATION under the USER project
//        Relation rho = RelationStorage.getInstance().load("rho");
//        RelationDisplay relationDisplay = new RelationDisplay();
//        relationDisplay.setRelation(rho);
//        userInterface.addLeftComponent(relationDisplay);


//        Project project = Project.getInstance();
//        project.setName("user");
//        project.setProject();
//        SetObjectStorage.getInstance().createPowerSetObject("HA^B", "B", "HA");
//        SetObjectStorage.getInstance().createSumSetObject("A+C", "A", "C");
//        SetObjectStorage.getInstance().createProductSetObject("(AxB)xC", "AxB", "C");
//
//        PrintStream out;
//        out = new PrintStream(System.out,true, StandardCharsets.UTF_8);
//
//        SetObject pro = SetObjectStorage.getInstance().load("AxB");
//        Basis ba = BasisStorage.getInstance().load("HA");
//        Relation rho = rho((ProductSetObject) pro, ba);
//        out.println(rho);

//        SetObject A = SetObjectStorage.getInstance().load("A");
//        SetObject B = SetObjectStorage.getInstance().load("B");
//        SetObject C = SetObjectStorage.getInstance().load("C");
//
//        Relation idl = ideal(B, C, ba, 1);
//        idl.setName("ideal");
//        try {
//            RelationStorage.getInstance().save(idl.getName(), idl.toXMLString());
//
//            rho.setName("rho");
//            RelationStorage.getInstance().save(rho.getName(), rho.toXMLString());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }


//
//        out.println(idl);
//
//
//        Relation obj1 = RelationStorage.getInstance().load("rho_new");
//        out.println(obj1.toString());

//
//        SetObject obj2 = SetObject.load("B");
//        out.println(obj2.getName());
//        out.println(obj2.getNumElements());
//        out.println(Arrays.toString(obj2.getElementNames()));
//
//        SetObject prod1 = new ProductSetObject("AxB",obj1,obj2);
//        out.println(prod1.getName());
//        out.println(prod1.getNumElements());
//        out.println(Arrays.toString(prod1.getElementNames()));
//
//        SetObject prod2 = SetObject.load("AxB");
//        out.println(prod2.getName());
//        out.println(prod2.getNumElements());
//        out.println(Arrays.toString(prod2.getElementNames()));
//
//        SetObject prod3 = SetObject.load("(AxB)xA");
//        out.println(prod3.getName());
//        out.println(prod3.getNumElements());
//        out.println(Arrays.toString(prod3.getElementNames()));
//
//        SetObject sum1 = new SumSetObject("A+B", obj1, obj2);
//        out.println(sum1.getName());
//        out.println(sum1.getNumElements());
//        out.println(Arrays.toString(sum1.getElementNames()));
//
//        SetObject sum2 = SetObject.load("(A+B)+A");
//        out.println(sum2.getName());
//        out.println(sum2.getNumElements());
//        out.println(Arrays.toString(sum2.getElementNames()));
//
//        try {
//            obj1.setName("Aout");
//            obj1.save();
//            prod3.setName("(AxB)xAout");
//            prod3.save();
//
//            sum2.setName("(A+B)+Aout");
//            sum2.save();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//        HeytingAlgebra hey = HeytingAlgebra.load("BooleanHA");
//        Basis basis = new Basis(hey,new int[2][2]);
//
//        Relation pi = pi((ProductSetObject) prod2, basis);
//        out.println(pi);
//
//        Relation rho = rho((ProductSetObject) prod2, basis);
//        out.println(rho);
//
//        Relation iota = iota((SumSetObject) sum1, basis);
//        out.println(iota);
//
//        Relation kappa = kappa((SumSetObject) sum1, basis);
//        out.println(kappa);
//
//
//        HeytingAlgebra powerHey = HeytingAlgebra.load("H");
//        Basis H = new Basis(powerHey,new int[2][2]);
//
////        SetObject power = new PowerSetObject(C, H);
//        SetObject power = PowerSetObject.load("Ba^C");
//        out.println(Arrays.toString(power.getElementNames()));
//
//        Relation epsi = epsi((PowerSetObject) power);
//        out.println(epsi);
//
//        SetObject A = SetObject.load("A");
//        HeytingAlgebra booleanHA = HeytingAlgebra.load("BooleanHA");
//        Basis HA = new Basis("HA", booleanHA, new int[2][2]);
//
//        SetObject pow = new PowerSetObject("HA^A", A, HA);
//        out.println(Arrays.toString(pow.getElementNames()));
//
//        Relation ep = epsi((PowerSetObject) pow);
//        out.println(ep);
//
//        try {
//
//            Basis b = ((PowerSetObject) power).getBasis();
//            HeytingAlgebra h = b.getHeytingAlgebra();
//            h.setName("Hout");
//            b.setName("Baout");
//
//            power.setName("Ba^Cout");
//            power.save();
//            pow.save();
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
        
        /*SetObject source1 =  SetObject.load("src/test/data/composition/primitive_set_object_test.xml");
        out.println(source1.toString());

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
        out.println(result1);
        Relation convB = b.converse();
        Relation notG = g.impl(Relation.bot(source1, source1, basis));
        Relation result2 = convB.composition(notG);
        out.println(result2);

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
        out.println(result3);
        out.println(result4);

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
        out.println(resultSyq);

        PrimitiveSetObject setObject = (PrimitiveSetObject) SetObject.load("src/data/primitive_set_object.xml");
        out.println("PrimitiveSetObject-> "+setObject.toString());

        HeytingAlgebra heytingAlgebra = HeytingAlgebra.load("src/data/heyting.xml");
        out.println("Heyting-> "+heytingAlgebra.toString());

        //product set objects
        ProductSetObject productSetObject = (ProductSetObject) SetObject.load("src/data/product_object.xml");
        out.println("\nProductObject: "+productSetObject.toString());

        //test PI Relation
        Relation piRelation = Relation.pi(productSetObject, basis, 0);
        out.println("\nPi: "+piRelation.piToString());

        //test RHO Relation
        Relation rhoRelation = Relation.rho(productSetObject, basis, 0);
        out.println("\nRho: "+rhoRelation.rhoToString());

        try{
            setObject.save("src/data/primitive_set_object_out.xml");
            heytingAlgebra.save("src/data/heyting_out.xml");
            productSetObject.save("src/data/product_object_out.xml");
        }catch (IOException ex){
            out.println(ex.getMessage());
        }*/
//        RelTerm parse = reltermParser.parse("ε\\ε;C⊓ε˘");
        
    }
}