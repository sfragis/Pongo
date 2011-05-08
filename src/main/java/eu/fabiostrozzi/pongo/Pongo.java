// Pongo.java, created by Fabio Strozzi on May 7, 2011
package eu.fabiostrozzi.pongo;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Pongo helps converting instances from one type to another.
 * <p>
 * It just helps, it's not directly responsible for converting. Writing the
 * conversion algorithm is up to the developer.
 * <p>
 * Pongo uses a minimalistic DSL to help you do that.
 * <p>
 * Note: it's not meant to convert primitive types (int, double, boolean, etc.).
 * 
 * @author Fabio Strozzi
 */
public class Pongo {
    @SuppressWarnings("rawtypes")
    private HashMap<Couple<Class, Class>, IConverter> graph;

    @SuppressWarnings("rawtypes")
    public Pongo() {
        graph = new HashMap<Couple<Class, Class>, Pongo.IConverter>();
    }

    /**
     * This exception is thrown whenever a conversion cannot be undertaken.
     * 
     * @author Fabio Strozzi
     */
    @SuppressWarnings("serial")
    public class ConversionNotSupported extends RuntimeException {
        public ConversionNotSupported() {}

        public ConversionNotSupported(String arg0, Throwable arg1) {
            super(arg0, arg1);
        }

        public ConversionNotSupported(String arg0) {
            super(arg0);
        }

        public ConversionNotSupported(Throwable arg0) {
            super(arg0);
        }
    }

    /**
     * Intermediate class that provides ultimate transformation and filtering
     * methods.
     * 
     * @author Fabio Strozzi
     */
    public class Clay {
        private Object sourceObject;
        @SuppressWarnings("rawtypes")
        private IFilter filter;

        public Clay iff(@SuppressWarnings("rawtypes") IFilter selector) {
            this.filter = selector != null ? selector : Pongo.ANY;
            return this;
        }

        /**
         * Converts the source object into a target object whose type is given
         * in input.
         * 
         * @param <T>
         *            The type of the target object.
         * @param target
         *            The class of the target object.
         * @return An instance of type <code>T</code> that is derived from the
         *         source object.
         */
        @SuppressWarnings("unchecked")
        public <T> T to(Class<T> target) {
            if (sourceObject == null)
                return null;
            if (sourceObject.getClass().isArray())
                return array2object((Object[]) sourceObject, target);
            if (sourceObject instanceof List)
                return list2object((List<Object>) sourceObject, target);
            return object2object(sourceObject, target);
        }

        /**
         * Converts the source object into an array of objects whose type is
         * given in input.
         * 
         * @param <T>
         *            The type of the target objects.
         * @param target
         *            The class of the target objects.
         * @return An array of <code>T</code> objects that is derived from the
         *         source object.
         */
        @SuppressWarnings("unchecked")
        public <T> T[] toArrayOf(Class<T> target) {
            if (sourceObject == null)
                return null;
            if (sourceObject.getClass().isArray())
                return array2array((Object[]) sourceObject, target);
            if (sourceObject instanceof List)
                return list2array((List<Object>) sourceObject, target);
            return null;
        }

        /**
         * Converts the source object into a list of objects whose type is given
         * in input.
         * 
         * @param <T>
         *            The type of the target objects.
         * @param target
         *            The class of the target objects.
         * @return An list of <code>T</code> objects that is derived from the
         *         source object.
         */
        @SuppressWarnings("unchecked")
        public <T> List<T> toListOf(Class<T> target) {
            if (sourceObject == null)
                return null;
            if (sourceObject.getClass().isArray())
                return array2list((Object[]) sourceObject, target);
            if (sourceObject instanceof List)
                return list2list((List<Object>) sourceObject, target);
            return null;
        }

        private <T> T[] list2array(List<Object> sourceObjects, Class<T> target) {
            List<T> targetList = list2list(sourceObjects, target);
            @SuppressWarnings("unchecked") T[] targetArray = (T[]) Array.newInstance(
                    target, targetList.size());
            return targetList.toArray(targetArray);
        }

