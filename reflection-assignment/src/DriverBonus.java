import java.lang.reflect.Method;

public class DriverBonus {

	public static void main(String[] args) {
		
		if (args.length != 3) {
			System.err.println("Invalid # of arguments");
			System.err.println("ARGUMENTS: [Inspector Class] [Object Name] [Recursive (true/false)]");
			return;
		}
		
		try {
			Class inspectorClass = Class.forName(args[0]);
			Method inspectMethod = 
					inspectorClass.getDeclaredMethod("inspect", new Class[] {Object.class, boolean.class});
			
			Class objClass = Class.forName(args[1]);
			Object obj = objClass.newInstance();
			boolean recursive = Boolean.parseBoolean(args[2]);
			
			Object inspectorObj = inspectorClass.newInstance();
			inspectMethod.invoke(inspectorObj, new Object[] {obj, recursive});
			
		} catch (ClassNotFoundException cnfe) {
			System.err.println("Invalid class name entered.");
		} catch (NoSuchMethodException nsme) {
			System.err.println("No inspect(Object, boolean) method found in " + args[0]);
		} catch (Exception e) {
			System.err.println("An unexpected error occured.");
			e.printStackTrace();
		}
	}
}
