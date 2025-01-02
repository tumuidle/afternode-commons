package cn.afternode.commons.bukkit.message;

import java.util.Arrays;

/**
 * Command arguments reader
 */
public class ArgsReader {
    private final String[] args;

    private int readerIndex = 0;

    /**
     * Primary constructor
     * @param args args
     */
    public ArgsReader(String[] args) {
        this.args = Arrays.copyOf(args, args.length);
    }

    /**
     * Get arguments count
     * @return size
     */
    public int size() {
        return args.length;
    }

    /**
     * Reset the reader index
     */
    public void resetReader() {
        this.readerIndex = 0;
    }

    /**
     * Get current readerIndex
     * @return index
     */
    public int readerIndex() {
        return this.readerIndex;
    }

    /**
     * Set readerIndex
     * @param index index
     */
    public void readerIndex(int index) {
        this.readerIndex = index;
    }

    /**
     * Read argument in specified index
     * @param index index
     * @return argument or null if array index out of bounds
     */
    public String read(int index) {
        if (args.length <= index) {
            return null;
        }
        return args[index];

    }

    /**
     * Read next argument
     * @return argument or null if not more argument in array
     */
    public String next() {
        String read = this.read(this.readerIndex);
        if (read != null)
            this.readerIndex++;
        return read;
    }

    /**
     * Read argument and parse to integer
     * @param index index
     * @return result
     * @throws IllegalArgumentException if no argument of this index in array
     */
    public int readInt(int index) {
        String val = this.read(index);
        if (val == null)
            throw new IllegalStateException("No argument in index" + index);
        return Integer.parseInt(val);
    }

    /**
     * Read next argument and parse to integer
     * @return result
     * @throws IllegalStateException if no more argument in array
     */
    public int readInt() {
        String val = this.next();
        if (val == null)
            throw new IllegalStateException("No more argument");
        return Integer.parseInt(val);
    }

    /**
     * Read argument and parse to double
     * @param index index
     * @return result
     * @throws IllegalStateException if no argument of this index in array
     */
    public double readDouble(int index) {
        String val = this.read(index);
        if (val == null)
            throw new IllegalStateException("No argument in index " + index);
        return Double.parseDouble(val);
    }

    /**
     * Read next argument and parse to double
     * @return result
     * @throws IllegalStateException if no more argument in array
     */
    public double readDouble() {
        String val = this.next();
        if (val == null)
            throw new IllegalStateException("No more argument");
        return Double.parseDouble(val);
    }

    /**
     * Read argument and parse to float
     * @param index index
     * @return result
     * @throws IllegalStateException if no argument of this index in array
     */
    public float readFloat(int index) {
        String val = this.read(index);
        if (val == null)
            throw new IllegalArgumentException("No argument in index" + index);
        return Float.parseFloat(val);
    }

    /**
     * Read next argument and parse to float
     * @return result
     * @throws IllegalStateException if no more argument in array
     */
    public float readFloat() {
        String val = this.next();
        if (val == null)
            throw new IllegalArgumentException("No more argument");
        return Float.parseFloat(val);
    }

    /**
     * Read argument and parse to boolean
     * @param index index
     * @return result
     * @throws IllegalStateException if no argument of this index in array
     */
    public boolean readBoolean(int index) {
        String val = this.read(index);
        if (val == null)
            throw new IllegalArgumentException("No argument in index" + index);
        return Boolean.parseBoolean(val);
    }

    /**
     * Read next argument and parse to boolean
     * @return result
     * @throws IllegalStateException if no more argument in array
     */
    public boolean readBoolean() {
        String val = this.next();
        if (val == null)
            throw new IllegalArgumentException("No more argument");
        return Boolean.parseBoolean(val);
    }

    /**
     * Read arguments and parse to long string
     * @param index start index
     * @return result
     */
    public String readString(int index) {
        String first = this.read(index);
        if (first.startsWith("\"")) {
            // read long string
            StringBuilder sb = new StringBuilder();
            sb.append(first.substring(1));
            index ++;
            String read = this.read(index);

            while (read != null) {
                if (read.endsWith("\"")) {
                    sb.append(read, 0, read.length() - 1);
                    break;
                }
                sb.append(read);

                index ++;
                read = this.read(index);
            }

            return sb.toString();
        } else {
            return first;
        }
    }

    /**
     * Read arguments since {@link ArgsReader#readerIndex} and parse to long string
     * @return result
     */
    public String readString() {
        String first = this.next();
        if (first.startsWith("\"")) {
            // read long string
            StringBuilder sb = new StringBuilder();
            sb.append(first.substring(1));
            String read = this.next();

            while (read != null) {
                sb.append(" ");
                if (read.endsWith("\"")) {
                    sb.append(read, 0, read.length() - 1);
                    break;
                }
                sb.append(read);

                read = this.next();
            }

            return sb.toString();
        } else {
            return first;
        }
    }
}
