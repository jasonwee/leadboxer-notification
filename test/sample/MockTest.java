package sample;

import static org.mockito.Mockito.*;

import java.util.List;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * https://github.com/mockito/mockito
 *
 * @author jason
 *
 */
public class MockTest {

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

}
