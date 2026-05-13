package com.rakbank.studentservice.utils;

import ch.qos.logback.core.util.StringUtil;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.collections.MapUtils;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;


/**
 *	collection util.
 *
 * @author Aldmour Hamzeh (hamzeh.aldmour@acabes.com)
 * @version 1.0, Mar 8, 2026 at 4:04:19 PM
 * @editor IDEA
 */
public class CollectionUtil
{
	// ---------------------------------------------------------------- null ---

	/**
	 * 	@param 	c	collection to be checked.
	 * 	@return	original collection if it is not null; or an empty collection.
	 */
	public static <T> Collection<T> noNull(Collection<T> c)
	{
		if (c == null) {
			return Collections.emptyList();
		}
		return c;
	}


	/**
	 * 	@param 	list	list to be checked.
	 * 	@return	original list if it is not null; or an empty list.
	 */
	public static <T> List<T> noNull(List<T> list)
	{
		if (list == null) {
			return Collections.emptyList();
		}
		return list;
	}


	/**
	 * 	@param 	set		set to be checked.
	 * 	@return	original set if it is not null; or an empty set.
	 */
	public static <T> Set<T> noNull(Set<T> set)
	{
		if (set == null) {
			return Collections.emptySet();
		}
		return set;
	}


	/**
	 * 	@param 	map		map to be checked.
	 * 	@return	original map if it is not null; or an empty map. 
	 */
	public static <K,V> Map<K,V> noNull(Map<K,V> map)
	{
		if (map == null) {
			return Collections.emptyMap();
		}
		return map;
	}


	// --------------------------------------------------------------- query ---

	/**
	 * 	return true if given collection 'c' is neither null nor empty.
	 * 	this is same as (! isNullOrEmpty(Collection))
	 * 
	 * 	@param 	c	collection to be handled.
	 * 	@return	true if given collection has content.  i.e., neither null nor empty.
	 * 	@see	#isNullOrEmpty(Collection) 
	 */
	public static boolean hasContent(@Nullable Collection c)
	{
		return ! isNullOrEmpty(c);
	}
	

	/**
	 * 	return true if given map is neither null nor empty.
	 * 	this is same as (! isNullOrEmpty(Map))
	 * 
	 * 	@param 	map		Map to be handled.
	 * 	@return	true if given map has content.  i.e., neither null nor empty.
	 * 	@see	#isNullOrEmpty(Map) 
	 */
	public static boolean hasContent(Map map)
	{
		return ! isNullOrEmpty(map);
	}
	
	
	/**
	 * 	@param	c	Collection object.
	 *	@return true if given collection is null or empty; false otherwise.
	 */
	public static boolean isNullOrEmpty(Collection c)
	{
		return (c==null) || c.isEmpty();
	}


	/**
	 * 	@param	map	Map object.
	 *	@return true if given map is null or empty; false otherwise.
	 */
	public static boolean isNullOrEmpty(Map map)
	{
		return (map==null) || map.isEmpty();
	}


	/**
	 * 	@param 	c	Collection object.
	 * 	@return	size of given collection, -1 if it is null.
	 */
	public static int size(@Nullable Collection c)
	{
		return (c!=null) ? c.size() : -1;
	}
	
	
	/**
	 * 	@param 	map	Map object.
	 * 	@return	size of given map, -1 if it is null.
	 */
	public static int size(Map map)
	{
		return (map!=null) ? map.size() : -1;
	}
	

	// --------------------------------------------------------------- empty ---

	/**
	 *	return an empty list with given modifiability.
     *  if it is modifiable, the backend is a LinkedList. 
	 *
	 * 	@param 	modifiable	do we want resulting list to be modifiable?
	 * 	@return	List<T>.
	 */
	public static <T> List<T> emptyList(boolean modifiable)
	{
		if (modifiable) {
			return new LinkedList<>();
		} else {
			return Collections.emptyList();
		}
	}


