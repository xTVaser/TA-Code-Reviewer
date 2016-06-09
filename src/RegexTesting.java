/**
 * Created by Tyler Wilding on 06/06/16.
 * TA-Code-Reviewer - COSC
 */
public class RegexTesting {

    public static void main(String[] args) {

        System.out.println("\tString test5=; "  .matches(".+\\s(.+?)(;|=)."));
        System.out.println("\t\tString test5=; ".matches(".+\\s(.+?)(;|=)."));
        System.out.println("String \ttest5\t=; ".matches(".+\\s(.+?)(;|=)."));
        System.out.println("String test5=\t; "  .matches(".+\\s(.+?)(;|=)."));
        System.out.println("String test5=6; "   .matches(".+\\s(.+?)(;|=)."));
        System.out.println("String test5= 6; "  .matches(".+\\s(.+?)(;|=)."));
        System.out.println("String test5 = 6; " .matches(".+\\s(.+?)(;|=)."));
        System.out.println();

        System.out.println("\t//blah blah blah" .matches(".*(//|/\\*\\*|/\\*|\\*).*"));
        System.out.println("\t// blah blah blah".matches(".*(//|/\\*\\*|/\\*|\\*).*"));
        System.out.println("//blahblahblah"     .matches(".*(//|/\\*\\*|/\\*|\\*).*"));
        System.out.println("/**blah blah blah"  .matches(".*(//|/\\*\\*|/\\*|\\*).*"));
        System.out.println("/** blah blah blah" .matches(".*(//|/\\*\\*|/\\*|\\*).*"));
        System.out.println("/**blahblahblah"    .matches(".*(//|/\\*\\*|/\\*|\\*).*"));
        System.out.println("/*blah blah blah"   .matches(".*(//|/\\*\\*|/\\*|\\*).*"));
        System.out.println("/* blah blah blah"  .matches(".*(//|/\\*\\*|/\\*|\\*).*"));
        System.out.println("/*blahblahblah"     .matches(".*(//|/\\*\\*|/\\*|\\*).*"));
        System.out.println("*blah blah blah"    .matches(".*(//|/\\*\\*|/\\*|\\*).*"));
        System.out.println("* blah blah blah"   .matches(".*(//|/\\*\\*|/\\*|\\*).*"));
        System.out.println("*blahblahblah"      .matches(".*(//|/\\*\\*|/\\*|\\*).*"));


        System.out.println();
        System.out.println("\t".matches(".*\t*.*"));
        System.out.println("\t ".matches(".*\t*.*"));
        System.out.println();
        System.out.println(" {".matches("[\t ]*[{}][\t ]*|[\t ]*(\\*/)[\t ]*"));
        System.out.println(" }".matches("[\t ]*[{}][\t ]*|[\t ]*(\\*/)[\t ]*"));
        System.out.println("{".matches("[\t ]*[{}][\t ]*|[\t ]*(\\*/)[\t ]*"));
        System.out.println("}".matches("[\t ]*[{}][\t ]*|[\t ]*(\\*/)[\t ]*"));
        System.out.println("*/".matches("[\t ]*[{}][\t ]*|[\t ]*(\\*/)[\t ]*"));
        System.out.println(" */".matches("[\t ]*[{}][\t ]*|[\t ]*(\\*/)[\t ]*"));
        System.out.println("\t{".matches("[\t ]*[{}][\t ]*|[\t ]*(\\*/)[\t ]*"));
        System.out.println("\t\t}".matches("[\t ]*[{}][\t ]*|[\t ]*(\\*/)[\t ]*"));
        System.out.println("\t\t*/".matches("[\t ]*[{}][\t ]*|[\t ]*(\\*/)[\t ]*"));
        System.out.println("*/\t\t".matches("[\t ]*[{}][\t ]*|[\t ]*(\\*/)[\t ]*"));
        System.out.println("public class Division { ".matches("[\t ]*[{}][\t ]*|[\t ]*(\\*/)[\t ]*"));
    }
}
