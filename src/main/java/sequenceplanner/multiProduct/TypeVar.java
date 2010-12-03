/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sequenceplanner.multiProduct;

/**
 *
 * @author Patrik Magnusson
 */
public class TypeVar {

    // Operators for EFA guards.
    static String EFA_AND = "&";
    static String EFA_OR = "|";
    static String EFA_BOOK = "+=1;";
    static String EFA_UNBOOK = "-=1;";
    static String EFA_EQUAL = "==";
    static String EFA_SET = "=";
    static String EFA_STRICTLY_LESS_THAN = "<";
    static String EFA_STRICTLY_LARGER_THAN = ">";
    // Never change these two.
    static String EFA_PLUS_ONE = "+=1;";
    static String EFA_MINUS_ONE = "-=1;";
    final static String EFA_STRICTLY_LARGER_THAN_ZERO = EFA_STRICTLY_LARGER_THAN+"0";
    // Operators in Sequence Planner
    public static String SP_AND = "A";
    public static String SP_OR = "V";
    static String SP_BOOK_RESOURCE = "+";
    static String SP_UNBOOK_RESOURCE = "-";
    //MultiProduct
    public final static String SEPARATION = "_";
    final static String POS = "p";
    final static String POS_OUT = "out";
    final static String POS_MERGE = "mrg";
    final static String POS_PROCESS = SEPARATION + "p";
    final static String POS_MOVE = SEPARATION + "m";
    final static String PROCESSING_LEVEL = SEPARATION + "pl";
    final static Integer PROCESSING_LEVEL_COUNT_LIMIT = 3; //just a large number. 0->7 gives 3 bits.
    final static String LIMIT = SEPARATION + "limit";
    final static String YES = "1";
    final static String NO = "0";
    public final static String TRANSPORT = "t";
    final static String LOCATION = "PM";
    public final static String SP_RESOURCE_POSITIONS = "Positions";
    public final static String SP_RESOURCE_PRODUCTS = "Products";
    public final static String SP_RESOURCE_CAPACITY = "Capacity";
    public final static String SP_RESOURCE_LIMIT = "Limit";

    //Description
    final static String DESC_KEYSEPARATION = "#";
    final static String DESC_VALUESEPARATION = ":";
    //Extended Data
    final static String ED_PRODUCT_TYPE = "productType";
    final static String ED_OP_TYPE = "opType";
    public final static String ED_OP_TYPE_PROCESS = "pop";
    public final static String ED_OP_TYPE_TRANSPORT = "top";
    final static String ED_SOURCE_POS = "sourcePos";
    final static String ED_DEST_POS = "destPos";
    final static String ED_PROCESSING_LEVEL_COUNTER = "plCount";
    public final static String ED_PROCESSING_LEVEL_COUNTER_NO = "no";
    final static String ED_MERGE = "merge";
    final static String ED_GUARD = "guard";
    final static String ED_ORDER = "order";
    public final static String ED_ORDER_FIRST = "first";
    public final static String ED_ORDER_LAST = "last";

}
