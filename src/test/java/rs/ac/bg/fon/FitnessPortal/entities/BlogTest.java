package rs.ac.bg.fon.FitnessPortal.entities;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class BlogTest {

    Blog b;

    @BeforeEach
    void setUp() {
        b = new Blog();
    }

    @AfterEach
    void tearDown() {
        b= null;
    }

    @Test
    void testBlogNull() {
        assertEquals(null, b.getId());
        assertEquals(null, b.getUser());
        assertEquals(null, b.getImageSrc());
        assertEquals(null, b.getText());
        assertEquals(null, b.getTitle());
        assertEquals(null, b.getType());

    }

    @Test
    void testBlogNotNull() {
        User u= new User();
       b= new Blog(1, "title", "text", "source", BlogType.DIET, u);

        assertEquals(1, b.getId());
        assertEquals(u, b.getUser());
        assertEquals("source", b.getImageSrc());
        assertEquals("text", b.getText());
        assertEquals("title", b.getTitle());
        assertEquals(BlogType.DIET, b.getType());

    }

    @ParameterizedTest
    @CsvSource({"1", "5", "7", "9"})
    void testSetBlogId(int id) {
        b.setId(id);

        assertEquals(id, b.getId());
    }
    @Test
    void testSetBlogIdThrowsNullPointer() {

        assertThrows(java.lang.NullPointerException.class, () -> b.setId(null));
    }

    @ParameterizedTest
    @CsvSource({"Title1", "Title2", "Title3", "Title4"})
    void testSetBlogTitle(String title) {
        b.setTitle(title);

        assertEquals(title, b.getTitle());
    }
    @Test
    void testSetBlogTitleThrowsNullPointer() {

        assertThrows(java.lang.NullPointerException.class, () -> b.setTitle(null));
    }

    @Test
    void testSetBlogTitleEmptyString() {
        assertThrows(java.lang.IllegalArgumentException.class,
                () -> b.setTitle("") );
    }

    @ParameterizedTest
    @CsvSource({"Text1", "Text2", "Text3", "Text4"})
    void testSetBlogText(String text) {
        b.setText(text);

        assertEquals(text, b.getText());
    }
    @Test
    void testSetBlogTextThrowsNullPointer() {

        assertThrows(java.lang.NullPointerException.class, () -> b.setText(null));
    }

    @Test
    void testSetBlogTextEmptyString() {
        assertThrows(java.lang.IllegalArgumentException.class,
                () -> b.setText("") );
    }

    @Test
    void setUser() {
        User u = new User();
        b.setUser(u);

        assertEquals(u, b.getUser());

    }

    @Test
    void setUserThrowsException() {

        assertThrows(java.lang.NullPointerException.class, () -> b.setUser(null));

    }

    @Test
    void testSetBlogType() {
        b.setType(BlogType.DIET);

        assertEquals(BlogType.DIET, b.getType());
    }
    @Test
    void testSetBlogTypeThrowsNullPointer() {

        assertThrows(java.lang.NullPointerException.class, () -> b.setType(null));
    }

    @ParameterizedTest
    @CsvSource({"Test1", "Test2", "Test3", "Test4"})
    void testSetBlogImageSource(String source) {
        b.setImageSrc(source);

        assertEquals(source, b.getImageSrc());
    }
    @Test
    void testSetBlogImageSourceThrowsNullPointer() {

        assertThrows(java.lang.NullPointerException.class, () -> b.setImageSrc(null));
    }

    @Test
    void testSetBlogImageSrcEmptyString() {
        assertThrows(java.lang.IllegalArgumentException.class,
                () -> b.setImageSrc("") );
    }
}