	/**
	 *	return an empty set with given modifiability.
     *  if it is modifiable, the backend is a LinkedHashSet. 
	 *
	 * 	@param 	modifiable	do we want resulting set to be modifiable?
	 * 	@return	Set<T>.
	 */
	public static <T> Set<T> emptySet(boolean modifiable)
	{
		if (modifiable) {
			return new LinkedHashSet<>();
		} else {
			return Collections.emptySet();
		}
	}


	/**
	 *	return an empty map with given modifiability.
     *  if it is modifiable, the backend is a LinkedHashMap. 
	 *
	 * 	@param 	modifiable	do we want resulting map to be modifiable?
	 * 	@return	Map<K, V>.
	 */
	public static <K,V> Map<K,V> emptyMap(boolean modifiable)
	{
		if (modifiable) {
			return new LinkedHashMap<>();
		} else {
			return Collections.emptyMap();
		}
	}


	// -------------------------------------------------------- synchronized ---

	/**
	 *	@return a new synchronized list backended with LinkedList.
	 */
	public static <T> List<T> synchronizedList()
	{
		return Collections.synchronizedList(new LinkedList<T>());
	}

	
	/**
	 *	@return a new synchronized set backended with LinkedHashSet.
	 */
	public static <T> Set<T> synchronizedSet()
	{
		return Collections.synchronizedSet(new LinkedHashSet<T>());
	}


	/**
	 *	@return a new synchronized map backended with LinkedHashMap.
	 */
	public static <K, V> Map<K, V> synchronizedMap()
	{
		return Collections.synchronizedMap(new LinkedHashMap<K, V>());
	}


	// ----------------------------------------------------------------- set ---
    
    /**
     * 	@param	elements	elements in the resulting set.
     *	@return 
     *      a new modifiable list with given elements, order returning by 
     *      iterator is same order as items given in parameter. 
     */
    public static <T> List<T> newList(T... elements)
    {
        List<T> list = new LinkedList<>();
        for (T t : elements) {
            list.add(t);
        }
        return list;
    }
    
    
    // ----------------------------------------------------------------- set ---

    /**
     *  @param  comp    comparator.
     *  @return new modifiable tree set with specified comparator.
     */
    public static <T> Set<T> newSet(Comparator<T> comp)
    {
        return new TreeSet<>(comp);
    }


	/**
	 * 	@param	elements	elements in the resulting set.
	 *	@return 
     *      a new modifiable set with given elements, order returning by 
     *      iterator is same order as items given in parameter. 
	 */
	public static <T> Set<T> newSet( @Nullable T... elements)
	{
        if( null == elements )
            return Collections.emptySet();

		Set<T> set = new LinkedHashSet<>();
        set.addAll( Arrays.asList( elements ) );
		return set;
	}

    public static <I,O> Set<O> newSet(Convertor<I, O> convertor, @Nullable I... elements) {
        if( null == elements )
            return Collections.emptySet();

        Set<O> set = new LinkedHashSet<>();
        for (I i : elements) {
            set.add(convertor.convert(i));
        }
        return set;
    }


    // ----------------------------------------------------------------- map ---

    /**
     *  @param  comp    comparator.
     *  @return new modifiable tree map with specified comparator.
     */
    public static <K, V> Map<K, V> newMap(Comparator<K> comp)
    {
        return new TreeMap<>(comp);
    }


	/**
     *  create a new modifiable Map object with given key-value pairs.
     *  entries in the returning map has same order defined in given map.
     * 
	 * 	@param	keys	map keys.
	 * 	@param	values	map values.
	 *	@return a new map with given key/value pairs.
	 * 	@throws	IllegalArgumentException 
	 * 		if any argument is null, or lengths of the arrays not match. 
	 */
	public static <K, V> Map<K, V> newMap(K[] keys, V[] values)
	{
		AssertUtil.Nonnull("keys", keys);
		AssertUtil.Nonnull("values", values);
		AssertUtil.eq(keys.length+"!="+values.length, keys.length, values.length);
		
		Map<K, V> map = new LinkedHashMap<>();
		for (int i=0, n=keys.length; i<n; i++) {
			map.put(keys[i], values[i]);
		}
		return map;
	}
	

