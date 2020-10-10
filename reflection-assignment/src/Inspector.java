
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
    	tabPrintln("CLASS");
    	tabPrintln("Class: " + c.getName());
    	
    	if (c.isArray()) {
			tabPrintln(" Component Type: " + c.getComponentType());
			tabPrintln(" Length: " + Array.getLength(obj));
			tabPrint(" Entries-> ");
			if (Array.getLength(obj) == 0) {
				System.out.println("NONE");
				return;
			}
			System.out.println();
			
			for (int i = 0; i < Array.getLength(obj); i++) {
				Object entry = Array.get(obj, i);
				
				if (entry == null) {
					tabPrintln("  Value: " + entry);
					continue;
				}
				
				if (!c.getComponentType().isPrimitive()) {
					tabPrint("  Value (ref): ");
					System.out.println(entry);
					
					if (recursive) {
						tabPrintln("    -> Recursively inspect");
						inspectClass(entry.getClass(), entry, recursive, depth+1);
						this.depth = depth;
					}
					continue;
				}
				
				tabPrintln("   Value: " + Array.get(obj, i));
			}
			return;
    	}
    	
    	// Immediate SuperClass
    	Class superclass = c.getSuperclass();
    	if (superclass != null) {
    		tabPrintln("SUPERCLASS -> Recursively Inspect");
        	tabPrintln("SuperClass: " + superclass.getName());
        	inspectClass(superclass, obj, recursive, depth+1);
        	this.depth = depth;
    	}
    	
    	// Interfaces
    	tabPrintln("INTERFACES ( " + c.getName() + " )");
    	tabPrint("Interfaces-> ");
    	Class[] interfaces = c.getInterfaces();
    	if (interfaces.length == 0) {
    		System.out.println("NONE");
    	}
    	else {
    		System.out.println();
    		for (Class i : interfaces) {
    			tabPrintln(" INTERFACE -> Recursively Inspect");
    			tabPrintln(" " + i.getName());
    			inspectClass(i, obj, recursive, depth+1);
    			this.depth = depth;
    		}
    	}
    	
    	// Constructors
    	inspectConstructors(c);
    	
    	// Methods
    	inspectMethods(c);
    	
    	// Fields
    	tabPrintln("FIELDS ( " + c.getName() + " )");
    	tabPrint("Fields-> ");
    	Field[] fields = c.getDeclaredFields();
    	if (fields.length == 0) {
    		System.out.println("NONE");
    	} 
    	else {
    		System.out.println();
    		for (Field f : fields) {
    			tabPrintln(" FIELD");
    			tabPrintln("  Name: " + f.getName());
    			tabPrintln("  Type: " + f.getType());
    			
    			// Modifiers
    			tabPrint("  Modifiers: ");
    			int modifiers = f.getModifiers();
    			System.out.println(Modifier.toString(modifiers));
    			
    			// Value
    			f.setAccessible(true);
    			try {
					Object value = f.get(obj);
					if (value == null) {
						tabPrintln("  Value: " + value);
						continue;
					}
					Class valueClass = value.getClass();
					
					// Check if value is an array
					if (valueClass.isArray()) {
						// Component type
						tabPrintln("  Component Type: " + valueClass.getComponentType());
						// Length
						tabPrintln("  Length: " + Array.getLength(value));
						// Entries
						tabPrint("  Entries-> ");
						if (Array.getLength(value) == 0) {
							System.out.println("NONE");
							continue;
						}
						System.out.println();
						
						for (int i = 0; i < Array.getLength(value); i++) {
							Object entry = Array.get(value, i);
							
							if (entry == null) {
								tabPrintln("  Value: " + entry);
								continue;
							}
							
							if (!valueClass.getComponentType().isPrimitive()) {
								tabPrint("  Value (ref): ");
								System.out.println(entry);
								
								if (recursive) {
									tabPrintln("    -> Recursively inspect");
									inspectClass(entry.getClass(), entry, recursive, depth+1);
									this.depth = depth;
								}
								continue;
							}
							
							tabPrintln("   Value: " + Array.get(value, i));
						}
						continue;
					}
					
					// Check if value is a reference to an object and recurse
					if (!f.getType().isPrimitive()) {
						
						tabPrint("  Value (ref): ");
						System.out.println(value);
						
						if (recursive) {
							tabPrintln("    -> Recursively inspect");
							inspectClass(valueClass, value, recursive, depth+1);
							this.depth = depth;
						}
						continue;
					}
					
					tabPrint("  Value: ");
					System.out.println(value);
					
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
    		}
    	}
    	
    }
    
    private void inspectConstructors(Class c) {
    	tabPrintln("CONSTRUCTORS ( " + c.getName() + " )");
    	tabPrint("Constructors-> ");
    	Constructor[] constructors = c.getDeclaredConstructors();
    	if (constructors.length == 0) {
    		System.out.println("NONE");
    	}
    	else {
    		System.out.println();
    		for (Constructor con : constructors) {
    			tabPrintln(" CONSTRUCTOR");
    			tabPrintln("  Name: " + con.getName());
    			
    			// Parameter types
    			tabPrint("  Parameter types-> ");
    			Parameter[] parameters = con.getParameters();
    			if (parameters.length == 0) { 
    				System.out.println("NONE");
    			}
    			else {
    				System.out.println();
    				for (Parameter p : parameters) {
    					tabPrintln("   " + p.getType());
    				}
    			}
    			
    			// Modifiers
    			tabPrint("  Modifiers: ");
    			int modifiers = con.getModifiers();
    			System.out.println(Modifier.toString(modifiers));
    		}
    	}
    }
    
    private void inspectMethods(Class c) {
    	tabPrintln("METHODS ( " + c.getName() + " )");
    	tabPrint("Methods-> ");
    	Method[] methods = c.getDeclaredMethods();
    	if (methods.length == 0) {
    		System.out.println("NONE");
    	}
    	else {
    		System.out.println();
    		for (Method m : methods) {
    			tabPrintln(" METHOD");
    			tabPrintln("  Name: " + m.getName());
    			
    			// Exceptions
    			tabPrint("  Exceptions-> ");
    			Class[] exceptions = m.getExceptionTypes();
    			if (exceptions.length == 0) {
    				System.out.println("NONE");
    			}
    			else {
    				System.out.println();
    				for (Class e : exceptions) {
    					tabPrintln("   "  + e.getName());
    				}
    			}
    			
    			// Parameter types
    			tabPrint("  Parameter types-> ");
    			Parameter[] parameters = m.getParameters();
    			if (parameters.length == 0) { 
    				System.out.println("NONE");
    			}
    			else {
    				System.out.println();
    				for (Parameter p : parameters) {
    					tabPrintln("   " + p.getType());
    				}
    			}
    			
    			// Return type
    			tabPrintln("  Return type: " + m.getReturnType());
    			
    			// Modifiers
    			tabPrint("  Modifiers: ");
    			int modifiers = m.getModifiers();
    			System.out.println(Modifier.toString(modifiers));
    		}
    	}
    }
    
    private void tabPrintln(String string) {
    	for (int i = 0; i < depth; i++) {
    		System.out.print("\t");
    	}
    	System.out.println(string);
    }
    
    private void tabPrint(String string) {
    	for (int i = 0; i < depth; i++) {
    		System.out.print("\t");
    	}
    	System.out.print(string);
    }

}