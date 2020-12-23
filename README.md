# CPSC 501 Assignment 2 - Reflection

All source code can be found in `reflection-assignment/src` and all output script text files in `reflection-assignment`.

*Bonus code can be found in* `reflection-assignment/src/DriverBonus.java`

## Refactoring

### Worrisome Comments

The first bad code smell I noticed was in my print wrappers that I made in order to print to standard output with a certain number of tabs.

```java
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

```

I realised that naming the methods `print` and `println` did not accurately reflect what they were doing. It was especially confusing because in other parts of my code, when I did not want to print with tabs I would use the standard `System.out.println()` function.

In order to deal with this bad code smell I used the `Rename Method` technique and changed the names of the two functions to the more appropriate `tabPrint` and `tabPrintln`.

```java
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
```

See commit: `95e6db6ba3d50532d27d95a058b2d773d447d3ac`


### Long code

The next couple of refactorings I performed dealt with the fact that my `inspectClass` method was getting very long and hard to read. As a result I decided to apply the `Extract Method` technique multiple times in order to better organize the code.

I first extracted the funtionality that dealt with inspecting the constructors of a class into a separate method.

```java
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
```

See commit: `df0cc699a6d032d8350873b2b1c2aad2eeb1cb7c`


I next applied the same refactoring to the code responsible for inspecting the methods of a class.

```java
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
```

See commit: `a491fe1a3a9559c67ed840dddd95413bb96e12fc`


Next, I applied the same refractoring to the code responsible for inspecting the fields of a class.

```java
    private void inspectFields(Class c, Object obj, boolean recursive, int depth) {
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
```

See commits: `474804343f18f2968b6bba87ef123c3eb18aa4e5` and `f20563ae339f2d705e35ec33c0c24de2ad1de8ad`


Finally, I extracted the code that handles an array being directly passed in to the `inspect` method, as in this case the code behaves differently than if the object was not an array.

```java
    private void inspectArray(Class c, Object obj, boolean recursive, int depth) {
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
			
			tabPrintln("  Value: " + entry);
		}
    }
```

See commit: `55648e1261afb481d8133fb6646a90d9ef3b1b98`


### Additional Refactorings

Not all refactorings that could have been performed were performed. One bad code smell in particular that was left unaddressed was a case of `Duplicate Code`, where the code for handling arrays inspected directly in `inspectArray()` and the code for handling arrays found in fields in `inspectFields()` is almost identical.

The reason this was not addressed is because there is a slight difference in the code with regards to the number of spaces used for indenting each line, with `inspectFields()` needing one additional space. In order to keep the output files as readable and consistent as possible, I opted to leave this similar code as it is, deciding that this particular refactoring was not worth sacrificing readability.


