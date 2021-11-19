package bfst21.vector.KDTree;

import bfst21.vector.MapElements.MapFeature;

import java.util.List;

/**
 *  A modified version of Sedgewick's quick sort java class. 
 *  Changed to allow the use of lists rather than arrays.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class Quick {

    // This class should not be instantiated.
    private Quick() { }

    /**
     * Rearranges the array in ascending order, using the natural order.
     * @param a the array to be sorted
     */
    public static void sort(List<MapFeature> a) {
        sort(a, 0, a.size() - 1);
    }

    // quicksort the subarray from a[lo] to a[hi]
    private static void sort(List<MapFeature> a, int lo, int hi) { 
        if (hi <= lo) return;
        int j = partition(a, lo, hi);
        sort(a, lo, j-1);
        sort(a, j+1, hi);
    }

    // partition the subarray a[lo..hi] so that a[lo..j-1] <= a[j] <= a[j+1..hi]
    // and return the index j.
    private static int partition(List<MapFeature> a, int lo, int hi) {
        int i = lo;
        int j = hi;
        MapFeature v = a.get(lo);
        while (true) { 

            // find item on lo to swap
            while (less(a.get(++i), v)) {
                if (i == hi) break;
            }

            // find item on hi to swap
            while (less(v, a.get(--j))) {
                if (j == lo) break;      // redundant since a[lo] acts as sentinel
            }

            // check if pointers cross
            if (i >= j) break;

            exch(a, i, j);
        }       

        // put partitioning item v at a[j]
        exch(a, lo, j);

        // now, a[lo .. j-1] <= a[j] <= a[j+1 .. hi]
        return j;
    }

    
    /**
     * Rearranges the array so that {@code a[k]} contains the kth smallest key;
     * {@code a[0]} through {@code a[k-1]} are less than (or equal to) {@code a[k]}; and
     * {@code a[k+1]} through {@code a[n-1]} are greater than (or equal to) {@code a[k]}.
     *
     * @param  a the array
     * @param  k the rank of the key
     * @return the key of rank {@code k}
     * @throws IllegalArgumentException unless {@code 0 <= k < a.length}
     */
    public static Comparable select(List<MapFeature> a, int k) {
        if (k < 0 || k >= a.size()) {
            throw new IllegalArgumentException("index is not between 0 and " + a.size() + ": " + k);
        }
        int lo = 0, hi = a.size() - 1;
        while (hi > lo) {
            int i = partition(a, lo, hi);
            if      (i > k) hi = i - 1;
            else if (i < k) lo = i + 1;
            else return a.get(i);
        }
        return a.get(lo);
    }


   /***************************************************************************
    *  Helper sorting functions.
    ***************************************************************************/
    
    // is v < w ?
    private static boolean less(MapFeature v, MapFeature w) {
        if (v == w) return false;   // optimization when reference equals
        if(v == null) return false;
        if(w == null) return false;
        return v.compareTo(w) < 0;
    }
        
    // exchange a[i] and a[j]
    private static void exch(List<MapFeature> a, int i, int j) {
        MapFeature swap = a.get(i);
        a.set(i, a.get(j));
        a.set(j, swap);
    }
}