	/**
	 * 	reverse mapping from key->value to value->key.
     *  entries in the returning map has same order defined in given map.
	 * 
	 * 	this returns a new map, which keys are values of given map,
	 *	and values are keys of given map. 
	 * 
	 * 	@param 	map		original map to be handled.
	 * 	@return	map storing all the value->key entries.
	 * 	@throws	IllegalArgumentException 
	 * 		if given map contains two different keys (K1, K2) and a value V such that:
	 * 		1. map.get(K1) = V
	 * 		2. map.get(K2) = V 
	 */
	public static <V, K> Map<V, K> reverse(Map<K, V> map)
	{
		Map<V, K> reverse = new LinkedHashMap<>();
		for (Map.Entry<K, V> entry : map.entrySet()) {
            K key = entry.getKey();
            V value = entry.getValue();
            if (reverse.containsKey(value)) {
                throw new IllegalArgumentException("value for entry already exists : " + entry); 
            }
			reverse.put(value, key);
		}
		return reverse;
	}
	
	
	// ----------------------------------------------------------- iteration ---

	/**
     *	returns an iterator over the specified enumeration.  this provides
     *	interoperatbility with improved APIs an enumeration as input, but
     *	developers want to have iterator in the code.
     *
     *	note: after calling this method, the iterator being returned is
     *	backended with given enumeration.  any calls to the given enumeration
     *	will affect the iterator being returned.
     *
     *	@param e	the enumeration for which an iterator is to be returned.
     *	@return an iterator over the specified enumeration.
     *	@see	Collections#enumeration(Collection)
	 */
	public static <E> Iterator<E> iterator(final Enumeration<E> e)
	{
		return new Iterator<E>() {
			public boolean hasNext() {
				return e.hasMoreElements();
			}

			public E next() {
				return e.nextElement();
			}

			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}


	/**
     *	returns an iterable over the specified enumeration.  this provides
     *	interoperatbility with improved APIs an enumeration as input, but
     *	developers want to have iterable in the code. e.g.: enhanced-for
     *
     *	note: after calling this method, the iterator being returned is
     *	backended with given enumeration.  any calls to the given enumeration
     *	will affect the iterator being returned.
     *
     *	@param e	the enumeration for which an iterator is to be returned.
     *	@return an iterator over the specified enumeration.
     *	@see	Collections#enumeration(Collection)
	 */
	public static <E> Iterable<E> iterable(final Enumeration<E> e)
	{
		return new Iterable<E>() {
			public Iterator<E> iterator() {
				return CollectionUtil.iterator(e);
			}
		};
	}


	/**
	 * 	return next element in the given iterator.
	 * 	null when there is no more element available.
	 * 	this method never throw NoSuchElementException.
	 * 
	 * 	@param 	it	iterator to be handled.
	 * 	@return	next element; null if no more element.
	 */
	public static <E> E next(Iterator<E> it)
	{
		return it.hasNext() ? it.next() : null;
	}
	

	/**
	 * 	return the last element in the given collection.
	 *
	 * 	@param 	c	collection to be handled.
	 * 	@return	last element.
	 * 	@throws	IllegalArgumentException if given collection is null or empty.
	 */
	public static <E> E last(Collection<E> c)
	{
		AssertUtil.notEmpty("c", c);

		if (c instanceof RandomAccess) {
			List<E> list = (List<E>) c;
			return list.get(list.size()-1);
		}
		if (c instanceof LinkedList) {
			LinkedList<E> list = (LinkedList<E>) c;
			return list.getLast();
		}
		E last = null;
		for (E element : c) {
			last = element;
		}
		return last;
	}


	/**
	 * 	copy all elements from 'source' enumeration to 'target' collection.
	 * 	this method will iterate the given enumeration object until no more
	 * 	elements are available, thus after calling this method the 'souce'
	 * 	enumeration will be exhausted.
	 *
	 * 	@param 	source	source elements to be copied from.
	 * 	@param 	target	target collection to hold the results.
	 * 	@return	'target' collection object.
	 */
	public static <E> Collection<E> copyElements(Enumeration<E> source, Collection<E> target)
	{
		while (source.hasMoreElements()) {
			target.add(source.nextElement());
		}
		return target;
	}

	@Nonnull public static <T> Set<T> intersection(Collection<T>... collections) {
        if (collections == null) throw new IllegalArgumentException();
		if (collections.length == 1
				&& collections[0] != null) {
			return new HashSet<>(collections[0]);
		}

		// sort them by the size to minimize the number of iterations
		Collection<T>[] myCollections = new Collection[collections.length];
		System.arraycopy(collections, 0, myCollections, 0, collections.length);
		Arrays.sort(myCollections, new Comparator<Collection>() {
					public int compare(Collection set1, Collection set2) {
						return (set1 == null ? -1 : set1.size())
								- (set2 == null ? -1 : set2.size());
					}
				});
		HashSet<T> intersection = new HashSet<>(myCollections[0].size());
		for (T element : noNull(myCollections[0])) {
			boolean match = true;
			for (int i = 1; i < myCollections.length; i++) {
				Collection set = myCollections[i];
				if (set == null || set.isEmpty()) return Collections.emptySet();

				if (!set.contains(element)) {
					match = false;
					break;
				}
			}
			if (match) {
				intersection.add(element);
			}
		}
		return intersection;
	}

    // computes set operation A-B
    @Nonnull public static <T> Set<T> subtract(Collection<T> A, Collection<T> B) {
        Set<T> result = new HashSet<>();
        if (A == null || A.isEmpty()) return result;
        if (B == null || B.isEmpty()) return new HashSet<>(A);

        for (T item : A) {
            if (!B.contains(item)) {
                result.add(item);
            }
        }
        return result;
    }

    public static <T> boolean setEquality(Collection<T> A, Collection<T> B) {
        return subtract(A, B).isEmpty() && subtract(B, A).isEmpty();
    }
	
    // ----------------------------------------------------------------- add ---

    /**
     *  add all items from 'from' array to target collection.
     * 
     *  @param  from    items to be added.
     *  @param  to      target collection.
     */
    public static <T> void addAll(@Nullable T[] from, 
                                  @Nonnull Collection<T> to)
    {
        if (ArrayUtil.length(from) > 0) {
            for (T item : from) {
                to.add(item);
            }
        }
    }
    
    
    // --------------------------------------------------------------- print ---
    
    /**
     * Can handle nested Maps.
     *
     * @param map map to be handled.
     * @param label may be null
     * @return print.
     */
    public static <T,Z> String print( Map<T,Z> map, String label )
    {
        ByteArrayOutputStream baoStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream( baoStream );
        try
        {
            MapUtils.verbosePrint( out, label, map );
        }
        finally
        {
            out.close();
        }
        return baoStream.toString();
    }

    /**
     * Doesn't handle nested Sets.
     *
     * @param set set to be handled.
     * @param label may be null
     * @return print
     */
    public static <T> String print( Set<T> set, String label )
    {
        StringBuilder sb = new StringBuilder( set.size() * 50 );

        if( ! StringUtil.isNullOrEmpty( label ) )
            sb.append( label ).append( " =\n" );

        sb.append( "{\n" );
        for( T curItem : set )
            sb.append( "\t" ).append( curItem.toString() ).append( "\n" );

        sb.append( "}\n" );

        return sb.toString();
    }

	/**
     * @param list list to be handled. 
	 * @param label may be null
     * @return print
	 */
	public static <T> String print(List<T> list, String label) {
		if (list == null) return (label!=null?label:"List")+" = NULL";
		StringBuilder sb = new StringBuilder(list.size() * 50);
		if (!StringUtil.isNullOrEmpty(label)) sb.append(label ).append(" =\n");
		sb.append("{\n");
		for (T curItem : list) {
			if (curItem != null) {
				sb.append("\t" ).append( curItem.toString() ).append( "\n");
			} else {
				sb.append("\tNULL\n");
			}
		}
		sb.append("}\n");
		return sb.toString();
	}


    /**
     *  print items in given collection.
     *  description is converted by given convertor, and if there are more 
     *  entries in the collection than given size, remaining items are not
     *  printed. 
     * 
     *  @param  collection  collection to be handled.
     *  @param  convertor   convertor. null to use Object.toString()
     *  @param  size        max. size to be printed.
     *  @return collection content description.
     */
    @Nonnull
    public static <T> String print(@Nonnull Collection<T> collection, 
                                   @Nullable Convertor<T, String> convertor, 
                                   int size)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Iterator<T> it = collection.iterator();
        for (int i=0; i<size && it.hasNext(); i++) {
            T item = it.next();
            String desc = (convertor != null) ? 
                          convertor.convert(item) : 
                          String.valueOf(item);
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(desc);
        }
        if (collection.size() > size) {
            sb.append("... (size=").append(collection.size()).append(")");
        }
        sb.append("]");
        return sb.toString();
    }


