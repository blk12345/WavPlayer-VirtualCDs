package test;

public class thread extends Thread{

	@Override
	public void run() {
		for(int i=5;i>0;i--) {
			System.out.println(i);
			if(main.stream!=null) {
				
						main.clip.loop(0);
					
					
				}
		}
		
	}
}
