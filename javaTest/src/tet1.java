import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class tet1 {
	
	  
	  public static BeanFactory factory = new ClassPathXmlApplicationContext("applicationContext.xml");
	  
	  public static void main(String[] args) {
		  
		  HelloWorld hello =factory.getBean("helloWorld",HelloWorld.class);  
		  System.out.println(hello.hello());  
	
	  }
	  
	  
}