    /**
     *  @param  collection  collection to be handled.
     *  @return true if given collection is null, empty, or has only null elements.
     */
    public static <T> boolean containsNullOnly(@Nullable Collection<T> collection)
    {
        if (isNullOrEmpty(collection)) {
            return true;
        }
        for (T item : collection) {
            if (item != null) {
                return false;
            }
        }
        return true;
    }

	/**
	 * This method copies all elements of <code>source</code> to <code>target</code> collection.
	 * All elements are converted using <code>convertor</code>.
	 * @param source
	 *     cannot be null
	 * @param target
	 *     cannot be null
	 * @param convertor
	 *     cannot be null
	 */
	public static <S, D> void copy(@Nonnull Collection<S> source, @Nonnull Collection<D> target, 
			@Nonnull Convertor<? super S, D> convertor) {
		for (S s : source) {
			D d = convertor.convert(s);
			target.add(d);
		}
	}

    /**
     * Copies all elements of <code>source</code> matching the filters
     * to <code>target</code> collection.
     */
    public static <E> void copy(@Nonnull Collection<E> source, @Nonnull Collection<E> target,
            Collection<? extends IFilter<? super E>> filters) {
        for (E e : source) {
            if (FilterUtil.matchesAll(e, filters)) {
                target.add(e);
            }
        }
    }

