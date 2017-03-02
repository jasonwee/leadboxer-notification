package unit.models;

import static play.test.Helpers.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

import org.junit.Test;

import com.avaje.ebean.PagedList;
import com.google.common.collect.ImmutableMap;

import models.NotificationSpecification;
import play.Application;
import play.Environment;
import play.Mode;
import play.db.Database;
import play.db.Databases;
import play.inject.guice.GuiceApplicationBuilder;
import play.test.Helpers;
import play.test.WithApplication;


/**
 * 
 * @author jason
 *
 */
public class NotificationSpecificationTest extends WithApplication {
	
	 @Test
	  public void testMock() {
	    
		// Create and train mock
		List<String> mockedList = mock(List.class);
		when(mockedList.get(0)).thenReturn("first");

		// check value
		assertEquals("first", mockedList.get(0));

		// verify interaction
		verify(mockedList).get(0);
	  }
	 
	  public void testMock1() {
	    
		// Create and train mock
		 NotificationSpecification ns = new NotificationSpecification();
		NotificationSpecification mockedList = mock(NotificationSpecification.class);
		when(mockedList.getNotificationSpecification("1", "2", "3")).thenReturn(ns);
		

		NotificationSpecification mytest = mockedList.getNotificationSpecification("1", "2", "3");
		System.out.println(mytest.toString());
		
	  }
	 
	 
	 
	 @Test
	 public void testIsAdmin() {
		 /*
	   // Create and train mock repository
	   NotificationSpecification repositoryMock = mock(NotificationSpecification.class);
	   Set<Role> roles = new HashSet<Role>();
	   roles.add(new Role("ADMIN"));
	   when(repositoryMock.findUserRoles(any(User.class))).thenReturn(roles);
	   
	   // Test Service
	   UserService userService = new UserService(repositoryMock);
	   User user = new User(1, "Johnny Utah");
	   assertTrue(userService.isAdmin(user));
	   verify(repositoryMock).findUserRoles(user);
	   */
	 }
	 
	 
	 /*
	 	 @Test
	 public void getNotificationSpecification1() {

		 Database database = Databases.createFrom(
				 "com.mysql.jdbc.Driver",
			     "jdbc:mysql://localhost/test"
				 );
		 
		 Database inMem = Databases.inMemory(
			        "mydatabase",
			        ImmutableMap.of(
			                "MODE", "MYSQL"
			        ),
			        ImmutableMap.of(
			                "logStatements", true
			        )
			);
		 
		 database.shutdown();
		 */
		 
			/*
			ClassLoader classLoader = classLoader();
			Application application = new GuiceApplicationBuilder()
					//.in(new Environment(new File("/home/jason/work/svn/leadboxer-notification/"), classLoader, Mode.TEST))
					.in(new Environment(new File("path/to/app"), classLoader, Mode.DEV))
					.build();

			running(application, () -> {
				NotificationSpecification ns = NotificationSpecification.getNotificationSpecification("1", "2", "3");
				System.out.println("here " + ns.toString());
			});

	 }

	    private ClassLoader classLoader() {
	        return new URLClassLoader(new URL[0]);
	    }
	  			*/  

	@Test
	public void testPagination() {
		running(fakeApplication(inMemoryDatabase()), new Runnable() {
			public void run() {
				PagedList<NotificationSpecification> nss = NotificationSpecification.page(1, 20, "nKey", "ASC", "");
				assertEquals(0, nss.getTotalPageCount());
				assertEquals(0, nss.getList().size());
			}
		});
	}
	
	@Test
	public void testByDatataset() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			List<NotificationSpecification> lists = NotificationSpecification.byDataset("a1d90dccc04df83f26553dc753ed41f2");
			assertEquals(0, lists.size());
		});
	}

	@Test
	public void testDistinctDatasets() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			List<NotificationSpecification> lists = NotificationSpecification.distinctDatasets();
			assertEquals(0, lists.size());
		});
	}

	@Test
	public void testGetNotificationSpecification() {

		running(fakeApplication(inMemoryDatabase()), () -> {
			NotificationSpecification ns = NotificationSpecification.getNotificationSpecification("a1d90dccc04df83f26553dc753ed41f2", "foo", "bar");
			assertNull(ns);
		});
	}
	
    @Test
    public void findById() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
           public void run() {
               NotificationSpecification ns = NotificationSpecification.find.byId(1l);
               assertNull(ns);
           }
        });
    }

}