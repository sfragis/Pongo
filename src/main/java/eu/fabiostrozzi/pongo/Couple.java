// Couple.java, created by Fabio Strozzi on May 8, 2011
package eu.fabiostrozzi.pongo;

/**
 * @author Fabio Strozzi
 * 
 */
public class Couple<S, T> {
    private S first;
    private T second;

    /**
     * @param first
     * @param second
     */
    public Couple(S first, T second) {
        this.first = first;
        this.second = second;
    }

    /**
     * @return the first
     */
    public S getFirst() {
        return first;
    }

    /**
     * @param first
     *            the first to set
     */
    public void setFirst(S first) {
        this.first = first;
    }

    /**
     * @return the second
     */
    public T getSecond() {
        return second;
    }

    /**
     * @param second
     *            the second to set
     */
    public void setSecond(T second) {
        this.second = second;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Couple [first=" + first + ", second=" + second + "]";
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((first == null) ? 0 : first.hashCode());
        result = prime * result + ((second == null) ? 0 : second.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        @SuppressWarnings("rawtypes") Couple other = (Couple) obj;
        if (first == null) {
            if (other.first != null)
                return false;
        } else if (!first.equals(other.first))
            return false;
        if (second == null) {
            if (other.second != null)
                return false;
        } else if (!second.equals(other.second))
            return false;
        return true;
    }

}