    /**
     * Copies all elements of <code>source</code> matching the filters
     * to <code>target</code> collection.
     */
    public static <E> void copy(@Nonnull Collection<E> source, @Nonnull Collection<E> target,
            IFilter<? super E> filter) {
        copy(source, target, Collections.singleton(filter));
    }

    public static float[] toPrimitive(Collection<Float> values)
    {
        float[] results = new float[values.size()];
        int index = 0;
        for (float value : values) {
            results[index++] = value;
        }
        return results;
    }

	/**
	 * Processes all elements of a collection in batches.
	 */
	public static <T> void processInBatches(Collection<T> collection, int batchSize, Closure<Collection<T>> closure) {
		if (batchSize <= 0) {
			throw new IllegalArgumentException();
		}
		if (closure == null) {
			throw new NullPointerException();
		}
		if (collection == null || collection.isEmpty()) {
			return;
		}
		ArrayList<T> batch = new ArrayList<>(batchSize);
		for (T v : collection) {
			batch.add(v);
			if (batch.size() == batchSize) {
				closure.run(batch);
				batch.clear();
			}
		}
		if (!batch.isEmpty()) {
			closure.run(batch);
			batch.clear();
		}
	}


    /**
     * @return first item of the collection, if collection was null or empty returns null
     */
    @Nullable
    public static <T> T first(@Nullable Collection<? extends T> collection) {
        return isNullOrEmpty(collection) ? null : collection.iterator().next();
    }

    
    /**
     * returns a list padded up to windowStart with <tt>padding</tt>.
     *
     * @param paddingLength how many positions to pad, starting from 0.
     * @param totalSize the initialized capacity of the list
     */
    public static <T> ArrayList<T> newPaddedArrayList( @Nullable T padding, int paddingLength, int totalSize )
    {
        AssertUtil.le( "paddingLength", paddingLength, totalSize );

        ArrayList<T> list = new ArrayList<>(totalSize);
        for (int i=0; i<paddingLength; i++) {
            list.add(padding);
        }
        return list;
    }

