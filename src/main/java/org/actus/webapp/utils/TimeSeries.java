package org.actus.webapp.utils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * {@link TimeSeries} class
 * <P>
 * TimeSeries class is used for different kind of operations such as
 * adding,removing the single value or array of values
 */
public class TimeSeries<T, V> {

	private NavigableMap<T, V> _map = null;

	public TimeSeries() {
		_map = new TreeMap<T, V>();
	}

	public TimeSeries(T[] dateTimes, V[] values) {
		NavigableMap<T, V> newMap = new TreeMap<T, V>();
		for (int i = 0; i < values.length; i++) {
			newMap.put(dateTimes[i], values[i]);
		}
		_map = newMap;
	}

	public TimeSeries(Map<T, V> subMap) {
		_map = new TreeMap<>();
		_map.putAll(subMap);
	}

	/**
	 * add values to timeSeries with dateTimes as keys
	 * @param dateTimes
	 * @param values
	 */
	public void of(T[] dateTimes, V[] values) {
		NavigableMap<T, V> newMap = new TreeMap<T, V>();
		for (int i = 0; i < values.length; i++) {
			newMap.put(dateTimes[i], values[i]);
		}
		_map = newMap;
	}

	/**
	 * add single value to timeSeries with dateTime as key
	 * @param dateTime
	 * @param value
	 */
	public void put(T dateTime, V value) {
		_map.put(dateTime, value);
	}

	/**
	 * remove value from timeSeries
	 * @param dateTime
	 */
	public void remove(T dateTime) {
		_map.remove(dateTime);
	}
	
	/**
	 * returns size of timeSeries
	 * @return size
	 */
	public int size() {
		return _map.size();
	}

	/**
	 * checks whether the timeSeries is empty or not
	 * @return true or false
	 */
	public boolean isEmpty() {
		return _map.isEmpty();
	}

	/**
	 * checks whether the given key exists in timeSeries 
	 * @param dateTime
	 * @return true 
	 * 				if key exists in timeSeries
	 */
	public boolean contains(T dateTime) {
		return _map.containsKey(dateTime);
	}

	/**
	 * returns value of specified key:dateTime
	 * @param dateTime
	 * @return value
	 */
	public V getValueFor(T dateTime, int method) {
		if(method == 0) {
			return _map.get(dateTime);
		} else if(method == 1) {
			return subSeries(getEarliestTime(), true,
			dateTime, true).getLatestValue();
		} else {
			throw new Error( "Time series method unknown!");
		}
		
	}

	/**
	 * returns dateTime at particular index
	 * @param index
	 * @return dateTime
	 */
	public T getTimeAt(int index) {
		int i = 0;
		for (Map.Entry<T, V> entry : _map.entrySet()) {
			if (i == index) {
				return entry.getKey();
			}
			i++;
		}
		return null;
	}

	/**
	 * returns value at specified index
	 * @param index
	 * @return value 
	 */
	public V getValueAt(int index) {
		return _map.get(getTimeAt(index));
	}

	/**
	 * returns earliest time from timeSeries
	 * @return dateTime 
	 */
	public T getEarliestTime() {
		return _map.firstKey();
	}

	/**
	 * returns earliest value from timeSeries
	 * @return value 
	 */
	public V getEarliestValue() {
		return _map.get(_map.firstKey());
	}
	/**
	 * returns latest dateTime from timeSeries
	 * @return dateTime
	 */
	public T getLatestTime() {
		return _map.lastKey();
	}

	/**
	 * returns latest value from timeSeries
	 * @return value
	 */
	public V getLatestValue() {
		return _map.get(_map.lastKey());
	}

	/**
	 * Returns an iterator over the entries in this series
	 * @return iterator
	 */
	public Iterator<Entry<T, V>> iterator() {
		return _map.entrySet().iterator();
	}

	/**
	 * Returns an iterator over the keys in this series
	 * @return iterator
	 */
	public Iterator<T> timesIterator() {
		return _map.keySet().iterator();
	}

	/**
	 * Returns an iterator over the values in this series
	 * @return iterator
	 */
	public Iterator<V> valuesIterator() {
		return _map.values().iterator();
	}

	/**
	 * Returns a view of the portion of this series whose keys range from startTime,
	 * to endTime.
	 * @param startTimeInclusive
	 * @param endTimeInclusive
	 * @return subSeries
	 */
	public TimeSeries<T, V> subSeries(T startTimeInclusive, T endTimeInclusive) {
		return new TimeSeries<T, V>( _map.subMap(
				startTimeInclusive, endTimeInclusive));
	}

	/**
	 * Returns a view of the portion of this series whose keys range from fromKey to toKey
	 * @param startTime
	 * @param includeStart
	 * @param endTime
	 * @param includeEnd
	 * @return subSeries
	 */
	public TimeSeries<T, V> subSeries(T startTime, boolean includeStart,
			T endTime, boolean includeEnd) {
		return new TimeSeries<T, V>(_map.subMap(startTime, includeStart,
				endTime, includeEnd));
	}

	/**
	 * Returns a view of the portion of this series whose keys are less than 
	 * (or equal to, if inclusive is true) toKey
	 * @param numItems
	 * @return subSeries
	 */
	public TimeSeries<T, V> head(int numItems) {
		T element = getTimeAt(numItems);
		return new TimeSeries<T, V>(_map.headMap(element, true));
	}

	/**
	 * Returns a view of the portion of this series whose keys are greater than 
	 * (or equal to, if inclusive is true) fromKey.
	 * @param numItems
	 * @return
	 */
	public TimeSeries<T, V> tail(int numItems) {
		T element = getTimeAt(size() - numItems);
		return new TimeSeries<T, V>(_map.tailMap(element, true));
	}

	public TimeSeries<T, V> lag(int lagCount) {
		return null;
	}

	/**
	 * Returns a list of the keys contained in this series
	 * @return list
	 */
	public List<T> times() {
		return new ArrayList<T>(_map.keySet());
	}

	/**
	 * Returns a list of the values contained in this series
	 * @return list
	 */
	public List<V> values() {
		return new ArrayList<V>(_map.values());
	}

	/**
	 * Returns a array of the keys contained in this series
	 * @return list
	 */
	public T[] timesArray() {
		return  this.toArray(times());
	}

	/**
	 * Returns a array of the values contained in this series
	 * @return list
	 */
	@SuppressWarnings("unchecked")
	public V[] valuesArray() {
		return (V[]) values().toArray();
	}

	/**
	 * returns instance of timeSeries
	 * @param dateTimes
	 * @param values
	 * @return instance of timeSeries
	 */
	public TimeSeries<T, V> newInstance(T[] dateTimes, V[] values) {
		return new TimeSeries<T, V>(dateTimes, values);
	}

	/**
	 * converts the given list type into array.
	 * @param list
	 * @return arrayType
	 */
	@SuppressWarnings("unchecked")
	public T[] toArray(List<T> list) {
		T[] arrayType = (T[]) Array.newInstance(list.get(0)
	                                           .getClass(), list.size());
	    for (int i = 0; i < list.size(); i++) {
	        arrayType[i] = list.get(i);
	    }
	    return arrayType;
	}
}