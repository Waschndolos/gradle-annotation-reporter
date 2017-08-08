import de.waschndolos.testannotation.MyAnnotation;

@MyAnnotation(description="desc", severity="High")
public class TestClass {

    private int i = 223;

    @MyAnnotation(description="wtf", severity="Ultra")
    public int add(int k) {
        return i + k;
    }

    @Override
    private void test() {
        System.out.println("lalala");
    }

}