    public static <T> String printNoLabel(List<T> list, Convertor<T, String> convertor, String separator)
    {
        if(list == null || list.isEmpty()) return "";

        StringBuilder sb = new StringBuilder( list.size() * 50 );

        if( StringUtil.isNullOrEmpty( separator ) )
            separator = ", ";

        for(int i=0; i<list.size(); i++) {
            T item = list.get(i);
            sb.append(item == null ? "" : convertor.convert(item));
            if(i != (list.size() - 1) && item != null) sb.append(separator);
        }

        return sb.toString();
    }

    public static <T> List<T> extract(List<T> list, int start, int end) {

        List<T> newList = new ArrayList<>();

        if(end >= list.size()) end = list.size();

        for(int i = start; i < end; i++) {
            newList.add(list.get(i));
        }

        return newList;
    }

    /**
     * Returns a new list containing only the items of the original <i>list</i> that are accepted by the <i>filter</i>.
     * The original collection <i>list</i> is left intact.
     *
     * @param list the original list
     * @param filter the filter applied on all list items
     * @param <T> the item type of the list
     * @return a new List containing only the items of the original <i>list</i> that are accepted by the <i>filter</i>
     */
    @Nonnull
    public static <T> List<T> filterListBy(final List<T> list, final IFilter<T> filter) {
        List<T> filteredList = new ArrayList<>();
        for (T item : list) {
            if (filter.accept(item)) {
                filteredList.add(item);
            }
        }
        return filteredList;
    }

    @Nonnull
    public static <T> List<T> addAll(final List<T> list1, final List<T> list2) {
        if (list1 == null) return list2 == null ? Collections.<T>emptyList() : new LinkedList<>(list2);
        List<T> result = new LinkedList<>(list1);
        if (list2 == null) return result;
        result.addAll(list2);
        return result;
    }

	@Nullable
	public static <T> T getFromList(List<T> licenses, int index) {
		return noNull(licenses).size() > index? licenses.get(index): null;
	}

