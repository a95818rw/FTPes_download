package ftps;

public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String target = "/asdsa:";
		
		if(
				target.contains("\\") || target.contains("|") || target.contains("\"") ||
				target.contains("<") || target.contains(":") || target.contains(">") ||
				target.contains("*") || target.contains("?")
			) System.out.print("The target is illegal.");
		
		if(!target.contains(":")) System.out.println(":");
		
		System.out.print(target);

	}

}
