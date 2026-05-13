package com.rakbank.studentservice.utils;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.io.File;
import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.NoSuchElementException;


/**
 * utility class provides array-related functionalities.
 * <p>
 * the APIs in this class follow the convention of other utilities
 * classes (such as Util, StringUtil) which all the methods are
 * defined as static, instead of getting an instance and call the
 * underlying APIs.
 *
 * @author Aldmour Hamzeh (hamzeh.aldmour@acabes.com)
 * @version 1.0, Mar 8, 2026 at 4:04:19 PM
 * @editor IDEA
 */
public class ArrayUtil {
    // -------------------------------------------------------------- length ---

    /**
     * get length of specified source object array.
     * if the array is null, this method will return -1.
     * so to deal with non-empty object array, we can write something like:
     * if (ArrayUtil.length(array) > 0) {...}
     * to deal with empty array (either length==0 or null), write:
     * if (ArrayUtil.length(array) <= 0) {...}
     *
     * @param    array    source object array to be handled.
     * @return length of the specified object array. -1 if src is null.
     */
    public static int length(Object[] array) {
        return (array != null) ? array.length : -1;
    }


    /**
     * @param    array    array to be handled.
     * @return length of the array; -1 if it is null
     * @see    #length(Object[])
     */
    public static int length(int[] array) {
        return (array != null) ? array.length : -1;
    }


    /**
     * @param    array    array to be handled.
     * @return length of the array; -1 if it is null
     * @see    #length(Object[])
     */
    public static int length(long[] array) {
        return (array != null) ? array.length : -1;
    }


    // ---------------------------------------------------------------- null ---

    /**
     * return true if given array has null elements; false otherwise.
     *
     * @param    array        array of object to be checked.
     * @return true if array has null elements.
     */
    public static boolean hasNull(Object[] array) {
        for (int i = length(array) - 1; i >= 0; i--) {
            if (array[i] == null) {
                return true;
            }
        }
        return false;
    }


    /**
     * return first non-null element from given array.
     *
     * @param array array of elements to be handled.
     * @return first non-null element; or null if not found;
     */
    public static Object firstNonNull(Object[] array) {
        for (int i = 0; i < length(array); i++) {
            if (array[i] != null) {
                return array[i];
            }
        }
        return null;
    }


    /**
     * a constant empty object array.
     */
    public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

    /**
     * a constant empty string array.
     */
    public static final String[] EMPTY_STRING_ARRAY = new String[0];

    public static final int[] EMPTY_INT_ARRAY = new int[0];

    /**
     * return a non-null array instance.
     *
     * @param array array to be handled.
     * @return original array if it is not null; otherwise an empty Object array.
     */
    public static Object[] noNull(Object[] array) {
        return (array != null) ? array : EMPTY_OBJECT_ARRAY;
    }


    /**
     * return a non-null array instance.
     *
     * @param array array to be handled.
     * @return original array if it is not null; otherwise an empty String array.
     */
    public static String[] noNull(String[] array) {
        return (array != null) ? array : EMPTY_STRING_ARRAY;
    }

    public static int[] noNull(int[] array) {
        return (array != null) ? array : EMPTY_INT_ARRAY;
    }

    public static File[] noNull(File[] array) {
        return (array != null) ? array : new File[0];
    }


    // ------------------------------------------------------------ contains ---