        @SuppressWarnings("unchecked")
        private <T> T object2object(Object sourceObject, Class<T> target) {
            T targetObject = null;
            if (filter == null || filter == ANY || filter.isConvertible(sourceObject)) {
                Couple<Class<?>, Class<T>> c = new Couple<Class<?>, Class<T>>(
                        sourceObject.getClass(), target);
                @SuppressWarnings("rawtypes") IConverter map = graph.get(c);
                if (map == null)
                    throw new ConversionNotSupported("Missing converter from "
                            + c.getFirst() + " to " + c.getSecond());
                targetObject = instanceOf(target);
                map.convert(sourceObject, targetObject, Pongo.this);
            }
            return targetObject;
        }

        @SuppressWarnings("unchecked")
        private <T> T list2object(List<Object> sourceObjects, Class<T> target) {
            if (sourceObjects.size() == 0)
                return null;
            T targetObject = instanceOf(target);
            for (Object o : sourceObjects) {
                if (filter == null || filter == ANY || filter.isConvertible(o)) {
                    Class<?> sc = o.getClass();
                    Couple<Class<?>, Class<T>> c = new Couple<Class<?>, Class<T>>(sc,
                            target);
                    @SuppressWarnings("rawtypes") IConverter map = graph.get(c);
                    if (map == null)
                        throw new ConversionNotSupported("Missing converter from "
                                + c.getFirst() + " to " + c.getSecond());
                    map.convert(o, targetObject, Pongo.this);
                }
            }
            return targetObject;
        }

        @SuppressWarnings("unchecked")
        private <T> T array2object(Object[] sourceObjects, Class<T> target) {
            if (sourceObjects.length == 0)
                return null;
            T targetObject = instanceOf(target);
            for (Object o : sourceObjects) {
                if (filter == null || filter == ANY || filter.isConvertible(o)) {
                    Class<?> sc = o.getClass();
                    Couple<Class<?>, Class<T>> c = new Couple<Class<?>, Class<T>>(sc,
                            target);
                    @SuppressWarnings("rawtypes") IConverter map = graph.get(c);
                    if (map == null)
                        throw new ConversionNotSupported("Missing converter from "
                                + c.getFirst() + " to " + c.getSecond());
                    map.convert(o, targetObject, Pongo.this);
                }
            }
            return targetObject;
        }

        @SuppressWarnings("unchecked")
        private <T> T[] array2array(Object[] sourceObjects, Class<T> target) {
            T[] targetArray = (T[]) Array.newInstance(target, sourceObjects.length);
            if (sourceObjects.length == 0)
                return targetArray;
            Class<?> sc = sourceObjects.getClass().getComponentType();
            Couple<Class<?>, Class<T>> c = new Couple<Class<?>, Class<T>>(sc, target);
            @SuppressWarnings("rawtypes") IConverter map = graph.get(c);
            if (map == null)
                throw new ConversionNotSupported("Missing converter from " + c.getFirst()
                        + " to " + c.getSecond());
            for (int i = 0; i < sourceObjects.length; i++) {
                Object o = sourceObjects[i];
                if (filter == null || filter == ANY || filter.isConvertible(o)) {
                    T targetObject = instanceOf(target);
                    map.convert(o, targetObject, Pongo.this);
                    targetArray[i] = targetObject;
                }
            }
            return targetArray;
        }

