
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
    	
    	// Constructors
    	println("CONSTRUCTORS ( " + c.getName() + " )");
    	print("Constructors-> ");
    	Constructor[] constructors = c.getDeclaredConstructors();
    	if (constructors.length == 0) {
    		System.out.println("NONE");
    	}
    	else {
    		System.out.println();
    		for (Constructor con : constructors) {
    			println(" CONSTRUCTOR");
    			println("  Name: " + con.getName());
    			
    			// Parameter types
    			print("  Parameter types-> ");
    			Parameter[] parameters = con.getParameters();
    			if (parameters.length == 0) { 
    				System.out.println("NONE");
    			}
    			else {
    				System.out.println();
    				for (Parameter p : parameters) {
    					println("   " + p.getType());
    				}
    			}
    			
    			// Modifiers
    			print("  Modifiers: ");
    			int modifiers = con.getModifiers();
    			System.out.println(Modifier.toString(modifiers));
    		}
    	}
    	
    	// Methods
    	println("METHODS ( " + c.getName() + " )");
    	print("Methods-> ");
    	Method[] methods = c.getDeclaredMethods();
    	if (methods.length == 0) {
    		System.out.println("NONE");
    	}
    	else {
    		System.out.println();
    		for (Method m : methods) {
    			println(" METHOD");
    			println("  Name: " + m.getName());
    			
    			// Exceptions
    			print("  Exceptions-> ");
    			Class[] exceptions = m.getExceptionTypes();
    			if (exceptions.length == 0) {
    				System.out.println("NONE");
    			}
    			else {
    				System.out.println();
    				for (Class e : exceptions) {
    					println("   "  + e.getName());
    				}
    			}
    			
    			// Parameter types
    			print("  Parameter types-> ");
    			Parameter[] parameters = m.getParameters();
    			if (parameters.length == 0) { 
    				System.out.println("NONE");
    			}
    			else {
    				System.out.println();
    				for (Parameter p : parameters) {
    					println("   " + p.getType());
    				}
    			}
    			
    			// Return type
    			println("  Return type: " + m.getReturnType());
    			
    			// Modifiers
    			print("  Modifiers: ");
    			int modifiers = m.getModifiers();
    			System.out.println(Modifier.toString(modifiers));
    		}
    	}
    	
    	// Fields
    	println("FIELDS ( " + c.getName() + " )");
    	print("Fields-> ");
    	Field[] fields = c.getDeclaredFields();
    	if (fields.length == 0) {
    		System.out.println("NONE");
    	} 
    	else {
    		System.out.println();
    		for (Field f : fields) {
    			println(" FIELD");
    			println("  Name: " + f.getName());
    			println("  Type: " + f.getType());
    			
    			// Modifiers
    			print("  Modifiers: ");
    			int modifiers = f.getModifiers();
    			System.out.println(Modifier.toString(modifiers));
    			
    			// Value
    			f.setAccessible(true);
    			try {
					Object value = f.get(obj);
					if (value == null) {
						println("  Value: " + value);
						continue;
					}
					Class valueClass = value.getClass();
					
					if (valueClass.isArray()) {
						// Component type
						println("  Component Type: " + valueClass.getComponentType());
						// Length
						// Entries
						continue;
					} 
					
					print("  Value: ");
					System.out.println(value);
					
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
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