    /**
     * check if given target is being contained in given array.
     * following rules applied:
     * 1.	if target is null; return true if array contains null element;
     * 2.	for non-null target, return true if there exists an element
     * such that target.equals(element) is true;
     *
     * @param    array        array of object to be handled.
     * @param    target        target object to be checked.
     * @return true if array contains at least one element equals to target.
     */
    public static boolean contains(Object[] array, Object target) {
        // special handling for null
        if (target == null) {
            return hasNull(array);
        }

        for (int i = length(array) - 1; i >= 0; i--) {
            if (target.equals(array[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param    array        array of object to be handled.
     * @param    target        target object to be checked.
     * @return true if array contains at least one element equals to target.
     */
    public static boolean contains(int[] array, int target) {
        for (int i = length(array) - 1; i >= 0; i--) {
            if (target == array[i]) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param    array        array of object to be handled.
     * @param    target        target object to be checked.
     * @return true if array contains at least one element equals to target.
     */
    public static boolean contains(long[] array, long target) {
        for (int i = length(array) - 1; i >= 0; i--) {
            if (target == array[i]) {
                return true;
            }
        }
        return false;
    }

    // --------------------------------------------------------------- equal ---

    /**
     * return true if all elements in given array are equal.
     * null elements are considering to be equal to each other.
     *
     * @param array array to be checked.
     * @return true iff all elements in given array are equal.
     */
    public static boolean isAllEqual(Object[] array) {
        if (length(array) <= 1) {
            return true;
        }
        Object first = array[0];
        for (int i = 1; i < array.length; i++) {
            if (((first == null) && (array[i] != null)) ||
                    ((first != null) && (array[i] == null))) {
                return false;
            }
            if ((first == null) && (array[i] == null)) {
                continue;
            }
            if (!array[i].equals(first)) {
                return false;
            }
        }
        return true;
    }


    // ------------------------------------------------------------- reverse ---

    /**
     * reverse the order of given objects.
     *
     * @param    objects        array of objects to be handled.
     */
    public static void reverse(Object[] objects) {
        if (objects == null)
            return;

        int size = objects.length;
        for (int i = 0, mid = size >> 1, j = size - 1; i < mid; i++, j--) {
            Object tmp = objects[i];
            objects[i] = objects[j];
            objects[j] = tmp;
        }
    }

    /**
     * get index of val in an array
     *
     * @param array source array
     * @param val   target element
     * @return index of given value in array.
     */
    public static int indexOf(Object[] array, Object val) {
        for (int i = 0; i < array.length; i++) {
            if (val != null && val.equals(array[i])) return i;
        }
        return -1;
    }

    /**
     * get index of string val in an array, string comparison is not case sensitive
     *
     * @param array source string  array
     * @param val   string target element
     * @return index of given string value in array.
     */
    public static int indexOfIgnoreCase(String[] array, String val) {
        for (int i = 0; i < array.length; i++) {
            if (val != null && val.equalsIgnoreCase(array[i])) return i;
        }
        return -1;
    }

    /**
     * get index of val in an array
     *
     * @param array source array
     * @param val   target element
     * @return index of given value in array.
     */
    public static int indexOf(int[] array, int val) {
        for (int i = 0; i < array.length; i++) {
            if (val == array[i]) return i;
        }
        return -1;
    }

    public static boolean isNullOrEmpty(Object[] array) {
        return null == array || array.length == 0;
    }


    /**
     * return true if given string is neither null nor empty.
     * this is same as (! isNullOrEmpty(String))
     *
     * @param array array to be handled.
     * @return true if given string 'str' has content.
     * i.e., neither null nor empty.
     * @see    #isNullOrEmpty(Object[])
     */
    public static boolean hasContent(Object[] array) {
        return !isNullOrEmpty(array);
    }


    /**
     * @param ray1 first array.
     * @param ray2 second array.
     * @return A new array holding both the contents of <code>ray1</code>
     * and <code>ray2</code>. Contents are placed in the same order,
     * with contents of <code>ray1</code> coming first.
     */
    public static int[] combine(int[] ray1, int[] ray2) {
        int[] ray = new int[ray1.length + ray2.length];
        System.arraycopy(ray1, 0, ray, 0, ray1.length);
        System.arraycopy(ray2, 0, ray, ray1.length, ray2.length);
        return ray;
    }

    /**
     * @param ray1 first array.
     * @param ray2 second array.
     * @return A new array holding both the contents of <code>ray1</code>
     * and <code>ray2</code>. Contents are placed in the same order,
     * with contents of <code>ray1</code> coming first.
     */
    public static <T> T[] combine(T[] ray1, T[] ray2) {
        Class<?> arrayType = ray1.getClass();
        T[] ray = ((Object) arrayType == (Object) Object[].class)
                ? (T[]) new Object[ray1.length + ray2.length]
                : (T[]) Array.newInstance(arrayType.getComponentType(), ray1.length + ray2.length);
        System.arraycopy(ray1, 0, ray, 0, ray1.length);
        System.arraycopy(ray2, 0, ray, ray1.length, ray2.length);
        return ray;
    }

    /**
     * Add an object to the beginning of an array
     *
     * @param string The string to add to the array
     * @param ray    The array to add the object to
     * @return a new array with string as the first position
     */
    public static String[] addStringToBeginningOfArray(String string, String[] ray) {
        String[] ray2 = {string};
        return combine(ray2, ray);
    }

    /**
     * does not take order into consideration
     */
    public static <T> boolean allElementsEqual(@Nullable T[] arrayOne, @Nullable T[] arrayTwo) {
        return CollectionUtil.newSet(arrayOne).equals(CollectionUtil.newSet(arrayTwo));
    }


    public final static Iterator EMPTY_ITERATOR = new Iterator() {
        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public Object next() {
            throw new NoSuchElementException("EMPTY_ITERATOR");
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("EMPTY_ITERATOR");
        }
    };

    @Nonnull
    public static <T> Iterator<T> iterator(@Nullable final T... array) {
        return isNullOrEmpty(array) ? EMPTY_ITERATOR : new Iterator<T>() {
            private int i;

            @Override
            public boolean hasNext() {
                return i < array.length;
            }

            @Override
            public T next() {
                if (hasNext()) return array[i++];
                throw new NoSuchElementException("index: " + i);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Removing item from an array is not supported.");
            }
        };
    }

    public static <T> String toString(final T[] array) {
        StringBuilder string = new StringBuilder("");
        for (T arrayItem : array) {
            string.append(arrayItem.toString()).append(",");
        }
        return string.toString();
    }

    /**
     * @param index  index in <code>params</code> array.
     *               It is safe to use index out of array bound - in such case
     *               the null will be returned.
     * @param values An array values.
     * @return Decoded index-th parameter, when such parameter is not available or is <code>null</code>
     * or is empty, the null will be returned.
     */
    public static <T> T get(int index, T... values) {
        return values == null || values.length < index || index < 0 ? null : values[index];
    }
}