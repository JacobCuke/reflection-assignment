
/**
 * CPSC 501
 * Inspector starter class
 *
 * @author Jonathan Hudson
 * @author Jacob Cuke
 */

import java.lang.reflect.*;

public class Inspector {
	
	private int depth = 0;

    public void inspect(Object obj, boolean recursive) {
        Class c = obj.getClass();
        inspectClass(c, obj, recursive, 0);
    }

    private void inspectClass(Class c, Object obj, boolean recursive, int depth) {
    	
    	this.depth = depth;
    	
    	// Class Name
    	println("CLASS");
    	println("Class: " + c.getName());
    	
    	// Immediate SuperClass
    	Class superclass = c.getSuperclass();
    	if (superclass != null) {
    		println("SUPERCLASS -> Recursively Inspect");
        	println("SuperClass: " + superclass.getName());
        	inspectClass(superclass, obj, recursive, depth+1);
        	this.depth = depth;
    	}
    	
    	// Interfaces
    	println("INTERFACES ( " + c.getName() + " )");
    	print("Interfaces-> ");
    	Class[] interfaces = c.getInterfaces();
    	if (interfaces.length == 0) {
    		System.out.println("NONE");
    	}
    	else {
    		System.out.println();
    		for (Class i : interfaces) {
    			println(" INTERFACE -> Recursively Inspect");
    			println(" " + i.getName());
    			inspectClass(i, null, recursive, depth+1);
    			this.depth = depth;
    		}
    	}
    	
    }
    
    private void println(String string) {
    	for (int i = 0; i < depth; i++) {
    		System.out.print("\t");
    	}
    	System.out.println(string);
    }
    
    private void print(String string) {
    	for (int i = 0; i < depth; i++) {
    		System.out.print("\t");
    	}
    	System.out.print(string);
    }

}