    public static <K, V extends Comparable<? super V>> Map<K, V> sortMapByValue(Map<K, V> map) {
        return map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    /**
     * Processes all elements of {@code src} collection and adds results to returned list.
     * @param src
     *      {@code null} is treated as an empty collection i.e. the result is an empty list.
     * @param fn
     * @param <S>
     * @param <D>
     * @return
     *      Never {@code null}.
     */
    public static <S, D> ArrayList<D> newCopy(Collection<S> src,
            Function<? super S, ? extends D> fn) {
        requireNonNull(fn);
        if (src == null) {
            return new ArrayList<>();
        }
        ArrayList<D> list = new ArrayList<>(src.size());
        for (S s : src) {
            list.add(fn.apply(s));
        }
        return list;
    }

    /**
     * Processes elements accepted by {@code filter} and adds the results to the returned list.
     * @param src
     *      {@code null} is treated as an empty collection i.e. the result is an empty list.
     * @param function
     * @param <S>
     * @param <D>
     * @return
     *      Never {@code null}.
     */
    public static <S, D> ArrayList<D> newCopy(Collection<S> src,
            Predicate<? super S> filter,
            Function<? super S, ? extends D> function) {
        requireNonNull(function);
        if (src == null) {
            return new ArrayList<>();
        }
        ArrayList<D> list = new ArrayList<>(src.size());
        for (S s : src) {
            if (filter.test(s)) {
                list.add(function.apply(s));
            }
        }
        return list;
    }

    /**
     * Returns an array filled with elements from the collection.
     * @param c
     *      Can be null.
     * @return
     *      Never null.
     */
    public static <E> E[] array(Collection<? extends E> c, Class<E> klass) {
        if (c == null) {
            //noinspection unchecked
            return (E[]) Array.newInstance(klass, 0);
        }
        //noinspection unchecked
        E[] arr = (E[]) Array.newInstance(klass, c.size());
        if (c instanceof List) {
            ((List) c).toArray(arr);
            return arr;
        }
        int i = 0;
        for (E v : c) {
            arr[i++] = v;
        }
        return arr;
    }

    /**
     * Collects all elements into a {@link Map}, {@code keyFunc} is used to map
     * elements to keys.
     * @param src
     *      Can be null.
     * @param keyFunc
     * @return
     *      Never null.
     */
    public static <V, K> HashMap<K, V> newMapNoDups(Iterable<V> src, Function<V, K> keyFunc) {
        HashMap<K, V> map = new HashMap<>();
        if (src == null) {
            return map;
        }
        for (V v : src) {
            K key = keyFunc.apply(v);
            if (map.put(key, v) != null) {
                throw new IllegalArgumentException("duplicate element with key: " + key);
            }
        }
        return map;
    }

    public static boolean containsIgnoreCase(List<String> list, String str) {
        for (String i : list) {
            if (i.equalsIgnoreCase(str)) { return true; }
        }
        return false;
    }

    /**
      * Finds the first single element matched by a predicate.
      * @param col
      *      Can be null.
      * @param predicate
      * @param <E>
      * @return
      *      Found element, or {@code null} if collection is {@code null}
      *      or no matching element is found.
      */
     public static <E> E findFirst(Iterable<E> col,
             Predicate<? super E> predicate) {
         if (col == null) {
             return null;
         }
         for (E e : col) {
             if (predicate.test(e)) {
                 return e;
             }
         }
         return null;
     }


	public static <T> int indexOf(List<T> list, Predicate<T> predicate) {
		int index = list.size() - 1;
		while (index >= 0) {
			T item = list.get(index);
			if (predicate.test(item)) {
				return index;
			}
			index--;
		}
		return -1;
	}

    /**
     * Performs a union for two lists while not creating a new instance if not needed. If 
     * one of the two lists is empty, the other list is returned. If both are not empty,
     * a new list is created combining the two.
     * @param a   The first list to union
     * @param b   The second list to union
     * @return   A union of the two lists that avoids new instance creation if not needed. If both
     *           lists are empty, A is returned. If A is not empty and B is empty, A is returned
     *           If A is empty and B is not empty, B is returned. If both A and B are not empty,
     *           a new List combining A and B is returned.
     */
    public static <T> List<T> chooseNotEmptyOrUnion(List<T> a, List<T> b) {
        //if B is not empty and A is empty return B
        if (!b.isEmpty()) {
            if (a.isEmpty()) {
                return b;
            }
        }
        //if both are empty or A is not empty and B is empty, return A
        else {
            return a;
        }
        
        //if both are not empty, create a new List combining A and B
        List<T> results = new ArrayList<>(a.size() + b.size());
        results.addAll(a);
        results.addAll(b);
        return results;
    } 
}
