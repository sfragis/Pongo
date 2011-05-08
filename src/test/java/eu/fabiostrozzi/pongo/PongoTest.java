// PongoTest.java, created by Fabio Strozzi on May 8, 2011
package eu.fabiostrozzi.pongo;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import eu.fabiostrozzi.pongo.Pongo.IConverter;

/**
 * Test unit for Pongo.
 * 
 * @author Fabio Strozzi
 */
public class PongoTest {
    public static class A {
        private boolean b;
        private int i;
        private String s;
        private Object o;
        private C c;
    }

    public static class B {
        private String s;
        private boolean b;
        private int i;
        private Object o;
        private D d;
    }

    public static class C {
        private String s;
    }

    public static class D {
        private String s;
    }

    /**
     * Tests conversion from object to object.
     */
    @Test
    public void testObjectConversion() {
        A a = new A();
        a.s = "pippo";
        a.b = true;
        a.i = 314;
        a.o = new Object();
        a.c = new C();
        a.c.s = "pluto";

        Pongo pongo = new Pongo();
        pongo.addConverter(aToBMapper());
        pongo.addConverter(cToDMapper());

        B b = pongo.convert(a).to(B.class);
        Assert.assertNotNull(b);
        Assert.assertEquals(a.b, b.b);
        Assert.assertEquals(a.i, b.i);
        Assert.assertEquals(a.s, b.s);
        Assert.assertEquals(a.o, b.o);
        Assert.assertNotNull(b.d);
        Assert.assertEquals(a.c.s, b.d.s);
    }

    /**
     * Tests conversion from array to array.
     */
    @Test
    public void testArrayConversion() {
        A a = new A();
        a.s = "pippo";
        a.b = true;
        a.i = 314;
        a.o = new Object();
        a.c = new C();
        a.c.s = "pluto";

        Pongo pongo = new Pongo();
        pongo.addConverter(aToBMapper());
        pongo.addConverter(cToDMapper());

        A[] as = new A[10];
        for (int i = 0; i < as.length; i++)
            as[i] = a;

        B[] bs = pongo.convert(as).toArrayOf(B.class);
        Assert.assertNotNull(bs);
        Assert.assertEquals(as.length, bs.length);
        for (int i = 0; i < as.length; i++) {
            Assert.assertEquals(as[i].b, bs[i].b);
            Assert.assertEquals(as[i].i, bs[i].i);
            Assert.assertEquals(as[i].s, bs[i].s);
            Assert.assertEquals(as[i].c.s, bs[i].d.s);
        }
    }

    /**
     * Tests conversion from list to list.
     */
    @Test
    public void testListConversion() {
        A a = new A();
        a.s = "pippo";
        a.b = true;
        a.i = 314;
        a.o = new Object();
        a.c = new C();
        a.c.s = "pluto";

        Pongo pongo = new Pongo();
        pongo.addConverter(aToBMapper());
        pongo.addConverter(cToDMapper());

        List<A> as = new ArrayList<A>(10);
        for (int i = 0; i < 10; i++)
            as.add(a);

        List<B> bs = pongo.convert(as).toListOf(B.class);
        Assert.assertNotNull(bs);
        Assert.assertEquals(as.size(), bs.size());
        for (int i = 0; i < as.size(); i++) {
            Assert.assertEquals(as.get(i).b, bs.get(i).b);
            Assert.assertEquals(as.get(i).i, bs.get(i).i);
            Assert.assertEquals(as.get(i).s, bs.get(i).s);
            Assert.assertEquals(as.get(i).c.s, bs.get(i).d.s);
        }
    }

    /**
     * Tests conversion from array to list.
     */
    @Test
    public void testArrayToListConversion() {
        A a = new A();
        a.s = "pippo";
        a.b = true;
        a.i = 314;
        a.o = new Object();
        a.c = new C();
        a.c.s = "pluto";

        Pongo pongo = new Pongo();
        pongo.addConverter(aToBMapper());
        pongo.addConverter(cToDMapper());

        A[] as = new A[10];
        for (int i = 0; i < as.length; i++)
            as[i] = a;

        List<B> bs = pongo.convert(as).toListOf(B.class);
        Assert.assertNotNull(bs);
        Assert.assertEquals(as.length, bs.size());
        for (int i = 0; i < as.length; i++) {
            Assert.assertEquals(as[i].b, bs.get(i).b);
            Assert.assertEquals(as[i].i, bs.get(i).i);
            Assert.assertEquals(as[i].s, bs.get(i).s);
            Assert.assertEquals(as[i].c.s, bs.get(i).d.s);
        }
    }

    /**
     * Tests conversion from list to list.
     */
    @Test
    public void testListToArrayConversion() {
        A a = new A();
        a.s = "pippo";
        a.b = true;
        a.i = 314;
        a.o = new Object();
        a.c = new C();
        a.c.s = "pluto";

        Pongo pongo = new Pongo();
        pongo.addConverter(aToBMapper());
        pongo.addConverter(cToDMapper());

        List<A> as = new ArrayList<A>(10);
        for (int i = 0; i < 10; i++)
            as.add(a);

        B[] bs = pongo.convert(as).toArrayOf(B.class);
        Assert.assertNotNull(bs);
        Assert.assertEquals(as.size(), bs.length);
        for (int i = 0; i < as.size(); i++) {
            Assert.assertEquals(as.get(i).b, bs[i].b);
            Assert.assertEquals(as.get(i).i, bs[i].i);
            Assert.assertEquals(as.get(i).s, bs[i].s);
            Assert.assertEquals(as.get(i).c.s, bs[i].d.s);
        }
    }

    /**
     * @return
     */
    private IConverter<C, D> cToDMapper() {
        return new IConverter<C, D>() {

            public D convert(C c, D d, Pongo p) {
                d.s = c.s;
                return d;
            }

            public Class<C> getSource() {
                return C.class;
            }

            public Class<D> getTarget() {
                return D.class;
            }

        };
    }

    /**
     * @return
     */
    private IConverter<A, B> aToBMapper() {
        return new IConverter<A, B>() {

            public B convert(A a, B b, Pongo p) {
                b.b = a.b;
                b.i = a.i;
                b.s = a.s;
                b.o = a.o;
                b.d = p.convert(a.c).to(D.class);
                return b;
            }

            public Class<A> getSource() {
                return A.class;
            }

            public Class<B> getTarget() {
                return B.class;
            }
        };
    }

    /**
     * Tests conversion from array to array given an iff condition.
     */
    @Test
    public void testIffNotNullCondition() {
        A a = new A();
        a.s = "pippo";
        a.b = true;
        a.i = 314;
        a.o = new Object();
        a.c = new C();
        a.c.s = "pluto";

        Pongo pongo = new Pongo();
        pongo.addConverter(aToBMapper());
        pongo.addConverter(cToDMapper());

        A[] as = new A[5];
        as[0] = as[1] = as[4] = a;
        as[2] = as[3] = null;

        B[] bs = pongo.convert(as).iff(Pongo.NOT_NULL).toArrayOf(B.class);
        Assert.assertNotNull(bs);
        Assert.assertEquals(as.length, bs.length);
        Assert.assertNotNull(bs[0]);
        Assert.assertNotNull(bs[1]);
        Assert.assertNotNull(bs[4]);
        Assert.assertNull(bs[2]);
        Assert.assertNull(bs[3]);
    }
}