        @SuppressWarnings("unchecked")
        private <T> List<T> array2list(Object[] sourceObjects, Class<T> target) {
            List<T> targetList = new ArrayList<T>(sourceObjects.length);
            if (sourceObjects.length == 0)
                return targetList;
            Class<?> sc = sourceObjects.getClass().getComponentType();
            Couple<Class<?>, Class<T>> c = new Couple<Class<?>, Class<T>>(sc, target);
            @SuppressWarnings("rawtypes") IConverter map = graph.get(c);
            if (map == null)
                throw new ConversionNotSupported("Missing converter from " + c.getFirst()
                        + " to " + c.getSecond());
            for (Object o : sourceObjects) {
                if (filter == null || filter == ANY || filter.isConvertible(o)) {
                    T targetObject = instanceOf(target);
                    map.convert(o, targetObject, Pongo.this);
                    targetList.add(targetObject);
                }
            }
            return targetList;
        }

        @SuppressWarnings("unchecked")
        private <T> List<T> list2list(List<Object> sourceObjects, Class<T> target) {
            List<T> targetList = new ArrayList<T>(sourceObjects.size());
            if (sourceObjects.size() == 0)
                return targetList;
            Class<?> sc = sourceObjects.get(0).getClass();
            Couple<Class<?>, Class<T>> c = new Couple<Class<?>, Class<T>>(sc, target);
            @SuppressWarnings("rawtypes") IConverter map = graph.get(c);
            if (map == null)
                throw new ConversionNotSupported("Missing converter from " + c.getFirst()
                        + " to " + c.getSecond());
            for (Object o : sourceObjects) {
                if (filter == null || filter == ANY || filter.isConvertible(o)) {
                    T targetObject = instanceOf(target);
                    map.convert(o, targetObject, Pongo.this);
                    targetList.add(targetObject);
                }
            }
            return targetList;
        }
    }

    /**
     * Defines a filter on the source object.
     * 
     * @param <S>
     * @author Fabio Strozzi
     */
    public interface IFilter<S> {
        /**
         * Tells whether the given object is to be converted or not.
         * 
         * @param sourceObject
         * @return
         */
        public boolean isConvertible(S sourceObject);
    }

    /**
     * This selector selects any object.
     */
    @SuppressWarnings("rawtypes")
    public static final IFilter ANY = new IFilter() {
        public boolean isConvertible(Object sourceObject) {
            return true;
        }
    };

    /**
     * This selector selects only non-null objects.
     */
    @SuppressWarnings("rawtypes")
    public static final IFilter NOT_NULL = new IFilter() {
        public boolean isConvertible(Object sourceObject) {
            return sourceObject != null;
        }
    };

    /**
     * Declares how an instance of type <code>S</code> is converted in an
     * instance of type <code>T</code>.
     * 
     * @author Fabio Strozzi
     */
    public interface IConverter<S, T> {
        /**
         * Converts the source object of type <code>S</code> to a target object
         * of type <code>T</code>.
         * 
         * @param source
         *            The source object.
         * @param target
         *            The target object.
         * @param pongo
         *            The pongo instance.
         * @return The converted target object.
         */
        public T convert(S source, T target, Pongo pongo);

        /**
         * Gets the source object class.
         * 
         * @return
         */
        public Class<S> getSource();

        /**
         * Gets the target object class.
         * 
         * @return
         */
        public Class<T> getTarget();
    }

    /**
     * Adds a new mapper from an object of type S to an object of type T.
     * 
     * @param <S>
     *            The type of the source object to be converted
     * @param <T>
     *            The type of the destination object to be converted
     * @param map
     *            The conversion mapper
     */
    @SuppressWarnings("rawtypes")
    public <S, T> void addConverter(IConverter<S, T> map) {
        Class<S> s = map.getSource();
        Class<T> t = map.getTarget();
        graph.put(new Couple<Class, Class>(s, t), map);
    }

    /**
     * Declares that the given input object has to be converted.
     * 
     * @param sourceObject
     *            The object to be converted.
     * @return
     */
    public Clay convert(Object sourceObject) {
        Clay c = new Clay();
        c.sourceObject = sourceObject;
        return c;
    }

    /**
     * Creates a new instance of the given class.
     * 
     * @param <T>
     * @param target
     * @return
     */
    private <T> T instanceOf(Class<T> target) {
        try {
            return target.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
